package com.ohmyvelocity.domain;

import com.ohmyvelocity.adapter.config.ConfigManager;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MotdServiceTest {
    @Test
    void rendersPlaceholdersAndPlayerNameHover() throws Exception {
        ConfigManager manager = config("""
                motd:
                  enabled: true
                  max-players: 128
                  entries:
                    - line1: "Online {online}"
                      line2: "Max {max}"
                      weight: 1
                restart:
                  schedule-enabled: false
                """);
        Optional<MotdPlan> plan = new MotdService().plan(
                manager.config().motd(),
                7,
                "lkjsxc.com",
                new Random(0),
                List.of("Alice", "Bob"));

        assertTrue(plan.isPresent());
        assertEquals("Online 7\nMax 128", plan.get().description());
        assertEquals(List.of("Alice", "Bob"), plan.get().samplePlayers());
    }

    @Test
    void addsRemainderLineAfterFifteenHoverNames() {
        MotdConfig config = new MotdConfig(
                true,
                List.of(new MotdEntry("Online", "", 1, "")),
                128,
                false,
                List.of("*"));
        List<String> names = IntStream.rangeClosed(1, 18)
                .mapToObj(index -> "Player" + index)
                .toList();
        MotdPlan plan = new MotdService().plan(config, 18, "", new Random(0), names).orElseThrow();

        assertEquals(16, plan.samplePlayers().size());
        assertEquals("Player1", plan.samplePlayers().getFirst());
        assertEquals("Player15", plan.samplePlayers().get(14));
        assertEquals("and 3 more", plan.samplePlayers().getLast());
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
