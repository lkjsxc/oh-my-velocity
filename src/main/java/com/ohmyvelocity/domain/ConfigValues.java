package com.ohmyvelocity.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class ConfigValues {
    private ConfigValues() {
    }

    @SuppressWarnings("unchecked")
    static Map<String, Object> section(Map<String, Object> root, String key) {
        Object value = root.get(key);
        return value instanceof Map<?, ?> map ? (Map<String, Object>) map : Map.of();
    }

    static boolean bool(Map<String, Object> map, String key, boolean fallback) {
        Object value = map.get(key);
        if (value instanceof Boolean bool) {
            return bool;
        }
        return value == null ? fallback : Boolean.parseBoolean(String.valueOf(value));
    }

    static int integer(Map<String, Object> map, String key, int fallback) {
        Object value = map.get(key);
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value != null) {
            try {
                return Integer.parseInt(String.valueOf(value));
            } catch (NumberFormatException ignored) {
                return fallback;
            }
        }
        return fallback;
    }

    static String text(Map<String, Object> map, String key, String fallback) {
        Object value = map.get(key);
        return value == null ? fallback : String.valueOf(value);
    }

    static List<String> stringList(Map<String, Object> map, String key, List<String> fallback) {
        Object value = map.get(key);
        if (!(value instanceof List<?> list)) {
            return fallback;
        }
        List<String> parsed = list.stream().map(String::valueOf).toList();
        return parsed.isEmpty() ? fallback : List.copyOf(parsed);
    }

    static List<Integer> integerList(Map<String, Object> map, String key, List<Integer> fallback) {
        Object value = map.get(key);
        if (!(value instanceof List<?> list)) {
            return fallback;
        }
        List<Integer> parsed = list.stream().map(ConfigValues::parseInteger).toList();
        return parsed.isEmpty() ? fallback : List.copyOf(parsed);
    }

    @SuppressWarnings("unchecked")
    static List<Map<String, Object>> mapList(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (!(value instanceof List<?> list)) {
            return List.of();
        }
        return list.stream()
                .filter(Map.class::isInstance)
                .map(item -> (Map<String, Object>) item)
                .toList();
    }

    @SuppressWarnings("unchecked")
    static Map<String, String> localized(Map<String, Object> map, String key, String fallback) {
        Object value = map.get(key);
        if (value instanceof Map<?, ?> locales) {
            Map<String, String> parsed = new LinkedHashMap<>();
            locales.forEach((locale, text) -> parsed.put(normalizeLocale(locale), String.valueOf(text)));
            return parsed.isEmpty() ? Map.of("en", fallback) : Map.copyOf(parsed);
        }
        if (value instanceof String text && !text.isBlank()) {
            return Map.of("en", text);
        }
        return fallback.isBlank() ? Map.of() : Map.of("en", fallback);
    }

    @SuppressWarnings("unchecked")
    static Map<String, Map<String, String>> localizedMessages(Map<String, Object> map) {
        Map<String, Map<String, String>> messages = new LinkedHashMap<>();
        map.forEach((key, value) -> {
            if (value instanceof Map<?, ?> child) {
                messages.put(key, localized((Map<String, Object>) child, "value", ""));
                if (!child.containsKey("value")) {
                    messages.put(key, localizedDirect(child));
                }
            } else {
                messages.put(key, Map.of("en", String.valueOf(value)));
            }
        });
        return Map.copyOf(messages);
    }

    private static Map<String, String> localizedDirect(Map<?, ?> locales) {
        Map<String, String> parsed = new LinkedHashMap<>();
        locales.forEach((locale, text) -> parsed.put(normalizeLocale(locale), String.valueOf(text)));
        return Map.copyOf(parsed);
    }

    private static String normalizeLocale(Object locale) {
        return String.valueOf(locale).replace('_', '-').toLowerCase(Locale.ROOT);
    }

    private static Integer parseInteger(Object item) {
        if (item instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(item));
    }
}
