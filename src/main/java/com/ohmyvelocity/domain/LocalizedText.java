package com.ohmyvelocity.domain;

import java.util.Locale;
import java.util.Map;

final class LocalizedText {
    private LocalizedText() {
    }

    static String resolve(Map<String, String> values, Locale locale) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        String exact = locale == null ? "" : locale.toLanguageTag().toLowerCase(Locale.ROOT);
        if (values.containsKey(exact)) {
            return values.get(exact);
        }
        String language = locale == null ? "" : locale.getLanguage().toLowerCase(Locale.ROOT);
        if (values.containsKey(language)) {
            return values.get(language);
        }
        if (values.containsKey("en")) {
            return values.get("en");
        }
        return values.values().iterator().next();
    }
}
