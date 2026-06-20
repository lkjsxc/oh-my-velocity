package com.ohmyvelocity.domain;

import java.util.Locale;
import java.util.Map;

public record LocalizedMessagesConfig(Map<String, Map<String, String>> messages) {
    public LocalizedMessagesConfig {
        messages = Map.copyOf(messages);
    }

    public String resolve(String key, Locale locale) {
        return LocalizedText.resolve(messages.get(key), locale);
    }
}
