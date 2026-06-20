package com.ohmyvelocity.domain;

import com.ohmyvelocity.adapter.config.ConfigManager;

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
        return plan(online, virtualHost, random, "", "");
    }

    public Optional<MotdPlan> plan(int online, String virtualHost, Random random, String date, String time) {
        MotdConfig config = configManager.config().motd();
        if (!config.enabled() || !matchesHost(config, virtualHost)) {
            return Optional.empty();
        }
        MotdEntry entry = select(config, random);
        int max = Math.max(0, config.maxPlayers());
        Map<String, String> values = values("", online, max, normalizeHost(virtualHost), 0, date, time);
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

    static Map<String, String> values(
            String player,
            int online,
            int max,
            String server,
            long ping,
            String date,
            String time) {
        String host = server == null ? "" : server;
        return Map.of(
                "player", player,
                "online", String.valueOf(online),
                "max", String.valueOf(max),
                "server", host,
                "host", host,
                "ping", String.valueOf(ping),
                "date", date == null ? "" : date,
                "time", time == null ? "" : time);
    }

    private static boolean matchesHost(MotdConfig config, String virtualHost) {
        if (config.targetHosts().isEmpty() || config.targetHosts().contains("*")) {
            return true;
        }
        String host = normalizeHost(virtualHost);
        return config.targetHosts().stream().map(item -> item.toLowerCase(Locale.ROOT)).anyMatch(host::equals);
    }

    private static String normalizeHost(String virtualHost) {
        String host = virtualHost == null ? "" : virtualHost.toLowerCase(Locale.ROOT);
        int portIndex = host.indexOf(':');
        return portIndex < 0 ? host : host.substring(0, portIndex);
    }

    private static String render(String template, Map<String, String> values) {
        return LegacyColorTranslator.toMiniMessage(PlaceholderFormatter.format(template, values));
    }

    private static java.util.List<String> samplePlayers(MotdConfig config, Map<String, String> values) {
        return config.samples().stream().map(item -> PlaceholderFormatter.format(item, values)).toList();
    }
}
