package com.ohmyvelocity.domain;

import java.util.Locale;
import java.util.Map;

public final class HubCommandService {
    private final ConfigManager configManager;

    public HubCommandService(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public HubCommandPlan plan(Locale locale, String currentServer, boolean targetAvailable) {
        HubCommandConfig config = configManager.config().hubCommand();
        String target = config.targetServer();
        if (!config.enabled()) {
            return new HubCommandPlan(HubCommandAction.DISABLED, target, message("disabled", locale, target));
        }
        if (!targetAvailable) {
            return new HubCommandPlan(HubCommandAction.UNAVAILABLE, target, message("unavailable", locale, target));
        }
        if (target.equalsIgnoreCase(currentServer == null ? "" : currentServer)) {
            return new HubCommandPlan(HubCommandAction.ALREADY_CONNECTED, target, message("already-connected", locale, target));
        }
        return new HubCommandPlan(HubCommandAction.CONNECT, target, message("connecting", locale, target));
    }

    public String targetServer() {
        return configManager.config().hubCommand().targetServer();
    }

    public String resultMessage(String key, Locale locale, String targetServer) {
        return message(key, locale, targetServer);
    }

    private String message(String key, Locale locale, String targetServer) {
        String template = configManager.config().hubCommand().messages().resolve(key, locale);
        return PlaceholderFormatter.format(template, Map.of("server", targetServer));
    }
}
