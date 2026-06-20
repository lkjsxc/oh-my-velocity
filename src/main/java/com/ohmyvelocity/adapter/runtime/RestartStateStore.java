package com.ohmyvelocity.adapter.runtime;

import com.ohmyvelocity.adapter.config.YamlConfigLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class RestartStateStore {
    private final Path statePath;

    public RestartStateStore(Path dataDirectory) {
        this.statePath = dataDirectory.resolve("next-restart.yml");
    }

    public long readNextRestartEpochMs() throws IOException {
        if (!Files.exists(statePath)) {
            return 0L;
        }
        Map<String, Object> root = YamlConfigLoader.load(statePath);
        Object value = root.get("next-restart-epoch-ms");
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String text) {
            try {
                return Long.parseLong(text);
            } catch (NumberFormatException ignored) {
                return 0L;
            }
        }
        return 0L;
    }

    public void writeNextRestartEpochMs(long epochMs) throws IOException {
        Files.createDirectories(statePath.getParent());
        String content = "next-restart-epoch-ms: " + epochMs + System.lineSeparator();
        Files.writeString(statePath, content);
    }

    public void clear() throws IOException {
        if (Files.exists(statePath)) {
            Files.delete(statePath);
        }
    }
}
