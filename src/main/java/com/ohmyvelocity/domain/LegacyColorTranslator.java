package com.ohmyvelocity.domain;

import java.util.Map;

public final class LegacyColorTranslator {
    private static final Map<Character, String> TAGS = Map.ofEntries(
            Map.entry('0', "black"),
            Map.entry('1', "dark_blue"),
            Map.entry('2', "dark_green"),
            Map.entry('3', "dark_aqua"),
            Map.entry('4', "dark_red"),
            Map.entry('5', "dark_purple"),
            Map.entry('6', "gold"),
            Map.entry('7', "gray"),
            Map.entry('8', "dark_gray"),
            Map.entry('9', "blue"),
            Map.entry('a', "green"),
            Map.entry('b', "aqua"),
            Map.entry('c', "red"),
            Map.entry('d', "light_purple"),
            Map.entry('e', "yellow"),
            Map.entry('f', "white"),
            Map.entry('l', "bold"),
            Map.entry('m', "strikethrough"),
            Map.entry('n', "underlined"),
            Map.entry('o', "italic"),
            Map.entry('k', "obfuscated"),
            Map.entry('r', "reset"));

    private LegacyColorTranslator() {
    }

    public static String toMiniMessage(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        StringBuilder output = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            if ((current == '&' || current == '§') && i + 1 < input.length()) {
                String tag = TAGS.get(Character.toLowerCase(input.charAt(i + 1)));
                if (tag != null) {
                    output.append('<').append(tag).append('>');
                    i++;
                    continue;
                }
            }
            output.append(current);
        }
        return output.toString();
    }
}
