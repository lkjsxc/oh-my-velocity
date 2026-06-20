package com.ohmyvelocity.domain;

import com.ohmyvelocity.adapter.config.ConfigManager;
import com.ohmyvelocity.adapter.runtime.RestartScheduleService;
import com.ohmyvelocity.adapter.runtime.RestartStateStore;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class RestartScheduleServiceTestSupport {
    static RestartScheduleService create(Path tempDir, boolean enabled, List<Integer> warnings) throws Exception {
        Files.writeString(tempDir.resolve("config.yml"), """
                messages:
                  enabled: true
                restart:
                  schedule-enabled: %s
                  manual-command-enabled: true
                  interval-hours: 24
                  random-jitter-minutes: 0
                  warning-minutes:
                %s
                  warning-message:
                    en: "<gold>Restart in {minutes}m"
                  kick-message:
                    en: "<red>Bye"
                  mode: graceful_shutdown
                  external-hook: ""
                  external-hook-timeout-seconds: 30
                """.formatted(enabled, warningLines(warnings)));
        ConfigManager configManager = new ConfigManager(tempDir);
        configManager.reload();
        return new RestartScheduleService(configManager, new MessageService(), new RestartStateStore(tempDir));
    }

    private static String warningLines(List<Integer> warnings) {
        StringBuilder builder = new StringBuilder();
        for (int warning : warnings) {
            builder.append("    - ").append(warning).append('\n');
        }
        return builder.toString();
    }
}
