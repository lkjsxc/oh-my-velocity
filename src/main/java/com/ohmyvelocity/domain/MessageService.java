package com.ohmyvelocity.domain;

public final class MessageService {
    private final MessageCatalog catalog;

    public MessageService(MessageCatalog catalog) {
        this.catalog = catalog;
    }

    public String raw(String key) {
        return catalog.get(key);
    }

    public String format(String key, java.util.Map<String, String> placeholders) {
        return PlaceholderFormatter.format(catalog.get(key), placeholders);
    }
}
