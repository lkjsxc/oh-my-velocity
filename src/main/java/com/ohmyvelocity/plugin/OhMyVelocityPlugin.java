package com.ohmyvelocity.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.ohmyvelocity.feature.restart.RestartScheduler;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "ohmyvelocity",
        name = "oh-my-velocity",
        version = "0.1.0",
        authors = {"lkj"})
public final class OhMyVelocityPlugin {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private Services services;
    private RestartScheduler restartScheduler;

    @Inject
    public OhMyVelocityPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        try {
            Services bootstrapped = ServiceBootstrap.bootstrap(dataDirectory);
            this.restartScheduler = new RestartScheduler(
                    this,
                    server,
                    logger,
                    bootstrapped.configManager(),
                    bootstrapped.restartSchedule(),
                    bootstrapped.messages(),
                    bootstrapped.shutdownExecutor());
            this.services = new Services(
                    bootstrapped.configManager(),
                    bootstrapped.messages(),
                    bootstrapped.joinMessages(),
                    bootstrapped.restartSchedule(),
                    bootstrapped.shutdownExecutor());
            ListenerRegistry.registerAll(this, server, services, restartScheduler);
            CommandRegistry.registerAll(this, server, services, restartScheduler);
            restartScheduler.start();
            logger.info("oh-my-velocity enabled");
        } catch (Exception ex) {
            logger.error("Failed to enable oh-my-velocity", ex);
        }
    }
}
