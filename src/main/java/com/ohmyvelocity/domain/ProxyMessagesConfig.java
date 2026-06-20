package com.ohmyvelocity.domain;

public record ProxyMessagesConfig(
        boolean enabled,
        LocalizedTemplateConfig join,
        LocalizedTemplateConfig leave,
        boolean firstJoinOnly,
        boolean suppressVanilla) {
}
