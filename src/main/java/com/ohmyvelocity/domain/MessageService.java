package com.ohmyvelocity.domain;

public final class MessageService {
    private static final java.util.Map<String, String> DEFAULTS = java.util.Map.of(
            "command.reload.ok", "<green>oh-my-velocity config reloaded.",
            "command.restart.status", "<gray>Next restart: <white>{time}</white>",
            "command.restart.none", "<gray>Scheduled restart is disabled.",
            "command.no-permission", "<red>You do not have permission.");

    public MessageService() {
    }

    public String raw(String key) {
        return DEFAULTS.getOrDefault(key, key);
    }

    public String format(String key, java.util.Map<String, String> placeholders) {
        return PlaceholderFormatter.format(raw(key), placeholders);
    }
}
