# Tab-List Config

```yaml
tab-list:
  enabled: true
  refresh-interval-seconds: 5
  header:
    - "<gold><bold>lkjsxc.com</bold>"
    - "<gray>{online}/{max} players online"
  footer:
    - "<gray>Connected to <yellow>{server}</yellow>"
    - "<dark_gray>{date} {time}"
  display-name-format: "§7[%server%]§r %player%"
  group-order:
    - owner
    - admin
    - mod
    - helper
    - builder
    - vip
    - default
```

## Validation

- `refresh-interval-seconds` must be positive.
- `group-order` must not be empty.
- Display, header, and footer templates are parsed during reload.
