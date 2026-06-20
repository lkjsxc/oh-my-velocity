package com.ohmyvelocity.domain;

import com.ohmyvelocity.adapter.config.ConfigManager;
import com.ohmyvelocity.adapter.config.YamlConfigLoader;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "motd:",
                        "  enabled: true",
                        "  entries:",
                        "    - line1: \"Bad\"",
                        "      line2: \"\"",
                        "      weight: 0"))));

        assertTrue(error.getMessage().contains("motd.entries[0].weight"));
    }

    @Test
    void rejectsEmptyHubTargetWhenEnabled() {
        assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "hub:",
                        "  enabled: true",
                        "  target-server: \"\""))));
    }

    @Test
    void rejectsLegacyConfigKeys() {
        assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "join-" + "messages:",
                        "  enabled: true"))));
    }

    @Test
    void rejectsInvalidScalarTypes() {
        assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "restart:",
                        "  random-jitter-minutes: not-a-number"))));
    }

    @Test
    void rejectsInvalidTemplatesOnReload() throws Exception {
        java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("omv-invalid-template");
        java.nio.file.Files.writeString(dir.resolve("config.yml"), """
                messages:
                  enabled: true
                  join:
                    to-player:
                      en: "<green"
                    broadcast:
                      en: ""
                  leave:
                    broadcast:
                      en: ""
                """);

        assertThrows(IllegalArgumentException.class, () -> new ConfigManager(dir).reload());
    }

    @Test
    void rejectsWrongShapedTopLevelSection() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of("restart: true"))));

        assertTrue(error.getMessage().contains("restart"));
    }

    @Test
    void rejectsWrongShapedLocalizedTemplate() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "messages:",
                        "  join:",
                        "    to-player: [bad]"))));

        assertTrue(error.getMessage().contains("messages.join.to-player"));
    }

    @Test
    void rejectsScalarLocalizedTemplate() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "messages:",
                        "  join:",
                        "    to-player: \"hello\""))));

        assertTrue(error.getMessage().contains("messages.join.to-player"));
    }

    @Test
    void rejectsEmptyHubLocaleMapOverride() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "hub:",
                        "  messages:",
                        "    disabled:"))));

        assertTrue(error.getMessage().contains("hub.messages.disabled"));
    }

    @Test
    void rejectsWrongShapedMapListItem() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "motd:",
                        "  entries: [bad]"))));

        assertTrue(error.getMessage().contains("motd.entries[0]"));
    }

    @Test
    void reportsPathForWrongShapedMotdEntryField() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "motd:",
                        "  entries:",
                        "    - line1: [bad]",
                        "      line2: \"\""))));

        assertTrue(error.getMessage().contains("motd.entries[0].line1"));
    }

    @Test
    void rejectsConfiguredEmptyMotdEntriesWhenEnabled() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "motd:",
                        "  enabled: true",
                        "  entries: []"))));

        assertTrue(error.getMessage().contains("motd.entries"));
    }

    @Test
    void rejectsConfiguredEmptyTabGroupOrderWhenEnabled() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> PluginConfig.fromMap(
                YamlConfigLoader.parse(List.of(
                        "tab-list:",
                        "  enabled: true",
                        "  group-order: []"))));

        assertTrue(error.getMessage().contains("tab-list.group-order"));
    }
}
