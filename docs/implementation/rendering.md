# Rendering

The renderer replaces documented placeholders and preserves unknown placeholders.

## Placeholder Forms

- `{key}` is the canonical form.
- `%key%` is accepted for supported tab-list compatibility aliases.

## Color Forms

- MiniMessage tags are passed to MiniMessage in adapters.
- Legacy `&` and `§` codes are converted before MiniMessage parsing.

## Validation

Configured templates are parsed during load/reload. Event handlers should not
discover invalid template syntax late.
