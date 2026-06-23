package com.ohmyvelocity.domain;

import java.util.List;

public record MotdConfig(
        boolean enabled,
        List<MotdEntry> entries,
        int maxPlayers,
        boolean hidePlayerCount,
        List<String> targetHosts) {
    public MotdConfig {
        entries = List.copyOf(entries);
        targetHosts = List.copyOf(targetHosts);
    }

    public static MotdConfig defaults() {
        return new MotdConfig(
                false,
                List.of(new MotdEntry("<gold><bold>lkjsxc.com</bold>", "<gray>{online}/{max} online", 1, "")),
                128,
                false,
                List.of("*"));
    }
}
