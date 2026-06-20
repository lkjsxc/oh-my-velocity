package com.ohmyvelocity.plugin;

import com.ohmyvelocity.command.HubCommand;
import com.ohmyvelocity.command.OmvCommand;
import com.ohmyvelocity.feature.motd.MotdPingListener;
import com.ohmyvelocity.feature.restart.RestartScheduler;
import com.ohmyvelocity.feature.tab.TabListFeature;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;

public final class CommandRegistry {
    private CommandRegistry() {
    }

    static void registerAll(
            Object plugin,
            ProxyServer server,
            Services services,
            RestartScheduler scheduler,
            TabListFeature tabListFeature,
            MotdPingListener motdPingListener) {
        CommandMeta meta = server.getCommandManager().metaBuilder("omv")
                .plugin(plugin)
                .build();
        server.getCommandManager().register(
                meta,
                new OmvCommand(services, scheduler, tabListFeature, motdPingListener));
        CommandMeta hub = server.getCommandManager().metaBuilder("hub")
                .plugin(plugin)
                .build();
        server.getCommandManager().register(hub, new HubCommand(server, services.hubCommand()));
    }
}
