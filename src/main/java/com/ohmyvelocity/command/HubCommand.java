package com.ohmyvelocity.command;

import com.ohmyvelocity.domain.HubCommandAction;
import com.ohmyvelocity.domain.HubCommandPlan;
import com.ohmyvelocity.domain.LegacyColorTranslator;
import com.ohmyvelocity.adapter.velocity.hub.HubCommandService;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Locale;
import java.util.Optional;

public final class HubCommand implements SimpleCommand {
    private final ProxyServer server;
    private final HubCommandService hubCommand;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public HubCommand(ProxyServer server, HubCommandService hubCommand) {
        this.server = server;
        this.hubCommand = hubCommand;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player player)) {
            source.sendMessage(miniMessage.deserialize(
                    LegacyColorTranslator.toMiniMessage(
                            hubCommand.resultMessage("players-only", Locale.ENGLISH, hubCommand.targetServer()))));
            return;
        }
        String targetName = hubCommand.targetServer();
        Optional<RegisteredServer> target = server.getServer(targetName);
        String current = player.getCurrentServer()
                .map(connection -> connection.getServerInfo().getName())
                .orElse("");
        HubCommandPlan plan = hubCommand.plan(player.getEffectiveLocale(), current, target.isPresent());
        player.sendMessage(miniMessage.deserialize(LegacyColorTranslator.toMiniMessage(plan.message())));
        if (plan.action() == HubCommandAction.CONNECT) {
            connect(player, target.orElseThrow(), plan.targetServer());
        }
    }

    private void connect(Player player, RegisteredServer target, String targetName) {
        player.createConnectionRequest(target).connect().thenAccept(result -> {
            String key = resultKey(result);
            String message = hubCommand.resultMessage(key, player.getEffectiveLocale(), targetName);
            player.sendMessage(miniMessage.deserialize(LegacyColorTranslator.toMiniMessage(message)));
        });
    }

    private static String resultKey(ConnectionRequestBuilder.Result result) {
        if (result.isSuccessful()) {
            return "connected";
        }
        if (result.getStatus() == ConnectionRequestBuilder.Status.ALREADY_CONNECTED) {
            return "already-connected";
        }
        return "failed";
    }
}
