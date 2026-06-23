package com.ohmyvelocity.feature.motd;

import com.ohmyvelocity.adapter.config.ConfigManager;
import com.ohmyvelocity.domain.MotdPlan;
import com.ohmyvelocity.domain.MotdService;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MotdPingListener {
    private final ProxyServer server;
    private final ConfigManager configManager;
    private final MotdService motd;
    private final Path dataDirectory;
    private final Logger logger;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Random random = new Random();
    private final Map<String, Optional<Favicon>> icons = new ConcurrentHashMap<>();

    public MotdPingListener(
            ProxyServer server,
            ConfigManager configManager,
            MotdService motd,
            Path dataDirectory,
            Logger logger) {
        this.server = server;
        this.configManager = configManager;
        this.motd = motd;
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        String host = event.getConnection().getRawVirtualHost().orElse("");
        int online = server.getPlayerCount();
        List<String> hoverNames = playerNames();
        Optional<MotdPlan> planned = motd.plan(
                configManager.config().motd(),
                online,
                host,
                random,
                currentDateTime(DateTimeFormatter.ISO_LOCAL_DATE),
                currentDateTime(DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT)),
                hoverNames);
        if (planned.isEmpty()) {
            return;
        }
        MotdPlan plan = planned.get();
        ServerPing.Builder builder = event.getPing().asBuilder()
                .description(miniMessage.deserialize(plan.description()));
        if (plan.hidePlayerCount()) {
            builder.nullPlayers();
        } else {
            builder.onlinePlayers(online)
                    .maximumPlayers(plan.maxPlayers())
                    .clearSamplePlayers();
            builder.samplePlayers(plan.samplePlayers().stream()
                    .map(MotdPingListener::samplePlayer)
                    .toList());
        }
        icon(plan.icon()).ifPresent(builder::favicon);
        event.setPing(builder.build());
    }

    public void clearIconCache() {
        icons.clear();
    }

    private Optional<Favicon> icon(String configured) {
        if (configured == null || configured.isBlank()) {
            return Optional.empty();
        }
        return icons.computeIfAbsent(configured, this::loadIcon);
    }

    private Optional<Favicon> loadIcon(String configured) {
        Path path = Path.of(configured);
        Path resolved = path.isAbsolute() ? path : dataDirectory.resolve(path);
        try {
            return Optional.of(Favicon.create(resolved));
        } catch (IOException ex) {
            logger.warn("Could not load MOTD icon {}", resolved, ex);
            return Optional.empty();
        }
    }

    private List<String> playerNames() {
        return server.getAllPlayers().stream()
                .map(Player::getUsername)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    private static ServerPing.SamplePlayer samplePlayer(String name) {
        UUID id = UUID.nameUUIDFromBytes(name.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return new ServerPing.SamplePlayer(name, id);
    }

    private static String currentDateTime(DateTimeFormatter formatter) {
        return formatter.format(LocalDateTime.now());
    }
}
