package com.ohmyvelocity.domain;

public record JoinMessagePlan(boolean suppressVanilla, String toPlayer, String broadcast) {
    public static JoinMessagePlan disabled() {
        return new JoinMessagePlan(false, "", "");
    }

    public boolean hasToPlayer() {
        return toPlayer != null && !toPlayer.isBlank();
    }

    public boolean hasBroadcast() {
        return broadcast != null && !broadcast.isBlank();
    }
}
