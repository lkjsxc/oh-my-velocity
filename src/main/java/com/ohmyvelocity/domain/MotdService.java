package com.ohmyvelocity.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public final class MotdService {
    private static final int SAMPLE_PLAYER_LIMIT = 15;

    public Optional<MotdPlan> plan(MotdConfig config, int online, String virtualHost, Random random) {
        return plan(config, online, virtualHost, random, List.of());
    }

    public Optional<MotdPlan> plan(
            MotdConfig config,
            int online,
            String virtualHost,
            Random random,
            List<String> playerNames) {
        return plan(config, online, virtualHost, random, "", "", playerNames);
    }

    public Optional<MotdPlan> plan(
            MotdConfig config,
            int online,
            String virtualHost,
            Random random,
            String date,
            String time) {
        return plan(config, online, virtualHost, random, date, time, List.of());
    }

    public Optional<MotdPlan> plan(
            MotdConfig config,
            int online,
            String virtualHost,
            Random random,
            String date,
            String time,
            List<String> playerNames) {
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
                samplePlayers(playerNames, online),
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

    private static List<String> samplePlayers(List<String> playerNames, int online) {
        int visibleLimit = Math.min(SAMPLE_PLAYER_LIMIT, Math.max(0, online));
        List<String> samples = new ArrayList<>();
        List<String> safePlayerNames = playerNames == null ? List.of() : playerNames;
        for (String playerName : safePlayerNames) {
            if (samples.size() >= visibleLimit) {
                break;
            }
            if (playerName != null && !playerName.isBlank()) {
                samples.add(playerName);
            }
        }
        if (online > SAMPLE_PLAYER_LIMIT) {
            int remaining = Math.max(0, online - samples.size());
            if (remaining > 0) {
                samples.add("and " + remaining + " more");
            }
        }
        return samples;
    }
}
