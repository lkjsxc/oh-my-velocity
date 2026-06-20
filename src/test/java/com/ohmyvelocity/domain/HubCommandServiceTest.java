package com.ohmyvelocity.domain;

import com.ohmyvelocity.adapter.config.ConfigManager;
import com.ohmyvelocity.adapter.velocity.hub.HubCommandService;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HubCommandServiceTest {
    @Test
    void plansUnavailableAlreadyConnectedAndConnect() throws Exception {
        HubCommandService service = new HubCommandService(config("""
                hub:
                  target-server: "hub"
                  messages:
                    connecting:
                      en: "go {server}"
                    already-connected:
                      en: "already {server}"
                    unavailable:
                      en: "missing {server}"
                restart:
                  schedule-enabled: false
                """));

        assertEquals(HubCommandAction.UNAVAILABLE, service.plan(Locale.ENGLISH, "survival", false).action());
        assertEquals(HubCommandAction.ALREADY_CONNECTED, service.plan(Locale.ENGLISH, "hub", true).action());
        HubCommandPlan connect = service.plan(Locale.ENGLISH, "survival", true);
        assertEquals(HubCommandAction.CONNECT, connect.action());
        assertTrue(connect.message().contains("hub"));
    }

    @Test
    void disabledHubUsesLocalizedMessage() throws Exception {
        HubCommandService service = new HubCommandService(config("""
                hub:
                  enabled: false
                  messages:
                    disabled:
                      ja: "無効 {server}"
                restart:
                  schedule-enabled: false
                """));

        HubCommandPlan plan = service.plan(Locale.JAPANESE, "survival", true);
        assertEquals(HubCommandAction.DISABLED, plan.action());
        assertEquals("無効 hub", plan.message());
    }

    private static ConfigManager config(String yaml) throws Exception {
        java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("omv-test");
        java.nio.file.Files.writeString(dir.resolve("config.yml"), yaml);
        ConfigManager manager = new ConfigManager(dir);
        manager.reload();
        return manager;
    }
}
