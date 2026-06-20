package com.ohmyvelocity.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.ohmyvelocity.domain.ConfigValues.bool;
import static com.ohmyvelocity.domain.ConfigValues.integer;
import static com.ohmyvelocity.domain.ConfigValues.integerList;
import static com.ohmyvelocity.domain.ConfigValues.localized;
import static com.ohmyvelocity.domain.ConfigValues.localizedMessages;
import static com.ohmyvelocity.domain.ConfigValues.mapList;
import static com.ohmyvelocity.domain.ConfigValues.section;
import static com.ohmyvelocity.domain.ConfigValues.stringList;
import static com.ohmyvelocity.domain.ConfigValues.text;

final class PluginConfigFactory {
    private PluginConfigFactory() {
    }

    static PluginConfig fromMap(Map<String, Object> root) {
        return new PluginConfig(
                proxyMessages(root),
                restart(root),
                motd(root),
                tab(root),
                hub(root));
    }

    private static ProxyMessagesConfig proxyMessages(Map<String, Object> root) {
        Map<String, Object> proxy = section(root, "proxy-messages");
        Map<String, Object> legacy = section(root, "join-messages");
        boolean enabled = bool(proxy, "enabled", bool(legacy, "enabled", true));
        boolean firstOnly = bool(proxy, "first-join-only", bool(legacy, "first-join-only", false));
        boolean suppress = bool(proxy, "suppress-vanilla", bool(legacy, "suppress-vanilla", false));
        LocalizedTemplateConfig join = template(
                section(proxy, "join"),
                text(legacy, "to-player", "<green>Welcome, <yellow>{player}</yellow>!"),
                text(legacy, "broadcast", "<gray>[+] <white>{player}</white> joined."));
        LocalizedTemplateConfig leave = template(section(proxy, "leave"), "", "");
        return new ProxyMessagesConfig(enabled, join, leave, firstOnly, suppress);
    }

    private static RestartConfig restart(Map<String, Object> root) {
        Map<String, Object> restart = section(root, "restart");
        return new RestartConfig(
                bool(restart, "enabled", true),
                integer(restart, "interval-hours", 24),
                integer(restart, "random-jitter-minutes", 10),
                integerList(restart, "warning-minutes", List.of(60, 15, 5, 1)),
                text(restart, "kick-message", ""),
                text(restart, "mode", "graceful_shutdown"),
                text(restart, "external-hook", ""));
    }

    private static MotdConfig motd(Map<String, Object> root) {
        Map<String, Object> motd = section(root, "motd");
        List<MotdEntry> entries = mapList(motd, "entries").stream()
                .map(item -> new MotdEntry(
                        text(item, "line1", ""),
                        text(item, "line2", ""),
                        Math.max(1, integer(item, "weight", 1)),
                        text(item, "icon", "")))
                .toList();
        return new MotdConfig(
                bool(motd, "enabled", false),
                entries.isEmpty() ? MotdConfig.defaults().entries() : entries,
                integer(motd, "max-players", 128),
                stringList(motd, "hover", List.of()),
                stringList(motd, "sample-players", List.of()),
                bool(motd, "hide-player-count", false),
                stringList(motd, "target-servers", List.of("*")));
    }

    private static TabConfig tab(Map<String, Object> root) {
        Map<String, Object> tab = section(root, "tab");
        Map<String, Object> ping = section(tab, "ping-objective");
        return new TabConfig(
                bool(tab, "enabled", false),
                Math.max(1, integer(tab, "refresh-interval-seconds", 5)),
                stringList(tab, "header", List.of("<gold><bold>lkjsxc.com</bold>")),
                stringList(tab, "footer", List.of("<gray>{server}")),
                text(tab, "display-name-format", "§7[%server%]§r %player%"),
                stringList(tab, "group-order", List.of("owner", "admin", "mod", "helper", "builder", "vip", "default")),
                new PingObjectiveConfig(
                        bool(ping, "enabled", true),
                        text(ping, "name", "omv_ping"),
                        text(ping, "title", "<green>Ping")),
                text(tab, "collision-rule", "never"),
                text(tab, "name-visibility", "always"));
    }

    private static HubCommandConfig hub(Map<String, Object> root) {
        Map<String, Object> hub = section(root, "hub-command");
        Map<String, Map<String, String>> messages = new LinkedHashMap<>(defaultHubMessages());
        localizedMessages(section(hub, "messages")).forEach((key, value) -> {
            Map<String, String> merged = new LinkedHashMap<>(messages.getOrDefault(key, Map.of()));
            merged.putAll(value);
            messages.put(key, Map.copyOf(merged));
        });
        return new HubCommandConfig(
                bool(hub, "enabled", true),
                text(hub, "target-server", "hub"),
                new LocalizedMessagesConfig(messages));
    }

    private static LocalizedTemplateConfig template(
            Map<String, Object> section,
            String defaultToPlayer,
            String defaultBroadcast) {
        return new LocalizedTemplateConfig(
                localized(section, "to-player", defaultToPlayer),
                localized(section, "broadcast", defaultBroadcast));
    }

    private static Map<String, Map<String, String>> defaultHubMessages() {
        return Map.of(
                "connecting", Map.of("en", "<gray>Sending you to <yellow>{server}</yellow>..."),
                "connected", Map.of("en", "<green>Connected to <yellow>{server}</yellow>."),
                "already-connected", Map.of("en", "<gray>You are already on <yellow>{server}</yellow>."),
                "unavailable", Map.of("en", "<red>The hub server is not available."),
                "failed", Map.of("en", "<red>Could not connect to <yellow>{server}</yellow>."),
                "disabled", Map.of("en", "<red>/hub is disabled."),
                "players-only", Map.of("en", "<red>Only players can use /hub."));
    }
}
