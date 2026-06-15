package com.ohmyvelocity.plugin;

import com.ohmyvelocity.command.OmvCommand;
import com.ohmyvelocity.feature.restart.RestartScheduler;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;

public final class CommandRegistry {
    private CommandRegistry() {
    }

    static void registerAll(Object plugin, ProxyServer server, Services services, RestartScheduler scheduler) {
        CommandMeta meta = server.getCommandManager().metaBuilder("omv")
                .plugin(plugin)
                .build();
        server.getCommandManager().register(meta, new OmvCommand(services, scheduler));
    }
}
