package com.ohmyvelocity.feature.restart;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class ShutdownExecutor {
    public void runExternalHook(String command, int timeoutSeconds, Logger logger) {
        if (command == null || command.isBlank()) {
            return;
        }
        try {
            Process process = new ProcessBuilder("/bin/sh", "-c", command).start();
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                logger.warn("External restart hook timed out");
            }
        } catch (IOException ex) {
            logger.warn("External restart hook failed: {}", ex.getMessage());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            logger.warn("External restart hook failed: {}", ex.getMessage());
        }
    }
}
