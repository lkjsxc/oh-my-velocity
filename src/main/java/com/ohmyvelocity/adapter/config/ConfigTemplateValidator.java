package com.ohmyvelocity.adapter.config;

import com.ohmyvelocity.domain.LegacyColorTranslator;
import com.ohmyvelocity.domain.LocalizedMessagesConfig;
import com.ohmyvelocity.domain.MotdEntry;
import com.ohmyvelocity.domain.PluginConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.Map;

final class ConfigTemplateValidator {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private ConfigTemplateValidator() {
    }

    static PluginConfig validate(PluginConfig config) {
        validateLocalized("messages.join.to-player", config.proxyMessages().join().toPlayer());
        validateLocalized("messages.join.broadcast", config.proxyMessages().join().broadcast());
        validateLocalized("messages.leave.broadcast", config.proxyMessages().leave().broadcast());
        validateMotd(config);
        validateList("tab-list.header", config.tab().header());
        validateList("tab-list.footer", config.tab().footer());
        validateTemplate("tab-list.display-name-format", config.tab().displayNameFormat());
        validateMessages("hub.messages", config.hubCommand().messages());
        validateMessages("restart.warning-message", config.restart().warningMessage());
        validateMessages("restart.kick-message", config.restart().kickMessage());
        return config;
    }

    private static void validateMotd(PluginConfig config) {
        int index = 0;
        for (MotdEntry entry : config.motd().entries()) {
            validateTemplate("motd.entries[" + index + "].line1", entry.line1());
            validateTemplate("motd.entries[" + index + "].line2", entry.line2());
            index++;
        }
    }

    private static void validateMessages(String path, LocalizedMessagesConfig config) {
        config.messages().forEach((key, values) -> validateLocalized(path + "." + key, values));
    }

    private static void validateLocalized(String path, Map<String, String> values) {
        values.forEach((locale, template) -> validateTemplate(path + "." + locale, template));
    }

    private static void validateList(String path, List<String> templates) {
        for (int index = 0; index < templates.size(); index++) {
            validateTemplate(path + "[" + index + "]", templates.get(index));
        }
    }

    private static void validateTemplate(String path, String template) {
        if (template == null || template.isBlank()) {
            return;
        }
        requireClosedAngles(path, template);
        try {
            MINI_MESSAGE.deserialize(LegacyColorTranslator.toMiniMessage(template));
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException(path + " has invalid MiniMessage: " + ex.getMessage(), ex);
        }
    }

    private static void requireClosedAngles(String path, String template) {
        int open = template.indexOf('<');
        while (open >= 0) {
            int close = template.indexOf('>', open + 1);
            if (close < 0) {
                throw new IllegalArgumentException(path + " has an unclosed MiniMessage tag");
            }
            open = template.indexOf('<', close + 1);
        }
    }
}
