package com.ohmyvelocity.plugin;

import com.ohmyvelocity.adapter.config.ConfigManager;
import com.ohmyvelocity.adapter.runtime.RestartScheduleService;
import com.ohmyvelocity.adapter.velocity.hub.HubCommandService;
import com.ohmyvelocity.adapter.velocity.message.JoinMessageService;
import com.ohmyvelocity.domain.MessageService;
import com.ohmyvelocity.domain.MotdService;
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
