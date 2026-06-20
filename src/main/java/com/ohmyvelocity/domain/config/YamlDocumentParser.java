package com.ohmyvelocity.domain.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class YamlDocumentParser {
    private YamlDocumentParser() { }

    public static Map<String, Object> parse(List<String> rawLines) {
        List<Line> lines = rawLines.stream()
                .map(YamlDocumentParser::line)
                .filter(line -> line != null)
                .toList();
        return new Parser(lines).parseMap(0);
    }

    private static Line line(String raw) {
        String stripped = stripComment(raw);
        if (stripped.strip().isEmpty()) {
            return null;
        }
        return new Line(leadingSpaces(stripped), stripped.strip());
    }

    private static String stripComment(String raw) {
        boolean single = false;
        boolean dbl = false;
        for (int i = 0; i < raw.length(); i++) {
            char current = raw.charAt(i);
            if (current == '\'' && !dbl) {
                single = !single;
            } else if (current == '"' && !single) {
                dbl = !dbl;
            } else if (current == '#' && !single && !dbl) {
                return raw.substring(0, i);
            }
        }
        return raw;
    }

    private static int leadingSpaces(String raw) {
        int count = 0;
        while (count < raw.length() && raw.charAt(count) == ' ') {
            count++;
        }
        return count;
    }

    private static int colonOutsideQuotes(String text) {
        boolean single = false;
        boolean dbl = false;
        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (current == '\'' && !dbl) {
                single = !single;
            } else if (current == '"' && !single) {
                dbl = !dbl;
            } else if (current == ':' && !single && !dbl) {
                return i;
            }
        }
        return -1;
    }

    private static Object parseScalar(String value) {
        if (value.startsWith("[") && value.endsWith("]")) {
            return parseInlineList(value.substring(1, value.length() - 1));
        }
        if ((value.startsWith("\"") && value.endsWith("\""))
                || (value.startsWith("'") && value.endsWith("'"))) {
            return value.substring(1, value.length() - 1);
        }
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return value;
        }
    }

    private static List<Object> parseInlineList(String value) {
        List<Object> items = new ArrayList<>();
        for (String part : splitComma(value)) {
            if (!part.isBlank()) {
                items.add(parseScalar(part.strip()));
            }
        }
        return List.copyOf(items);
    }

    private static List<String> splitComma(String value) {
        List<String> parts = new ArrayList<>();
        boolean single = false;
        boolean dbl = false;
        int start = 0;
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (current == '\'' && !dbl) {
                single = !single;
            } else if (current == '"' && !single) {
                dbl = !dbl;
            } else if (current == ',' && !single && !dbl) {
                parts.add(value.substring(start, i));
                start = i + 1;
            }
        }
        parts.add(value.substring(start));
        return parts;
    }
    private record Line(int indent, String text) { }

    private static final class Parser {
        private final List<Line> lines;
        private int index;

        private Parser(List<Line> lines) {
            this.lines = lines;
        }

        private Map<String, Object> parseMap(int indent) {
            Map<String, Object> map = new LinkedHashMap<>();
            while (index < lines.size()) {
                Line line = lines.get(index);
                if (line.indent() < indent || line.text().startsWith("- ")) {
                    break;
                }
                if (line.indent() > indent) {
                    throw error(line, "unexpected indentation");
                }
                int colon = colonOutsideQuotes(line.text());
                if (colon < 0) {
                    throw error(line, "expected key: value");
                }
                String key = line.text().substring(0, colon).strip();
                String value = line.text().substring(colon + 1).strip();
                index++;
                map.put(key, value.isEmpty() ? parseChild(line.indent()) : parseScalar(value));
            }
            return map;
        }

        private Object parseChild(int parentIndent) {
            if (index >= lines.size() || lines.get(index).indent() <= parentIndent) {
                return new LinkedHashMap<String, Object>();
            }
            Line child = lines.get(index);
            return child.text().startsWith("- ") ? parseList(child.indent()) : parseMap(child.indent());
        }

        private List<Object> parseList(int indent) {
            List<Object> list = new ArrayList<>();
            while (index < lines.size()) {
                Line line = lines.get(index);
                if (line.indent() < indent || !line.text().startsWith("- ")) {
                    break;
                }
                if (line.indent() > indent) {
                    throw error(line, "unexpected indentation");
                }
                String item = line.text().substring(2).strip();
                index++;
                list.add(parseListItem(item, indent));
            }
            return List.copyOf(list);
        }

        private Object parseListItem(String item, int listIndent) {
            if (item.isEmpty()) {
                return parseChild(listIndent);
            }
            int colon = colonOutsideQuotes(item);
            if (colon < 0) {
                if (index < lines.size() && lines.get(index).indent() > listIndent) {
                    throw error(lines.get(index), "unexpected nested scalar content");
                }
                return parseScalar(item);
            }
            Map<String, Object> map = new LinkedHashMap<>();
            String key = item.substring(0, colon).strip();
            String value = item.substring(colon + 1).strip();
            map.put(key, value.isEmpty() ? parseChild(listIndent) : parseScalar(value));
            if (index < lines.size() && lines.get(index).indent() > listIndent) {
                map.putAll(parseMap(lines.get(index).indent()));
            }
            return map;
        }

        private IllegalArgumentException error(Line line, String message) {
            return new IllegalArgumentException(message + ": " + line.text());
        }
    }
}
