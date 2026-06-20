# MOTD Config

```yaml
motd:
  enabled: true
  max-players: 128
  hide-player-count: false
  target-hosts:
    - "*"
  samples:
    - "lkjsxc.com"
    - "{online}/{max} players online"
  entries:
    - line1: "<gold><bold>lkjsxc.com</bold> <gray>| <white>Velocity Network"
      line2: "<yellow>Survival</yellow> <gray>+</gray> <aqua>Creative</aqua>"
      weight: 1
      icon: ""
```

## Validation

- `max-players` must be positive.
- Each entry weight must be positive.
- At least one enabled entry is required when MOTD is enabled.
- `target-hosts` defaults to `*` when omitted.
