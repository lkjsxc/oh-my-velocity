package com.ohmyvelocity.domain;

import java.util.Locale;
import java.util.Map;

public final class HubPlanner {
    private HubPlanner() {
    }

    public static HubCommandPlan plan(
            HubCommandConfig config,
            Locale locale,
            String currentServer,
            boolean targetAvailable) {
        String target = config.targetServer();
        if (!config.enabled()) {
            return result(config, HubCommandAction.DISABLED, "disabled", locale, target);
        }
        if (!targetAvailable) {
            return result(config, HubCommandAction.UNAVAILABLE, "unavailable", locale, target);
        }
        if (target.equalsIgnoreCase(currentServer == null ? "" : currentServer)) {
            return result(config, HubCommandAction.ALREADY_CONNECTED, "already-connected", locale, target);
        }
        return result(config, HubCommandAction.CONNECT, "connecting", locale, target);
    }

    public static String message(HubCommandConfig config, String key, Locale locale, String targetServer) {
        String template = config.messages().resolve(key, locale);
        return PlaceholderFormatter.format(template, Map.of("server", targetServer));
    }

    private static HubCommandPlan result(
            HubCommandConfig config,
            HubCommandAction action,
            String key,
            Locale locale,
            String target) {
        return new HubCommandPlan(action, target, message(config, key, locale, target));
    }
}
