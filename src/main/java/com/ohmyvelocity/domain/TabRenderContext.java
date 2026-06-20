package com.ohmyvelocity.domain;

public record TabRenderContext(
        String player,
        String server,
        String group,
        int online,
        int max,
        long ping) {
}
