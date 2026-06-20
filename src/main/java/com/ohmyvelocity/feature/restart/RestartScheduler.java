package com.ohmyvelocity.feature.restart;

import com.ohmyvelocity.adapter.config.ConfigManager;
import com.ohmyvelocity.adapter.runtime.RestartScheduleService;
import com.ohmyvelocity.domain.MessageService;
import com.ohmyvelocity.domain.RestartConfig;
import com.ohmyvelocity.domain.RestartTickResult;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class RestartScheduler {
    private final Object plugin;
    private final ProxyServer server;
    private final Logger logger;
    private final ConfigManager configManager;
    private final RestartScheduleService schedule;
    private final MessageService messages;
    private final ShutdownExecutor shutdownExecutor;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final AtomicBoolean restarting = new AtomicBoolean(false);
    private ScheduledTask tickTask;

    public RestartScheduler(
            Object plugin,
            ProxyServer server,
            Logger logger,
            ConfigManager configManager,
            RestartScheduleService schedule,
            MessageService messages,
            ShutdownExecutor shutdownExecutor) {
        this.plugin = plugin;
        this.server = server;
        this.logger = logger;
        this.configManager = configManager;
        this.schedule = schedule;
        this.messages = messages;
        this.shutdownExecutor = shutdownExecutor;
    }

    public void start() throws IOException {
        long now = System.currentTimeMillis();
        schedule.loadState();
        schedule.scheduleInitialIfNeeded(now);
        tickTask = server.getScheduler()
                .buildTask(plugin, this::tick)
                .repeat(1L, TimeUnit.MINUTES)
                .schedule();
    }

    public void stop() {
        if (tickTask != null) {
            tickTask.cancel();
            tickTask = null;
        }
    }

    public void reload() throws IOException {
        long now = System.currentTimeMillis();
        schedule.loadState();
        schedule.scheduleInitialIfNeeded(now);
    }

    public void restartNow() {
        if (!configManager.config().restart().manualCommandEnabled()) {
            return;
        }
        performRestart(configManager.config().restart());
    }

    private void tick() {
        if (server.isShuttingDown()) {
            return;
        }
        RestartTickResult result = schedule.tick(System.currentTimeMillis());
        if (result.type() == RestartTickResult.Type.WARNING) {
            var component = miniMessage.deserialize(result.warningMessage());
            server.getAllPlayers().forEach(player -> player.sendMessage(component));
            return;
        }
        if (result.type() == RestartTickResult.Type.RESTART_NOW) {
            performRestart(result.restartConfig());
        }
    }

    private void performRestart(RestartConfig config) {
        if (!restarting.compareAndSet(false, true)) {
            return;
        }
        try {
            schedule.rescheduleAfterRestart(System.currentTimeMillis());
        } catch (IOException ex) {
            logger.warn("Failed to persist next restart: {}", ex.getMessage());
        }
        String kickTemplate = config.kickMessage().resolve("kick", java.util.Locale.ENGLISH);
        var kick = miniMessage.deserialize(kickTemplate);
        if (config.externalHookMode() && !config.externalHook().isBlank()) {
            shutdownExecutor.runExternalHook(config.externalHook(), config.externalHookTimeoutSeconds(), logger);
        }
        logger.info("oh-my-velocity initiating scheduled proxy shutdown");
        server.shutdown(kick);
    }

    public long nextRestartEpochMs() {
        return schedule.nextRestartEpochMs();
    }

    public boolean enabled() {
        return schedule.enabled();
    }
}
