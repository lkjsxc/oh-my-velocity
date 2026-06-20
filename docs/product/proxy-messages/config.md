# Proxy Message Config

```yaml
messages:
  enabled: true
  first-join-only: false
  join:
    to-player:
      en: "<green>Welcome, <white>{player}</white>!"
    broadcast:
      en: "<gray>[+] <white>{player}</white> joined <yellow>{server}</yellow>. <dark_gray>({online}/{max})"
  leave:
    broadcast:
      en: "<gray>[-] <white>{player}</white> left the network. <dark_gray>({online}/{max})"
```

## Validation

- Locale maps may not be empty when their message path is enabled.
- `enabled: false` disables all proxy messages.
- Unknown legacy config keys are not part of the active schema.
- MiniMessage templates are parsed during reload, not during event handling.
