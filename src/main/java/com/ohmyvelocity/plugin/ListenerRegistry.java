package com.ohmyvelocity.plugin;

import com.ohmyvelocity.feature.join.JoinMessageListener;
import com.ohmyvelocity.feature.motd.MotdPingListener;
import com.ohmyvelocity.feature.restart.RestartScheduler;
import com.ohmyvelocity.feature.tab.TabListFeature;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;

public final class ListenerRegistry {
    private ListenerRegistry() {
    }

    static void registerAll(
            Object plugin,
            ProxyServer server,
            Services services,
            RestartScheduler scheduler,
            TabListFeature tabListFeature,
            MotdPingListener motdPingListener) {
        server.getEventManager().register(plugin, new JoinMessageListener(server, services.joinMessages()));
        server.getEventManager().register(plugin, motdPingListener);
        server.getEventManager().register(plugin, tabListFeature);
        server.getEventManager().register(plugin, new ShutdownListener(scheduler, tabListFeature));
    }

    static final class ShutdownListener {
        private final RestartScheduler scheduler;
        private final TabListFeature tabListFeature;

        ShutdownListener(RestartScheduler scheduler, TabListFeature tabListFeature) {
            this.scheduler = scheduler;
            this.tabListFeature = tabListFeature;
        }

        @Subscribe
        public void onShutdown(ProxyShutdownEvent event) {
            scheduler.stop();
            tabListFeature.stop();
        }
    }
}
