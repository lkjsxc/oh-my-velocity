package com.ohmyvelocity.domain;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaceholderFormatterTest {
    @Test
    void replacesKnownPlaceholders() {
        String result = PlaceholderFormatter.format(
                "Hello {player} ({online}/{max})",
                Map.of("player", "alice", "online", "3", "max", "100"));
        assertEquals("Hello alice (3/100)", result);
    }

    @Test
    void replacesImportedPercentAliases() {
        String result = PlaceholderFormatter.format(
                "[%server%] %player% %ping%",
                Map.of("server", "hub", "player", "alice", "ping", "42"));
        assertEquals("[hub] alice 42", result);
    }

    @Test
    void translatesLegacyColorsToMiniMessage() {
        assertEquals("<gray>[hub]<reset> alice", LegacyColorTranslator.toMiniMessage("§7[hub]§r alice"));
        assertEquals("<green>ok", LegacyColorTranslator.toMiniMessage("&aok"));
    }
}
