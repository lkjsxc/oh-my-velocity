package com.ohmyvelocity.plugin;

import com.ohmyvelocity.feature.join.JoinMessageListener;
import com.ohmyvelocity.feature.restart.RestartScheduler;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;

public final class ListenerRegistry {
    private ListenerRegistry() {
    }

    static void registerAll(Object plugin, ProxyServer server, Services services, RestartScheduler scheduler) {
        server.getEventManager().register(plugin, new JoinMessageListener(server, services.joinMessages()));
        server.getEventManager().register(plugin, new ShutdownListener(scheduler));
    }

    static final class ShutdownListener {
        private final RestartScheduler scheduler;

        ShutdownListener(RestartScheduler scheduler) {
            this.scheduler = scheduler;
        }

        @Subscribe
        public void onShutdown(ProxyShutdownEvent event) {
            scheduler.stop();
        }
    }
}
