package com.ohmyvelocity.feature.tab;

import com.ohmyvelocity.domain.TabConfig;
import com.velocitypowered.api.TextHolder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.scoreboard.CollisionRule;
import com.velocitypowered.api.scoreboard.DisplaySlot;
import com.velocitypowered.api.scoreboard.HealthDisplay;
import com.velocitypowered.api.scoreboard.NameVisibility;
import com.velocitypowered.api.scoreboard.ProxyObjective;
import com.velocitypowered.api.scoreboard.ProxyScoreboard;
import com.velocitypowered.api.scoreboard.ScoreboardManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

final class TabScoreboardBridge {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    void update(Player viewer, Collection<Player> players, TabConfig config, Function<Player, String> groupResolver) {
        ScoreboardManager manager = ScoreboardManager.getInstance();
        if (manager == null) {
            return;
        }
        ProxyScoreboard board = manager.getProxyScoreboard(viewer);
        updateObjective(board, players, config);
        updateTeams(board, players, config, groupResolver);
    }

    private void updateObjective(ProxyScoreboard board, Collection<Player> players, TabConfig config) {
        String name = config.pingObjective().name();
        ProxyObjective objective = board.getObjective(name);
        if (!config.pingObjective().enabled()) {
            if (objective != null) {
                board.unregisterObjective(name);
            }
            return;
        }
        if (objective == null) {
            objective = board.registerObjective(board.objectiveBuilder(name)
                    .title(TextHolder.of(component(config.pingObjective().title())))
                    .healthDisplay(HealthDisplay.INTEGER)
                    .displaySlot(DisplaySlot.PLAYER_LIST));
        } else {
            objective.setTitle(TextHolder.of(component(config.pingObjective().title())));
            objective.setHealthDisplay(HealthDisplay.INTEGER);
            objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }
        for (Player player : players) {
            int ping = (int) Math.min(Integer.MAX_VALUE, player.getPing());
            objective.setScore(player.getUsername(), score -> score.score(ping));
        }
    }

    private void updateTeams(
            ProxyScoreboard board,
            Collection<Player> players,
            TabConfig config,
            Function<Player, String> groupResolver) {
        board.getTeams().stream()
                .filter(team -> team.getName().startsWith("omv_"))
                .map(team -> team.getName())
                .toList()
                .forEach(board::unregisterTeam);
        Map<String, List<String>> grouped = new LinkedHashMap<>();
        players.forEach(player -> grouped
                .computeIfAbsent(groupResolver.apply(player), ignored -> new ArrayList<>())
                .add(player.getUsername()));
        grouped.forEach((group, entries) -> board.registerTeam(board.teamBuilder(teamName(config, group))
                .displayName(TextHolder.of(group))
                .prefix(TextHolder.empty())
                .suffix(TextHolder.empty())
                .nameVisibility(nameVisibility(config))
                .collisionRule(collisionRule(config))
                .entries(entries)));
    }

    private Component component(String text) {
        return text == null || text.isBlank() ? Component.empty() : miniMessage.deserialize(text);
    }

    private static String teamName(TabConfig config, String group) {
        int index = config.groupOrder().indexOf(group);
        String safe = group.replaceAll("[^a-z0-9_]", "");
        String name = "omv_" + Math.max(0, index) + "_" + safe;
        return name.length() <= 16 ? name : name.substring(0, 16);
    }

    private static CollisionRule collisionRule(TabConfig config) {
        CollisionRule rule = CollisionRule.getByName(config.collisionRule());
        return rule == null ? CollisionRule.NEVER : rule;
    }

    private static NameVisibility nameVisibility(TabConfig config) {
        NameVisibility visibility = NameVisibility.getByName(config.nameVisibility());
        return visibility == null ? NameVisibility.ALWAYS : visibility;
    }
}
