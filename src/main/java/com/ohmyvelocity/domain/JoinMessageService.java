package com.ohmyvelocity.domain;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class JoinMessageService {
    private final ConfigManager configManager;
    private final MessageService messages;
    private final Set<UUID> seenPlayers = new HashSet<>();

    public JoinMessageService(ConfigManager configManager, MessageService messages) {
        this.configManager = configManager;
        this.messages = messages;
    }

    public JoinMessagePlan plan(UUID playerId, String playerName, int online, int max, String serverName) {
        return joinPlan(playerId, playerName, online, max, serverName, Locale.ENGLISH);
    }

    public JoinMessagePlan joinPlan(
            UUID playerId,
            String playerName,
            int online,
            int max,
            String serverName,
            Locale locale) {
        ProxyMessagesConfig config = configManager.config().proxyMessages();
        if (!config.enabled()) {
            return JoinMessagePlan.disabled();
        }
        if (config.firstJoinOnly() && !seenPlayers.add(playerId)) {
            return JoinMessagePlan.disabled();
        }
        Map<String, String> values = values(playerName, online, max, serverName);
        String toPlayer = resolve(config.join().toPlayer(locale), "join.to-player", values);
        String broadcast = resolve(config.join().broadcast(locale), "join.broadcast", values);
        return new JoinMessagePlan(config.suppressVanilla(), toPlayer, broadcast);
    }

    public JoinMessagePlan leavePlan(String playerName, int online, int max, String serverName, Locale locale) {
        ProxyMessagesConfig config = configManager.config().proxyMessages();
        if (!config.enabled()) {
            return JoinMessagePlan.disabled();
        }
        Map<String, String> values = values(playerName, online, max, serverName);
        String broadcast = PlaceholderFormatter.format(config.leave().broadcast(locale), values);
        return new JoinMessagePlan(false, "", broadcast);
    }

    private String resolve(String configured, String catalogKey, Map<String, String> values) {
        String template = configured == null || configured.isBlank() ? messages.raw(catalogKey) : configured;
        if (template == null || template.isBlank()) {
            return "";
        }
        return PlaceholderFormatter.format(template, values);
    }

    private Map<String, String> values(String playerName, int online, int max, String serverName) {
        return Map.of(
                "player", playerName,
                "online", String.valueOf(online),
                "max", String.valueOf(max),
                "server", serverName == null ? "" : serverName);
    }
}
