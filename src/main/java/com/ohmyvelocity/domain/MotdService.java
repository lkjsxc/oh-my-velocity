package com.ohmyvelocity.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public final class MotdService {
    private final ConfigManager configManager;

    public MotdService(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public Optional<MotdPlan> plan(int online, String virtualHost, Random random) {
        MotdConfig config = configManager.config().motd();
        if (!config.enabled() || !matchesHost(config, virtualHost)) {
            return Optional.empty();
        }
        MotdEntry entry = select(config, random);
        int max = Math.max(0, config.maxPlayers());
        Map<String, String> values = values("", online, max, "", 0);
        String line1 = render(entry.line1(), values);
        String line2 = render(entry.line2(), values);
        return Optional.of(new MotdPlan(
                line2.isBlank() ? line1 : line1 + "\n" + line2,
                max,
                config.hidePlayerCount(),
                samplePlayers(config, values),
                entry.icon()));
    }

    static MotdEntry select(MotdConfig config, Random random) {
        int total = config.entries().stream().mapToInt(entry -> Math.max(0, entry.weight())).sum();
        if (total <= 0) {
            return config.entries().getFirst();
        }
        int selected = random.nextInt(total);
        for (MotdEntry entry : config.entries()) {
            selected -= Math.max(0, entry.weight());
            if (selected < 0) {
                return entry;
            }
        }
        return config.entries().getLast();
    }

    static Map<String, String> values(String player, int online, int max, String server, long ping) {
        LocalDateTime now = LocalDateTime.now();
        return Map.of(
                "player", player,
                "online", String.valueOf(online),
                "max", String.valueOf(max),
                "server", server == null ? "" : server,
                "ping", String.valueOf(ping),
                "date", DateTimeFormatter.ISO_LOCAL_DATE.format(now),
                "time", DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT).format(now));
    }

    private static boolean matchesHost(MotdConfig config, String virtualHost) {
        if (config.targetServers().isEmpty() || config.targetServers().contains("*")) {
            return true;
        }
        String host = virtualHost == null ? "" : virtualHost.toLowerCase(Locale.ROOT);
        return config.targetServers().stream().map(item -> item.toLowerCase(Locale.ROOT)).anyMatch(host::equals);
    }

    private static String render(String template, Map<String, String> values) {
        return LegacyColorTranslator.toMiniMessage(PlaceholderFormatter.format(template, values));
    }

    private static java.util.List<String> samplePlayers(MotdConfig config, Map<String, String> values) {
        java.util.List<String> source = config.samplePlayers().isEmpty() ? config.hover() : config.samplePlayers();
        return source.stream().map(item -> PlaceholderFormatter.format(item, values)).toList();
    }
}
