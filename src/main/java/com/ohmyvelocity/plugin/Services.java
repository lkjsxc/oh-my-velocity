package com.ohmyvelocity.plugin;

import com.ohmyvelocity.domain.ConfigManager;
import com.ohmyvelocity.domain.HubCommandService;
import com.ohmyvelocity.domain.JoinMessageService;
import com.ohmyvelocity.domain.MessageService;
import com.ohmyvelocity.domain.MotdService;
import com.ohmyvelocity.domain.RestartScheduleService;
import com.ohmyvelocity.domain.TabRenderService;
import com.ohmyvelocity.feature.restart.ShutdownExecutor;

public record Services(
        ConfigManager configManager,
        MessageService messages,
        JoinMessageService joinMessages,
        MotdService motd,
        TabRenderService tabRender,
        HubCommandService hubCommand,
        RestartScheduleService restartSchedule,
        ShutdownExecutor shutdownExecutor) {
}
