package com.ohmyvelocity.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.ohmyvelocity.feature.motd.MotdPingListener;
import com.ohmyvelocity.feature.restart.RestartScheduler;
import com.ohmyvelocity.feature.tab.TabListFeature;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "ohmyvelocity",
        name = "oh-my-velocity",
        version = OhMyVelocityPlugin.VERSION,
        authors = {"lkj"},
        dependencies = {})
public final class OhMyVelocityPlugin {
    public static final String VERSION = "0.1.0";

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private Services services;
    private RestartScheduler restartScheduler;
    private TabListFeature tabListFeature;
    private MotdPingListener motdPingListener;

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
                    bootstrapped.motd(),
                    bootstrapped.tabRender(),
                    bootstrapped.hubCommand(),
                    bootstrapped.restartSchedule(),
                    bootstrapped.shutdownExecutor());
            this.tabListFeature = new TabListFeature(this, server, services.configManager(), services.tabRender());
            this.motdPingListener = new MotdPingListener(
                    server,
                    services.configManager(),
                    services.motd(),
                    dataDirectory,
                    logger);
            ListenerRegistry.registerAll(this, server, services, restartScheduler, tabListFeature, motdPingListener);
            CommandRegistry.registerAll(this, server, services, restartScheduler, tabListFeature, motdPingListener);
            restartScheduler.start();
            tabListFeature.start();
            logger.info("oh-my-velocity enabled");
        } catch (Exception ex) {
            logger.error("Failed to enable oh-my-velocity", ex);
        }
    }
}
