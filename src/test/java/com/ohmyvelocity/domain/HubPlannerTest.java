package com.ohmyvelocity.domain;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HubPlannerTest {
    @Test
    void plansDisabledUnavailableAlreadyConnectedAndConnect() {
        HubCommandConfig enabled = config(true);

        assertEquals(HubCommandAction.UNAVAILABLE,
                HubPlanner.plan(enabled, Locale.ENGLISH, "survival", false).action());
        assertEquals(HubCommandAction.ALREADY_CONNECTED,
                HubPlanner.plan(enabled, Locale.ENGLISH, "hub", true).action());
        assertEquals(HubCommandAction.CONNECT,
                HubPlanner.plan(enabled, Locale.ENGLISH, "survival", true).action());
        assertEquals(HubCommandAction.DISABLED,
                HubPlanner.plan(config(false), Locale.ENGLISH, "survival", true).action());
    }

    private static HubCommandConfig config(boolean enabled) {
        return new HubCommandConfig(enabled, "hub", new LocalizedMessagesConfig(Map.of(
                "connecting", Map.of("en", "go {server}"),
                "already-connected", Map.of("en", "already {server}"),
                "unavailable", Map.of("en", "missing {server}"),
                "disabled", Map.of("en", "off {server}"))));
    }
}
