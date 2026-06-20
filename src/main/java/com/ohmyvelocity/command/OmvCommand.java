package com.ohmyvelocity.command;

import com.ohmyvelocity.domain.MessageService;
import com.ohmyvelocity.feature.motd.MotdPingListener;
import com.ohmyvelocity.feature.restart.RestartScheduler;
import com.ohmyvelocity.feature.tab.TabListFeature;
import com.ohmyvelocity.plugin.Services;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class OmvCommand implements SimpleCommand {
    public static final String PERMISSION = "ohmyvelocity.admin";

    private final Services services;
    private final RestartScheduler restartScheduler;
    private final TabListFeature tabListFeature;
    private final MotdPingListener motdPingListener;
    private final MessageService messages;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public OmvCommand(
            Services services,
            RestartScheduler restartScheduler,
            TabListFeature tabListFeature,
            MotdPingListener motdPingListener) {
        this.services = services;
        this.restartScheduler = restartScheduler;
        this.tabListFeature = tabListFeature;
        this.motdPingListener = motdPingListener;
        this.messages = services.messages();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length == 0) {
            source.sendMessage(miniMessage.deserialize("<gray>Usage: /omv <reload|restart>"));
            return;
        }
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "reload" -> handleReload(source);
            case "restart" -> handleRestart(source, args);
            default -> source.sendMessage(miniMessage.deserialize("<red>Unknown subcommand."));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(PERMISSION);
    }

    private void handleReload(CommandSource source) {
        try {
            services.configManager().reload();
            restartScheduler.reload();
            tabListFeature.reload();
            motdPingListener.clearIconCache();
            source.sendMessage(miniMessage.deserialize(messages.raw("command.reload.ok")));
        } catch (IOException | IllegalArgumentException ex) {
            source.sendMessage(miniMessage.deserialize("<red>Reload failed: " + ex.getMessage()));
        }
    }

    private void handleRestart(CommandSource source, String[] args) {
        if (args.length < 2) {
            source.sendMessage(miniMessage.deserialize("<gray>Usage: /omv restart <now|status>"));
            return;
        }
        switch (args[1].toLowerCase(Locale.ROOT)) {
            case "now" -> restartScheduler.restartNow();
            case "status" -> sendStatus(source);
            default -> source.sendMessage(miniMessage.deserialize("<red>Unknown restart subcommand."));
        }
    }

    private void sendStatus(CommandSource source) {
        if (!restartScheduler.enabled()) {
            source.sendMessage(miniMessage.deserialize(messages.raw("command.restart.none")));
            return;
        }
        String formatted = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
                Instant.ofEpochMilli(restartScheduler.nextRestartEpochMs()).atZone(ZoneId.systemDefault()));
        String text = messages.format("command.restart.status", java.util.Map.of("time", formatted));
        source.sendMessage(miniMessage.deserialize(text));
    }
}
