package com.ohmyvelocity.domain;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class RestartScheduleServiceTestSupport {
    static RestartScheduleService create(Path tempDir, boolean enabled, List<Integer> warnings) throws Exception {
        Files.writeString(tempDir.resolve("config.yml"), """
                join-messages:
                  enabled: true
                restart:
                  enabled: %s
                  interval-hours: 24
                  random-jitter-minutes: 0
                  warning-minutes:
                %s
                  kick-message: "<red>Bye"
                  mode: graceful_shutdown
                  external-hook: ""
                """.formatted(enabled, warningLines(warnings)));
        ConfigManager configManager = new ConfigManager(tempDir);
        configManager.reload();
        MessageService messages = new MessageService(new MessageCatalog(java.util.Map.of(
                "restart.warning", "<gold>Restart in {minutes}m")));
        return new RestartScheduleService(configManager, messages, new RestartStateStore(tempDir));
    }

    private static String warningLines(List<Integer> warnings) {
        StringBuilder builder = new StringBuilder();
        for (int warning : warnings) {
            builder.append("    - ").append(warning).append('\n');
        }
        return builder.toString();
    }
}
