package com.ohmyvelocity.domain;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JoinMessageServiceTest {
    @Test
    void firstJoinOnlySkipsRepeatPlayers() throws Exception {
        ConfigManager configManager = configWithFirstJoinOnly();
        JoinMessageService service = new JoinMessageService(configManager, new MessageService(MessageCatalog.load(null)));
        UUID id = UUID.randomUUID();

        assertTrue(service.plan(id, "bob", 1, 100, "lobby").hasToPlayer());
        assertFalse(service.plan(id, "bob", 2, 100, "lobby").hasToPlayer());
    }

    @Test
    void localeFallbackAndLeaveMessagesRender() throws Exception {
        ConfigManager configManager = configWithProxyMessages();
        JoinMessageService service = new JoinMessageService(configManager, new MessageService(MessageCatalog.load(null)));

        JoinMessagePlan join = service.joinPlan(
                UUID.randomUUID(),
                "aki",
                3,
                128,
                "hub",
                Locale.JAPANESE);
        JoinMessagePlan leave = service.leavePlan("aki", 2, 128, "hub", Locale.JAPANESE);

        assertTrue(join.toPlayer().contains("ようこそ"));
        assertTrue(join.broadcast().contains("hub"));
        assertTrue(leave.broadcast().contains("退出"));
    }

    private static ConfigManager configWithFirstJoinOnly() throws Exception {
        java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("omv-test");
        java.nio.file.Files.writeString(dir.resolve("config.yml"), """
                join-messages:
                  enabled: true
                  to-player: "Hello {player}"
                  broadcast: ""
                  first-join-only: true
                  suppress-vanilla: false
                restart:
                  enabled: false
                  interval-hours: 24
                  random-jitter-minutes: 0
                  warning-minutes:
                    - 5
                  kick-message: ""
                  mode: graceful_shutdown
                  external-hook: ""
                """);
        ConfigManager manager = new ConfigManager(dir);
        manager.reload();
        return manager;
    }

    private static ConfigManager configWithProxyMessages() throws Exception {
        java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("omv-test");
        java.nio.file.Files.writeString(dir.resolve("config.yml"), """
                proxy-messages:
                  enabled: true
                  join:
                    to-player:
                      en: "Welcome {player}"
                      ja: "ようこそ {player}"
                    broadcast:
                      en: "{player} joined {server}"
                  leave:
                    broadcast:
                      ja: "{player} 退出 {server}"
                restart:
                  enabled: false
                """);
        ConfigManager manager = new ConfigManager(dir);
        manager.reload();
        return manager;
    }
}
