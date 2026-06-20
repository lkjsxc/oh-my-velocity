package com.ohmyvelocity.domain;

import java.util.Locale;
import java.util.Map;

public final class NetworkMessagePlanner {
    private NetworkMessagePlanner() {
    }

    public static JoinMessagePlan joinPlan(
            ProxyMessagesConfig config,
            String playerName,
            int online,
            int max,
            String serverName,
            Locale locale,
            boolean firstSeen) {
        if (!config.enabled()) {
            return JoinMessagePlan.disabled();
        }
        Map<String, String> values = values(playerName, online, max, serverName);
        String toPlayer = config.firstJoinOnly() && !firstSeen ? "" : render(config.join().toPlayer(locale), values);
        return new JoinMessagePlan(
                toPlayer,
                render(config.join().broadcast(locale), values));
    }

    public static JoinMessagePlan leavePlan(
            ProxyMessagesConfig config,
            String playerName,
            int online,
            int max,
            String serverName,
            Locale locale) {
        if (!config.enabled()) {
            return JoinMessagePlan.disabled();
        }
        return new JoinMessagePlan(
                "",
                render(config.leave().broadcast(locale), values(playerName, online, max, serverName)));
    }

    private static String render(String template, Map<String, String> values) {
        if (template == null || template.isBlank()) {
            return "";
        }
        return PlaceholderFormatter.format(template, values);
    }

    private static Map<String, String> values(String playerName, int online, int max, String serverName) {
        return Map.of(
                "player", playerName,
                "online", String.valueOf(online),
                "max", String.valueOf(max),
                "server", serverName == null || serverName.isBlank() ? "proxy" : serverName);
    }
}
