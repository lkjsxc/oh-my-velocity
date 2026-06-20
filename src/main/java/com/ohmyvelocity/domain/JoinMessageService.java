package com.ohmyvelocity.domain;

import com.ohmyvelocity.adapter.config.ConfigManager;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class JoinMessageService {
    private final ConfigManager configManager;
    private final Set<UUID> seenPlayers = new HashSet<>();

    public JoinMessageService(ConfigManager configManager, MessageService messages) {
        this.configManager = configManager;
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
        String toPlayer = render(config.join().toPlayer(locale), values);
        String broadcast = render(config.join().broadcast(locale), values);
        return new JoinMessagePlan(toPlayer, broadcast);
    }

    public JoinMessagePlan leavePlan(String playerName, int online, int max, String serverName, Locale locale) {
        ProxyMessagesConfig config = configManager.config().proxyMessages();
        if (!config.enabled()) {
            return JoinMessagePlan.disabled();
        }
        Map<String, String> values = values(playerName, online, max, serverName);
        String broadcast = render(config.leave().broadcast(locale), values);
        return new JoinMessagePlan("", broadcast);
    }

    private String render(String template, Map<String, String> values) {
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
