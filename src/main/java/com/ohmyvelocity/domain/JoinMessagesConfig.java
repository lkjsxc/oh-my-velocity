package com.ohmyvelocity.domain;

import java.util.List;

public record JoinMessagesConfig(
        boolean enabled,
        String toPlayer,
        String broadcast,
        boolean firstJoinOnly,
        boolean suppressVanilla) {
}
