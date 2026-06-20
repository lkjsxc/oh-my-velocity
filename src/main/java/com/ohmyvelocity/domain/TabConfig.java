package com.ohmyvelocity.domain;

import java.util.List;

public record TabConfig(
        boolean enabled,
        int refreshIntervalSeconds,
        List<String> header,
        List<String> footer,
        String displayNameFormat,
        List<String> groupOrder) {
    public TabConfig {
        header = List.copyOf(header);
        footer = List.copyOf(footer);
        groupOrder = List.copyOf(groupOrder);
    }
}
