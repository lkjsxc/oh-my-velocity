package com.ohmyvelocity.adapter.config;

import com.ohmyvelocity.domain.PluginConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public final class ConfigManager {
    private final Path configPath;
    private PluginConfig config;

    public ConfigManager(Path dataDirectory) {
        this.configPath = dataDirectory.resolve("config.yml");
    }

    public void ensureDefault() throws IOException {
        Files.createDirectories(configPath.getParent());
        if (!Files.exists(configPath)) {
            try (InputStream stream = ConfigManager.class.getClassLoader().getResourceAsStream("config.yml")) {
                if (stream != null) {
                    Files.copy(stream, configPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        reload();
    }

    public void reload() throws IOException {
        Map<String, Object> root = YamlConfigLoader.load(configPath);
        this.config = ConfigTemplateValidator.validate(PluginConfig.fromMap(root));
    }

    public PluginConfig config() {
        return config;
    }

    public Path configPath() {
        return configPath;
    }
}
