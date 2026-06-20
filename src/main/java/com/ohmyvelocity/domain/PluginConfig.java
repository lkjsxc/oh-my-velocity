package com.ohmyvelocity.domain;

import java.util.Map;

public record PluginConfig(
        ProxyMessagesConfig proxyMessages,
        RestartConfig restart,
        MotdConfig motd,
        TabConfig tab,
        HubCommandConfig hubCommand) {
    public static PluginConfig fromMap(Map<String, Object> root) {
        return PluginConfigFactory.fromMap(root);
    }

    public JoinMessagesConfig joinMessages() {
        return new JoinMessagesConfig(
                proxyMessages.enabled(),
                proxyMessages.join().toPlayer("en"),
                proxyMessages.join().broadcast("en"),
                proxyMessages.firstJoinOnly(),
                proxyMessages.suppressVanilla());
    }
}
