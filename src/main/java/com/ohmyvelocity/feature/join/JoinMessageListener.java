package com.ohmyvelocity.feature.join;

import com.ohmyvelocity.domain.JoinMessagePlan;
import com.ohmyvelocity.domain.JoinMessageService;
import com.velocitypowered.api.event.Subscribe;
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
        Player player = event.getPlayer();
        String serverName = event.getServer().getServerInfo().getName();
        JoinMessagePlan plan = joinMessages.plan(
                player.getUniqueId(),
                player.getUsername(),
                server.getPlayerCount(),
                server.getConfiguration().getShowMaxPlayers(),
                serverName);
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
