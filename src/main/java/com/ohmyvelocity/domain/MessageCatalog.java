package com.ohmyvelocity.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class MessageCatalog {
    private final Map<String, String> messages;

    public MessageCatalog(Map<String, String> messages) {
        this.messages = Map.copyOf(messages);
    }

    public String get(String key) {
        return messages.getOrDefault(key, key);
    }

    public static MessageCatalog load(InputStream stream) {
        if (stream == null) {
            return new MessageCatalog(Map.of());
        }
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            Map<String, String> loaded = new Gson().fromJson(reader, new TypeToken<Map<String, String>>() { }.getType());
            return new MessageCatalog(loaded == null ? Map.of() : loaded);
        } catch (Exception ex) {
            return new MessageCatalog(Map.of());
        }
    }
}
