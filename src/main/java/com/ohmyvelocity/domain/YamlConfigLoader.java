package com.ohmyvelocity.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class YamlConfigLoader {
    private YamlConfigLoader() {
    }

    public static Map<String, Object> load(Path path) throws IOException {
        return parse(Files.readAllLines(path));
    }

    static Map<String, Object> parse(List<String> lines) {
        Map<String, Object> root = new LinkedHashMap<>();
        String section = null;
        String listKey = null;
        List<Integer> list = null;

        for (String raw : lines) {
            if (raw.strip().isEmpty() || raw.strip().startsWith("#")) {
                continue;
            }
            int indent = leadingSpaces(raw);
            String line = raw.strip();

            if (line.startsWith("- ") && list != null) {
                list.add(Integer.parseInt(line.substring(2).strip()));
                continue;
            }
            if (list != null) {
                commitList(root, section, listKey, list);
                list = null;
                listKey = null;
            }

            int colon = line.indexOf(':');
            if (colon < 0) {
                continue;
            }
            String key = line.substring(0, colon).strip();
            String value = line.substring(colon + 1).strip();

            if (value.isEmpty()) {
                if (indent == 0) {
                    section = key;
                    root.putIfAbsent(section, new LinkedHashMap<String, Object>());
                } else if (section != null) {
                    listKey = key;
                    list = new ArrayList<>();
                }
                continue;
            }

            Object parsed = parseScalar(value);
            if (indent == 0) {
                section = null;
                root.put(key, parsed);
            } else if (section != null) {
                sectionMap(root, section).put(key, parsed);
            }
        }
        if (list != null) {
            commitList(root, section, listKey, list);
        }
        return root;
    }

    private static int leadingSpaces(String raw) {
        int count = 0;
        while (count < raw.length() && raw.charAt(count) == ' ') {
            count++;
        }
        return count;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> sectionMap(Map<String, Object> root, String section) {
        return (Map<String, Object>) root.computeIfAbsent(section, ignored -> new LinkedHashMap<>());
    }

    private static void commitList(Map<String, Object> root, String section, String key, List<Integer> list) {
        if (section == null || key == null) {
            return;
        }
        sectionMap(root, section).put(key, List.copyOf(list));
    }

    private static Object parseScalar(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return value;
        }
    }
}
