package com.ohmyvelocity.domain;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NetworkMessagePlannerTest {
    @Test
    void disablesRepeatFirstJoinMessage() {
        ProxyMessagesConfig config = new ProxyMessagesConfig(
                true,
                new LocalizedTemplateConfig(Map.of("en", "Hi {player}"), Map.of("en", "{player} joined")),
                new LocalizedTemplateConfig(Map.of(), Map.of("en", "{player} left")),
                true);

        JoinMessagePlan plan = NetworkMessagePlanner.joinPlan(config, "Ada", 2, 128, "hub", Locale.ENGLISH, false);

        assertFalse(plan.hasToPlayer());
        assertFalse(plan.hasBroadcast());
    }

    @Test
    void rendersLeavePlaceholders() {
        ProxyMessagesConfig config = new ProxyMessagesConfig(
                true,
                new LocalizedTemplateConfig(Map.of(), Map.of()),
                new LocalizedTemplateConfig(Map.of(), Map.of("en", "{player} left {server} {online}/{max}")),
                false);

        JoinMessagePlan plan = NetworkMessagePlanner.leavePlan(config, "Ada", 1, 128, "hub", Locale.ENGLISH);

        assertEquals("Ada left hub 1/128", plan.broadcast());
    }
}
