package com.ohmyvelocity.domain;

import java.util.Map;

public final class PlaceholderFormatter {
    private PlaceholderFormatter() {
    }

    public static String format(String template, Map<String, String> values) {
        if (template == null || template.isEmpty()) {
            return "";
        }
        String result = template;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
            result = result.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return result;
    }
}
