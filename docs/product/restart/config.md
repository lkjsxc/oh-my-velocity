# Restart Config

```yaml
restart:
  schedule-enabled: true
  manual-command-enabled: true
  interval-hours: 24
  random-jitter-minutes: 10
  warning-minutes:
    - 60
    - 15
    - 5
    - 1
  warning-message:
    en: "<gold>Proxy restart in <yellow>{minutes}</yellow> minute(s)."
  kick-message:
    en: "<red>The proxy is restarting for maintenance. Please reconnect shortly."
  mode: graceful_shutdown
  external-hook: ""
  external-hook-timeout-seconds: 30
```

## Validation

- `interval-hours` must be positive.
- `random-jitter-minutes` must be non-negative.
- `warning-minutes` must be positive and unique.
- Hook timeout must be positive.
- Warning and kick messages must not be empty.
