package com.ohmyvelocity.feature.tab;

import com.ohmyvelocity.domain.ConfigManager;
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
import com.velocitypowered.api.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public final class TabListFeature {
    private final Object plugin;
    private final ProxyServer server;
    private final ConfigManager configManager;
    private final TabRenderService renderer;
    private final TabScoreboardBridge scoreboard = new TabScoreboardBridge();
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
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
            return;
        }
        task = server.getScheduler()
                .buildTask(plugin, this::updateAll)
                .repeat(Duration.ofSeconds(config.refreshIntervalSeconds()))
                .schedule();
        updateAll();
    }

    public void reload() {
        start();
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
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
        server.getScheduler().buildTask(plugin, this::updateAll).delay(Duration.ofSeconds(1)).schedule();
    }

    public void updateAll() {
        TabConfig config = configManager.config().tab();
        if (!config.enabled()) {
            return;
        }
        Collection<Player> players = server.getAllPlayers();
        players.forEach(viewer -> updateViewer(viewer, players, config));
    }

    private void updateViewer(Player viewer, Collection<Player> players, TabConfig config) {
        TabRenderPlan viewerPlan = renderer.render(config, context(viewer, group(viewer, config)));
        viewer.getTabList().setHeaderAndFooter(component(viewerPlan.header()), component(viewerPlan.footer()));
        for (Player subject : players) {
            updateEntry(viewer, subject, config);
        }
        scoreboard.update(viewer, players, config, player -> group(player, config));
    }

    private void updateEntry(Player viewer, Player subject, TabConfig config) {
        String group = group(subject, config);
        TabRenderPlan plan = renderer.render(config, context(subject, group));
        viewer.getTabList().getEntry(subject.getUniqueId()).ifPresent(entry -> entry
                .setDisplayName(component(plan.displayName()))
                .setLatency((int) Math.min(Integer.MAX_VALUE, subject.getPing()))
                .setListOrder(plan.order() + Math.floorMod(subject.getUsername().toLowerCase(Locale.ROOT).hashCode(), 1000)));
    }

    private TabRenderContext context(Player player, String group) {
        return new TabRenderContext(
                player.getUsername(),
                currentServer(player),
                group,
                server.getPlayerCount(),
                Math.max(configManager.config().motd().maxPlayers(), server.getConfiguration().getShowMaxPlayers()),
                player.getPing());
    }

    private String group(Player player, TabConfig config) {
        for (String group : config.groupOrder()) {
            if (player.hasPermission("ohmyvelocity.group." + group) || player.hasPermission("group." + group)) {
                return group;
            }
        }
        return "default";
    }

    private String currentServer(Player player) {
        return player.getCurrentServer().map(connection -> connection.getServerInfo().getName()).orElse("");
    }

    private Component component(List<String> lines) {
        return component(String.join("\n", lines));
    }

    private Component component(String text) {
        return text == null || text.isBlank() ? Component.empty() : miniMessage.deserialize(text);
    }

}
