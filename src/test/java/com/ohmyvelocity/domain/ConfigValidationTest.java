package com.ohmyvelocity.domain;

import com.ohmyvelocity.adapter.config.YamlConfigLoader;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigValidationTest {
    @Test
    void rejectsInvalidRestartInterval() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "restart:",
                        "  interval-hours: 0"))));

        org.junit.jupiter.api.Assertions.assertTrue(error.getMessage().contains("restart.interval-hours"));
    }

    @Test
    void rejectsUnsupportedMotdWeight() {
        assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "motd:",
                        "  enabled: true",
                        "  entries:",
                        "    - line1: \"Bad\"",
                        "      line2: \"\"",
                        "      weight: 0"))));
    }

    @Test
    void rejectsEmptyHubTargetWhenEnabled() {
        assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "hub:",
                        "  enabled: true",
                        "  target-server: \"\""))));
    }
}
