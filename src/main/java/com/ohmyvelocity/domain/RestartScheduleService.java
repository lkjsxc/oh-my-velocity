package com.ohmyvelocity.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class RestartScheduleService {
    private final ConfigManager configManager;
    private final MessageService messages;
    private final RestartStateStore stateStore;
    private long nextRestartEpochMs;
    private final java.util.Set<Integer> deliveredWarnings = new java.util.HashSet<>();

    public RestartScheduleService(ConfigManager configManager, MessageService messages, RestartStateStore stateStore) {
        this.configManager = configManager;
        this.messages = messages;
        this.stateStore = stateStore;
    }

    public void loadState() throws IOException {
        nextRestartEpochMs = stateStore.readNextRestartEpochMs();
        deliveredWarnings.clear();
    }

    public void scheduleInitialIfNeeded(long nowEpochMs) throws IOException {
        if (!configManager.config().restart().enabled()) {
            nextRestartEpochMs = 0L;
            stateStore.clear();
            return;
        }
        if (nextRestartEpochMs <= nowEpochMs) {
            nextRestartEpochMs = computeNext(nowEpochMs);
            stateStore.writeNextRestartEpochMs(nextRestartEpochMs);
            deliveredWarnings.clear();
        }
    }

    public RestartTickResult tick(long nowEpochMs) {
        RestartConfig config = configManager.config().restart();
        if (!config.enabled() || nextRestartEpochMs <= 0L) {
            return RestartTickResult.idle();
        }
        long minutesRemaining = Math.max(0L, (nextRestartEpochMs - nowEpochMs + 59_999L) / 60_000L);
        if (minutesRemaining == 0L) {
            deliveredWarnings.clear();
            return RestartTickResult.restartNow(config);
        }
        for (int warning : sortedWarnings(config.warningMinutes())) {
            if (minutesRemaining <= warning && deliveredWarnings.add(warning)) {
                String text = messages.format("restart.warning", Map.of("minutes", String.valueOf(warning)));
                return RestartTickResult.warning(text);
            }
        }
        return RestartTickResult.idle();
    }

    public void rescheduleAfterRestart(long nowEpochMs) throws IOException {
        deliveredWarnings.clear();
        nextRestartEpochMs = computeNext(nowEpochMs);
        stateStore.writeNextRestartEpochMs(nextRestartEpochMs);
    }

    public long nextRestartEpochMs() {
        return nextRestartEpochMs;
    }

    public boolean enabled() {
        return configManager.config().restart().enabled();
    }

    private long computeNext(long nowEpochMs) {
        RestartConfig config = configManager.config().restart();
        long base = config.intervalHours() * 3_600_000L;
        int jitter = config.randomJitterMinutes();
        long extra = jitter <= 0 ? 0L : ThreadLocalRandom.current().nextLong(0, jitter * 60_000L + 1);
        return nowEpochMs + base + extra;
    }

    private static List<Integer> sortedWarnings(List<Integer> warnings) {
        return warnings.stream().sorted((a, b) -> Integer.compare(b, a)).toList();
    }
}
