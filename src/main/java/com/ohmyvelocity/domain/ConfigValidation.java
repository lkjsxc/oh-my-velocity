package com.ohmyvelocity.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ConfigValidation {
    private ConfigValidation() {
    }

    public static PluginConfig validate(PluginConfig config) {
        messages(config.proxyMessages());
        motd(config.motd());
        tab(config.tab());
        hub(config.hubCommand());
        restart(config.restart());
        return config;
    }

    private static void messages(ProxyMessagesConfig config) {
        if (!config.enabled()) {
            return;
        }
        requireLocales("messages.join.to-player", config.join().toPlayer());
        requireLocales("messages.join.broadcast", config.join().broadcast());
        requireLocales("messages.leave.broadcast", config.leave().broadcast());
    }

    private static void motd(MotdConfig config) {
        if (!config.enabled()) {
            return;
        }
        require(config.maxPlayers() > 0, "motd.max-players must be positive");
        require(!config.entries().isEmpty(), "motd.entries must not be empty");
        for (int index = 0; index < config.entries().size(); index++) {
            MotdEntry entry = config.entries().get(index);
            require(entry.weight() > 0, "motd.entries[" + index + "].weight must be positive");
        }
    }

    private static void tab(TabConfig config) {
        if (!config.enabled()) {
            return;
        }
        require(config.refreshIntervalSeconds() > 0, "tab-list.refresh-interval-seconds must be positive");
        require(!config.groupOrder().isEmpty(), "tab-list.group-order must not be empty");
    }

    private static void hub(HubCommandConfig config) {
        if (config.enabled()) {
            require(!config.targetServer().isBlank(), "hub.target-server must not be empty when hub is enabled");
        }
        for (String key : config.messages().messages().keySet()) {
            requireLocales("hub.messages." + key, config.messages().messages().get(key));
        }
    }

    private static void restart(RestartConfig config) {
        require(config.intervalHours() > 0, "restart.interval-hours must be positive");
        require(config.randomJitterMinutes() >= 0, "restart.random-jitter-minutes must be non-negative");
        require(config.externalHookTimeoutSeconds() > 0, "restart.external-hook-timeout-seconds must be positive");
        Set<Integer> seen = new HashSet<>();
        for (int warning : config.warningMinutes()) {
            require(warning > 0, "restart.warning-minutes must be positive");
            require(seen.add(warning), "restart.warning-minutes must be unique");
        }
        requireLocales("restart.warning-message", config.warningMessage().messages().get("warning"));
        requireLocales("restart.kick-message", config.kickMessage().messages().get("kick"));
    }

    private static void requireLocales(String path, Map<String, String> values) {
        require(values != null && !values.isEmpty(), path + " must define at least one locale");
        values.forEach((locale, text) -> require(text != null, path + "." + locale + " must not be null"));
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
