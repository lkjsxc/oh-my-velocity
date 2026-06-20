package com.ohmyvelocity.domain;

import java.util.Locale;
import java.util.Map;

public final class TabRenderService {
    public TabRenderPlan render(TabConfig config, TabRenderContext context) {
        Map<String, String> values = MotdService.values(
                context.player(),
                context.online(),
                context.max(),
                context.server(),
                context.ping());
        return new TabRenderPlan(
                renderLines(config.header(), values),
                renderLines(config.footer(), values),
                render(config.displayNameFormat(), values),
                order(config, context.group()));
    }

    public int order(TabConfig config, String group) {
        String normalized = group == null ? "default" : group.toLowerCase(Locale.ROOT);
        int index = config.groupOrder().indexOf(normalized);
        return (index < 0 ? config.groupOrder().size() : index) * 1000;
    }

    private static java.util.List<String> renderLines(java.util.List<String> lines, Map<String, String> values) {
        return lines.stream().map(line -> render(line, values)).toList();
    }

    private static String render(String template, Map<String, String> values) {
        return LegacyColorTranslator.toMiniMessage(PlaceholderFormatter.format(template, values));
    }
}
