package com.ohmyvelocity.domain;

import com.ohmyvelocity.adapter.config.ConfigManager;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MotdServiceTest {
    @Test
    void rendersPlaceholdersAndHoverSamples() throws Exception {
        ConfigManager manager = config("""
                motd:
                  enabled: true
                  max-players: 128
                  samples:
                    - "{online}/{max}"
                  entries:
                    - line1: "Online {online}"
                      line2: "Max {max}"
                      weight: 1
                restart:
                  schedule-enabled: false
                """);
        Optional<MotdPlan> plan = new MotdService().plan(manager.config().motd(), 7, "lkjsxc.com", new Random(0));

        assertTrue(plan.isPresent());
        assertEquals("Online 7\nMax 128", plan.get().description());
        assertEquals("7/128", plan.get().samplePlayers().getFirst());
    }

    @Test
    void skipsUnmatchedVirtualHosts() throws Exception {
        ConfigManager manager = config("""
                motd:
                  enabled: true
                  target-hosts:
                    - "play.example.net"
                restart:
                  schedule-enabled: false
                """);

        assertTrue(new MotdService().plan(manager.config().motd(), 1, "other.example.net", new Random(0)).isEmpty());
    }

    private static ConfigManager config(String yaml) throws Exception {
        java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("omv-test");
        java.nio.file.Files.writeString(dir.resolve("config.yml"), yaml);
        ConfigManager manager = new ConfigManager(dir);
        manager.reload();
        return manager;
    }
}
