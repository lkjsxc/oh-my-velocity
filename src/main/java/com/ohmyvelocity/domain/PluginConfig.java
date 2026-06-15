package com.ohmyvelocity.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PluginConfig {
    private final JoinMessagesConfig joinMessages;
    private final RestartConfig restart;

    public PluginConfig(JoinMessagesConfig joinMessages, RestartConfig restart) {
        this.joinMessages = joinMessages;
        this.restart = restart;
    }

    public JoinMessagesConfig joinMessages() {
        return joinMessages;
    }

    public RestartConfig restart() {
        return restart;
    }

    public static PluginConfig fromMap(Map<String, Object> root) {
        Map<String, Object> join = section(root, "join-messages");
        Map<String, Object> restart = section(root, "restart");
        return new PluginConfig(
                new JoinMessagesConfig(
                        bool(join, "enabled", true),
                        text(join, "to-player", ""),
                        text(join, "broadcast", ""),
                        bool(join, "first-join-only", false),
                        bool(join, "suppress-vanilla", false)),
                new RestartConfig(
                        bool(restart, "enabled", true),
                        integer(restart, "interval-hours", 24),
                        integer(restart, "random-jitter-minutes", 10),
                        integerList(restart, "warning-minutes", List.of(60, 15, 5, 1)),
                        text(restart, "kick-message", ""),
                        text(restart, "mode", "graceful_shutdown"),
                        text(restart, "external-hook", "")));
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> section(Map<String, Object> root, String key) {
        Object value = root.get(key);
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return Map.of();
    }

    private static boolean bool(Map<String, Object> map, String key, boolean fallback) {
        Object value = map.get(key);
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value instanceof String text) {
            return Boolean.parseBoolean(text);
        }
        return fallback;
    }

    private static int integer(Map<String, Object> map, String key, int fallback) {
        Object value = map.get(key);
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text) {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException ignored) {
                return fallback;
            }
        }
        return fallback;
    }

    private static String text(Map<String, Object> map, String key, String fallback) {
        Object value = map.get(key);
        return value == null ? fallback : String.valueOf(value);
    }

    @SuppressWarnings("unchecked")
    private static List<Integer> integerList(Map<String, Object> map, String key, List<Integer> fallback) {
        Object value = map.get(key);
        if (!(value instanceof List<?> list)) {
            return fallback;
        }
        List<Integer> parsed = list.stream().map(item -> {
            if (item instanceof Number number) {
                return number.intValue();
            }
            return Integer.parseInt(String.valueOf(item));
        }).toList();
        return parsed.isEmpty() ? fallback : List.copyOf(parsed);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> join = new LinkedHashMap<>();
        join.put("enabled", joinMessages.enabled());
        join.put("to-player", joinMessages.toPlayer());
        join.put("broadcast", joinMessages.broadcast());
        join.put("first-join-only", joinMessages.firstJoinOnly());
        join.put("suppress-vanilla", joinMessages.suppressVanilla());

        Map<String, Object> restartMap = new LinkedHashMap<>();
        restartMap.put("enabled", restart.enabled());
        restartMap.put("interval-hours", restart.intervalHours());
        restartMap.put("random-jitter-minutes", restart.randomJitterMinutes());
        restartMap.put("warning-minutes", restart.warningMinutes());
        restartMap.put("kick-message", restart.kickMessage());
        restartMap.put("mode", restart.mode());
        restartMap.put("external-hook", restart.externalHook());

        Map<String, Object> root = new LinkedHashMap<>();
        root.put("join-messages", join);
        root.put("restart", restartMap);
        return Collections.unmodifiableMap(root);
    }
}
