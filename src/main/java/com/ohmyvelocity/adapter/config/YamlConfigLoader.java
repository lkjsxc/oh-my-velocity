package com.ohmyvelocity.adapter.config;

import com.ohmyvelocity.domain.config.YamlDocumentParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class YamlConfigLoader {
    private YamlConfigLoader() {
    }

    public static Map<String, Object> load(Path path) throws IOException {
        return parse(Files.readAllLines(path));
    }

    public static Map<String, Object> parse(List<String> rawLines) {
        return YamlDocumentParser.parse(rawLines);
    }
}
