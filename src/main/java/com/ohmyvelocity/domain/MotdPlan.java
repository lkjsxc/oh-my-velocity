package com.ohmyvelocity.domain;

import java.util.List;

public record MotdPlan(
        String description,
        int maxPlayers,
        boolean hidePlayerCount,
        List<String> samplePlayers,
        String icon) {
    public MotdPlan {
        samplePlayers = List.copyOf(samplePlayers);
    }
}
