package com.ohmyvelocity.domain;

import java.util.Locale;
import java.util.Map;

public record LocalizedTemplateConfig(Map<String, String> toPlayer, Map<String, String> broadcast) {
    public LocalizedTemplateConfig {
        toPlayer = Map.copyOf(toPlayer);
        broadcast = Map.copyOf(broadcast);
    }

    public String toPlayer(Locale locale) {
        return LocalizedText.resolve(toPlayer, locale);
    }

    public String toPlayer(String locale) {
        return toPlayer(Locale.forLanguageTag(locale));
    }

    public String broadcast(Locale locale) {
        return LocalizedText.resolve(broadcast, locale);
    }

    public String broadcast(String locale) {
        return broadcast(Locale.forLanguageTag(locale));
    }
}
