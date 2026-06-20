# Hub Config

```yaml
hub:
  enabled: true
  target-server: "hub"
  messages:
    connecting:
      en: "<gray>Sending you to <yellow>{server}</yellow>..."
    connected:
      en: "<green>Connected to <yellow>{server}</yellow>."
    already-connected:
      en: "<gray>You are already on <yellow>{server}</yellow>."
    unavailable:
      en: "<red>The hub server is not available."
    failed:
      en: "<red>Could not connect to <yellow>{server}</yellow>."
    disabled:
      en: "<red>/hub is disabled."
    players-only:
      en: "<red>Only players can use /hub."
```

## Validation

- `target-server` must be non-empty when `hub.enabled` is true.
- Every message key must have at least one locale value.
