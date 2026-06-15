package com.ohmyvelocity.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlConfigLoaderTest {
    @Test
    void parsesNestedSectionsAndLists() {
        Map<String, Object> root = YamlConfigLoader.parse(List.of(
                "join-messages:",
                "  enabled: true",
                "  to-player: \"<green>Hi\"",
                "restart:",
                "  enabled: false",
                "  warning-minutes:",
                "    - 60",
                "    - 5"));

        PluginConfig config = PluginConfig.fromMap(root);
        assertTrue(config.joinMessages().enabled());
        assertEquals("<green>Hi", config.joinMessages().toPlayer());
        assertEquals(List.of(60, 5), config.restart().warningMinutes());
    }
}
