package com.ohmyvelocity.plugin;

import com.ohmyvelocity.domain.ConfigManager;
import com.ohmyvelocity.domain.JoinMessageService;
import com.ohmyvelocity.domain.MessageService;
import com.ohmyvelocity.domain.RestartScheduleService;
import com.ohmyvelocity.feature.restart.ShutdownExecutor;

public record Services(
        ConfigManager configManager,
        MessageService messages,
        JoinMessageService joinMessages,
        RestartScheduleService restartSchedule,
        ShutdownExecutor shutdownExecutor) {
}
