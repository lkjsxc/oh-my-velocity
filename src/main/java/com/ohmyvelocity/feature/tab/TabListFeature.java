package com.ohmyvelocity.feature.tab;

import com.ohmyvelocity.adapter.config.ConfigManager;
import com.ohmyvelocity.domain.TabConfig;
import com.ohmyvelocity.domain.TabRenderContext;
import com.ohmyvelocity.domain.TabRenderPlan;
import com.ohmyvelocity.domain.TabRenderService;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TabListFeature {
    private final Object plugin;
    private final ProxyServer server;
    private final ConfigManager configManager;
    private final TabRenderService renderer;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Map<UUID, Set<UUID>> managedEntries = new ConcurrentHashMap<>();
    private ScheduledTask task;

    public TabListFeature(Object plugin, ProxyServer server, ConfigManager configManager, TabRenderService renderer) {
        this.plugin = plugin;
        this.server = server;
        this.configManager = configManager;
        this.renderer = renderer;
    }

    public void start() {
        stop();
        TabConfig config = configManager.config().tab();
        if (!config.enabled()) {
            clearHeaderFooter();
            return;
        }
        task = server.getScheduler()
                .buildTask(plugin, this::updateAll)
                .repeat(Duration.ofSeconds(config.refreshIntervalSeconds()))
                .schedule();
        updateAll();
    }

    public void reload() { start(); }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        clearManagedEntries();
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        updateAll();
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        updateAll();
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        managedEntries.remove(event.getPlayer().getUniqueId());
        server.getScheduler().buildTask(plugin, this::updateAll).delay(Duration.ofSeconds(1)).schedule();
    }

    public void updateAll() {
        TabConfig config = configManager.config().tab();
        if (!config.enabled()) {
            return;
        }
        Collection<Player> players = server.getAllPlayers();
        String date = currentDateTime(DateTimeFormatter.ISO_LOCAL_DATE);
        String time = currentDateTime(DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT));
        Map<UUID, TabRenderPlan> plans = new HashMap<>();
        players.forEach(player -> plans.put(
                player.getUniqueId(),
                renderer.render(config, context(player, group(player, config), date, time))));
        players.forEach(viewer -> updateViewer(viewer, players, plans));
    }

    private void updateViewer(Player viewer, Collection<Player> players, Map<UUID, TabRenderPlan> plans) {
        TabRenderPlan viewerPlan = plans.get(viewer.getUniqueId());
        if (viewerPlan == null) {
            return;
        }
        viewer.getTabList().setHeaderAndFooter(component(viewerPlan.header()), component(viewerPlan.footer()));
        removeStaleEntries(viewer, players);
        for (Player subject : players) {
            updateEntry(viewer, subject, plans.get(subject.getUniqueId()));
        }
    }

    private void updateEntry(Player viewer, Player subject, TabRenderPlan plan) {
        if (plan == null) {
            return;
        }
        int order = plan.order() + Math.floorMod(subject.getUsername().toLowerCase(Locale.ROOT).hashCode(), 1000);
        Component displayName = component(plan.displayName());
        if (!viewer.getTabList().containsEntry(subject.getUniqueId())) {
            TabListEntry entry = viewer.getTabList()
                    .buildEntry(subject.getGameProfile(), displayName, latency(subject), 0);
            viewer.getTabList().addEntry(entry);
            entry.setListOrder(order);
            managedEntries.computeIfAbsent(viewer.getUniqueId(), ignored -> ConcurrentHashMap.newKeySet())
                    .add(subject.getUniqueId());
        }
        viewer.getTabList().getEntry(subject.getUniqueId()).ifPresent(entry -> entry
                .setDisplayName(displayName)
                .setLatency(latency(subject))
                .setListOrder(order));
    }

    private TabRenderContext context(Player player, String group, String date, String time) {
        return new TabRenderContext(
                player.getUsername(),
                currentServer(player),
                group,
                server.getPlayerCount(),
                Math.max(configManager.config().motd().maxPlayers(), server.getConfiguration().getShowMaxPlayers()),
                player.getPing(),
                date,
                time);
    }

    private static String currentDateTime(DateTimeFormatter formatter) { return formatter.format(LocalDateTime.now()); }

    private String group(Player player, TabConfig config) {
        for (String group : config.groupOrder()) {
            if (player.hasPermission("ohmyvelocity.group." + group) || player.hasPermission("group." + group)) {
                return group;
            }
        }
        return "default";
    }

    private String currentServer(Player player) { return player.getCurrentServer().map(c -> c.getServerInfo().getName()).orElse(""); }

    private Component component(List<String> lines) { return component(String.join("\n", lines)); }

    private Component component(String text) {
        return text == null || text.isBlank() ? Component.empty() : miniMessage.deserialize(text);
    }

    private static int latency(Player player) { return (int) Math.min(Integer.MAX_VALUE, player.getPing()); }

    private void removeStaleEntries(Player viewer, Collection<Player> players) {
        Set<UUID> managed = managedEntries.get(viewer.getUniqueId());
        if (managed == null || managed.isEmpty()) {
            return;
        }
        Set<UUID> online = new HashSet<>();
        players.forEach(player -> online.add(player.getUniqueId()));
        managed.removeIf(subjectId -> {
            if (online.contains(subjectId)) {
                return false;
            }
            viewer.getTabList().removeEntry(subjectId);
            return true;
        });
    }

    private void clearHeaderFooter() {
        server.getAllPlayers().forEach(Player::clearPlayerListHeaderAndFooter);
    }

    private void clearManagedEntries() {
        server.getAllPlayers().forEach(viewer -> {
            Set<UUID> managed = managedEntries.remove(viewer.getUniqueId());
            if (managed != null) {
                managed.forEach(subjectId -> viewer.getTabList().removeEntry(subjectId));
            }
        });
        managedEntries.clear();
    }
}
