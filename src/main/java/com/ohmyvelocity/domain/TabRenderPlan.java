package com.ohmyvelocity.domain;

import java.util.List;

public record TabRenderPlan(List<String> header, List<String> footer, String displayName, int order) {
    public TabRenderPlan {
        header = List.copyOf(header);
        footer = List.copyOf(footer);
    }
}
