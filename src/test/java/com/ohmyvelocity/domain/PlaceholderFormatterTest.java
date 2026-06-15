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
}
