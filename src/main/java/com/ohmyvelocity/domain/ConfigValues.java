package com.ohmyvelocity.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class ConfigValues {
    private ConfigValues() {
    }

    static Map<String, Object> section(Map<String, Object> root, String key) {
        return section(root, key, key);
    }

    @SuppressWarnings("unchecked")
    static Map<String, Object> section(Map<String, Object> root, String key, String path) {
        Object value = root.get(key);
        if (value == null) {
            return Map.of();
        }
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException(path + " must be a section");
    }

    static boolean bool(Map<String, Object> map, String key, boolean fallback) {
        Object value = map.get(key);
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value == null) {
            return fallback;
        }
        String text = String.valueOf(value);
        if ("true".equalsIgnoreCase(text) || "false".equalsIgnoreCase(text)) {
            return Boolean.parseBoolean(text);
        }
        throw new IllegalArgumentException(key + " must be boolean");
    }

    static int integer(Map<String, Object> map, String key, int fallback) {
        return integer(map, key, fallback, key);
    }

    static int integer(Map<String, Object> map, String key, int fallback, String path) {
        Object value = map.get(key);
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value != null) {
            try {
                return Integer.parseInt(String.valueOf(value));
            } catch (NumberFormatException ignored) {
                throw new IllegalArgumentException(path + " must be an integer");
            }
        }
        return fallback;
    }

    static String text(Map<String, Object> map, String key, String fallback) {
        return text(map, key, fallback, key);
    }

    static String text(Map<String, Object> map, String key, String fallback, String path) {
        Object value = map.get(key);
        return value == null ? fallback : scalarText(path, value);
    }

    static List<String> stringList(Map<String, Object> map, String key, List<String> fallback) {
        Object value = map.get(key);
        if (!(value instanceof List<?> list)) {
            if (value == null) {
                return fallback;
            }
            throw new IllegalArgumentException(key + " must be a list");
        }
        List<String> parsed = new java.util.ArrayList<>();
        for (int index = 0; index < list.size(); index++) {
            parsed.add(scalarText(key + "[" + index + "]", list.get(index)));
        }
        return List.copyOf(parsed);
    }

    static List<Integer> integerList(Map<String, Object> map, String key, List<Integer> fallback) {
        Object value = map.get(key);
        if (!(value instanceof List<?> list)) {
            if (value == null) {
                return fallback;
            }
            throw new IllegalArgumentException(key + " must be an integer list");
        }
        List<Integer> parsed = list.stream().map(item -> parseInteger(key, item)).toList();
        return List.copyOf(parsed);
    }

    @SuppressWarnings("unchecked")
    static List<Map<String, Object>> mapList(Map<String, Object> map, String key) {
        return mapList(map, key, key);
    }

    @SuppressWarnings("unchecked")
    static List<Map<String, Object>> mapList(Map<String, Object> map, String key, String path) {
        Object value = map.get(key);
        if (!(value instanceof List<?> list)) {
            if (value != null) {
                throw new IllegalArgumentException(path + " must be a list of maps");
            }
            return List.of();
        }
        List<Map<String, Object>> parsed = new java.util.ArrayList<>();
        for (int index = 0; index < list.size(); index++) {
            Object item = list.get(index);
            if (!(item instanceof Map<?, ?> itemMap)) {
                throw new IllegalArgumentException(path + "[" + index + "] must be a map");
            }
            parsed.add((Map<String, Object>) itemMap);
        }
        return List.copyOf(parsed);
    }

    @SuppressWarnings("unchecked")
    static Map<String, String> localized(Map<String, Object> map, String key, String fallback) {
        return localized(map, key, fallback, key);
    }

    static Map<String, String> localized(Map<String, Object> map, String key, String fallback, String path) {
        Object value = map.get(key);
        if (value instanceof Map<?, ?> locales) {
            return localizedDirect(locales, path);
        }
        if (value == null) {
            return Map.of("en", fallback);
        }
        throw new IllegalArgumentException(path + " must be a locale map");
    }

    static Map<String, Map<String, String>> localizedMessages(Map<String, Object> map) {
        return localizedMessages(map, "messages");
    }

    static Map<String, Map<String, String>> localizedMessages(Map<String, Object> map, String path) {
        Map<String, Map<String, String>> messages = new LinkedHashMap<>();
        map.forEach((key, value) -> {
            if (value instanceof Map<?, ?> child) {
                messages.put(key, localizedDirect(child, path + "." + key));
            } else {
                throw new IllegalArgumentException(path + "." + key + " must be a locale map");
            }
        });
        return Map.copyOf(messages);
    }

    private static Map<String, String> localizedDirect(Map<?, ?> locales, String path) {
        Map<String, String> parsed = new LinkedHashMap<>();
        locales.forEach((locale, text) -> parsed.put(
                normalizeLocale(locale),
                scalarText(path + "." + normalizeLocale(locale), text)));
        return Map.copyOf(parsed);
    }

    private static String normalizeLocale(Object locale) {
        return String.valueOf(locale).replace('_', '-').toLowerCase(Locale.ROOT);
    }

    private static String scalarText(String path, Object value) {
        if (value instanceof Map<?, ?> || value instanceof List<?>) {
            throw new IllegalArgumentException(path + " must be scalar text");
        }
        return String.valueOf(value);
    }

    private static Integer parseInteger(String key, Object item) {
        if (item instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(item));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(key + " must contain only integers");
        }
    }
}
