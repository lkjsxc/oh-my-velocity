package com.ohmyvelocity.feature.join;

import com.ohmyvelocity.domain.JoinMessagePlan;
import com.ohmyvelocity.adapter.velocity.message.JoinMessageService;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class JoinMessageListener {
    private final ProxyServer server;
    private final JoinMessageService joinMessages;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public JoinMessageListener(ProxyServer server, JoinMessageService joinMessages) {
        this.server = server;
        this.joinMessages = joinMessages;
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        if (event.getPreviousServer().isPresent()) {
            return;
        }
        Player player = event.getPlayer();
        String serverName = event.getServer().getServerInfo().getName();
        JoinMessagePlan plan = joinMessages.joinPlan(
                player.getUniqueId(),
                player.getUsername(),
                server.getPlayerCount(),
                server.getConfiguration().getShowMaxPlayers(),
                serverName,
                player.getEffectiveLocale());
        sendPlan(player, plan);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        String serverName = player.getCurrentServer()
                .map(connection -> connection.getServerInfo().getName())
                .orElse("");
        int onlineAfterLeave = Math.max(0, server.getPlayerCount() - 1);
        JoinMessagePlan plan = joinMessages.leavePlan(
                player.getUsername(),
                onlineAfterLeave,
                server.getConfiguration().getShowMaxPlayers(),
                serverName,
                player.getEffectiveLocale());
        sendPlan(player, plan);
    }

    private void sendPlan(Player player, JoinMessagePlan plan) {
        if (!plan.hasToPlayer() && !plan.hasBroadcast()) {
            return;
        }
        if (plan.hasToPlayer()) {
            player.sendMessage(miniMessage.deserialize(plan.toPlayer()));
        }
        if (plan.hasBroadcast()) {
            server.getAllPlayers().forEach(online -> {
                if (!online.getUniqueId().equals(player.getUniqueId())) {
                    online.sendMessage(miniMessage.deserialize(plan.broadcast()));
                }
            });
        }
    }
}
