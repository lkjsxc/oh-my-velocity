package com.ohmyvelocity.adapter.velocity.hub;

import com.ohmyvelocity.adapter.config.ConfigManager;
import com.ohmyvelocity.domain.HubCommandConfig;
import com.ohmyvelocity.domain.HubCommandPlan;
import com.ohmyvelocity.domain.HubPlanner;

import java.util.Locale;

public final class HubCommandService {
    private final ConfigManager configManager;

    public HubCommandService(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public HubCommandPlan plan(Locale locale, String currentServer, boolean targetAvailable) {
        HubCommandConfig config = configManager.config().hubCommand();
        return HubPlanner.plan(config, locale, currentServer, targetAvailable);
    }

    public String targetServer() {
        return configManager.config().hubCommand().targetServer();
    }

    public String resultMessage(String key, Locale locale, String targetServer) {
        return HubPlanner.message(configManager.config().hubCommand(), key, locale, targetServer);
    }
}
