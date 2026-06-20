package com.ohmyvelocity.adapter.velocity.message;

import com.ohmyvelocity.adapter.config.ConfigManager;
import com.ohmyvelocity.domain.JoinMessagePlan;
import com.ohmyvelocity.domain.MessageService;
import com.ohmyvelocity.domain.NetworkMessagePlanner;
import com.ohmyvelocity.domain.ProxyMessagesConfig;

import java.util.HashSet;
import java.util.Locale;
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
        boolean firstSeen = seenPlayers.add(playerId);
        return NetworkMessagePlanner.joinPlan(config, playerName, online, max, serverName, locale, firstSeen);
    }

    public JoinMessagePlan leavePlan(String playerName, int online, int max, String serverName, Locale locale) {
        ProxyMessagesConfig config = configManager.config().proxyMessages();
        if (!config.enabled()) {
            return JoinMessagePlan.disabled();
        }
        return NetworkMessagePlanner.leavePlan(config, playerName, online, max, serverName, locale);
    }
}
