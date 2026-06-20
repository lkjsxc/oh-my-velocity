package com.ohmyvelocity.domain;

import java.util.Map;

public record PluginConfig(
        ProxyMessagesConfig proxyMessages,
        RestartConfig restart,
        MotdConfig motd,
        TabConfig tab,
        HubCommandConfig hubCommand) {
    public static PluginConfig fromMap(Map<String, Object> root) {
        return ConfigValidation.validate(PluginConfigFactory.fromMap(root));
    }
}
