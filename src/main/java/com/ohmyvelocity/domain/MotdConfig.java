package com.ohmyvelocity.domain;

import java.util.List;

public record MotdConfig(
        boolean enabled,
        List<MotdEntry> entries,
        int maxPlayers,
        List<String> hover,
        List<String> samplePlayers,
        boolean hidePlayerCount,
        List<String> targetServers) {
    public MotdConfig {
        entries = List.copyOf(entries);
        hover = List.copyOf(hover);
        samplePlayers = List.copyOf(samplePlayers);
        targetServers = List.copyOf(targetServers);
    }

    public static MotdConfig defaults() {
        return new MotdConfig(
                false,
                List.of(new MotdEntry("<gold><bold>lkjsxc.com</bold>", "<gray>{online}/{max} online", 1, "")),
                128,
                List.of(),
                List.of(),
                false,
                List.of("*"));
    }
}
