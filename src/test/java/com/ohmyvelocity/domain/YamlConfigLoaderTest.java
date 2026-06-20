package com.ohmyvelocity.domain;

import com.ohmyvelocity.adapter.config.YamlConfigLoader;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlConfigLoaderTest {
    @Test
    void parsesNestedSectionsAndLists() {
        Map<String, Object> root = YamlConfigLoader.parse(List.of(
                "messages:",
                "  enabled: true",
                "  join:",
                "    to-player:",
                "      en: \"<green>Hi\"",
                "      ja: \"<green>やあ\"",
                "motd:",
                "  enabled: true",
                "  entries:",
                "    - line1: \"One\"",
                "      line2: \"Two\"",
                "      weight: 2",
                "restart:",
                "  schedule-enabled: false",
                "  warning-minutes:",
                "    - 60",
                "    - 5"));

        PluginConfig config = PluginConfig.fromMap(root);
        assertTrue(config.proxyMessages().enabled());
        assertEquals("<green>やあ", config.proxyMessages().join().toPlayer(java.util.Locale.JAPANESE));
        assertEquals("One", config.motd().entries().getFirst().line1());
        assertEquals(2, config.motd().entries().getFirst().weight());
        assertEquals(List.of(60, 5), config.restart().warningMinutes());
    }
}
