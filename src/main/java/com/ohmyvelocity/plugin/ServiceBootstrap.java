package com.ohmyvelocity.plugin;

import com.ohmyvelocity.domain.ConfigManager;
import com.ohmyvelocity.domain.JoinMessageService;
import com.ohmyvelocity.domain.MessageCatalog;
import com.ohmyvelocity.domain.MessageService;
import com.ohmyvelocity.domain.RestartScheduleService;
import com.ohmyvelocity.domain.RestartStateStore;
import com.ohmyvelocity.feature.restart.RestartScheduler;
import com.ohmyvelocity.feature.restart.ShutdownExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public final class ServiceBootstrap {
    private ServiceBootstrap() {
    }

    static Services bootstrap(Path dataDirectory) throws IOException {
        ConfigManager configManager = new ConfigManager(dataDirectory);
        configManager.ensureDefault();

        MessageCatalog catalog = MessageCatalog.load(resourceStream("lang/en.json"));
        MessageService messages = new MessageService(catalog);
        JoinMessageService joinMessages = new JoinMessageService(configManager, messages);
        RestartStateStore stateStore = new RestartStateStore(dataDirectory);
        RestartScheduleService restartSchedule = new RestartScheduleService(configManager, messages, stateStore);
        ShutdownExecutor shutdownExecutor = new ShutdownExecutor();

        return new Services(configManager, messages, joinMessages, restartSchedule, shutdownExecutor);
    }

    private static InputStream resourceStream(String path) {
        return ServiceBootstrap.class.getClassLoader().getResourceAsStream(path);
    }
}
