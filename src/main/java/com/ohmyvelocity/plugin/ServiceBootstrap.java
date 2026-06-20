package com.ohmyvelocity.plugin;

import com.ohmyvelocity.adapter.config.ConfigManager;
import com.ohmyvelocity.adapter.runtime.RestartScheduleService;
import com.ohmyvelocity.adapter.runtime.RestartStateStore;
import com.ohmyvelocity.domain.HubCommandService;
import com.ohmyvelocity.domain.JoinMessageService;
import com.ohmyvelocity.domain.MessageService;
import com.ohmyvelocity.domain.MotdService;
import com.ohmyvelocity.domain.TabRenderService;
import com.ohmyvelocity.feature.restart.RestartScheduler;
import com.ohmyvelocity.feature.restart.ShutdownExecutor;

import java.io.IOException;
import java.nio.file.Path;

public final class ServiceBootstrap {
    private ServiceBootstrap() {
    }

    static Services bootstrap(Path dataDirectory) throws IOException {
        ConfigManager configManager = new ConfigManager(dataDirectory);
        configManager.ensureDefault();

        MessageService messages = new MessageService();
        JoinMessageService joinMessages = new JoinMessageService(configManager, messages);
        MotdService motd = new MotdService(configManager);
        TabRenderService tabRender = new TabRenderService();
        HubCommandService hubCommand = new HubCommandService(configManager);
        RestartStateStore stateStore = new RestartStateStore(dataDirectory);
        RestartScheduleService restartSchedule = new RestartScheduleService(configManager, messages, stateStore);
        ShutdownExecutor shutdownExecutor = new ShutdownExecutor();

        return new Services(
                configManager,
                messages,
                joinMessages,
                motd,
                tabRender,
                hubCommand,
                restartSchedule,
                shutdownExecutor);
    }

}
