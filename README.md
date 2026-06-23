# oh-my-velocity

Self-contained Velocity proxy plugin for MOTD, native tab-list presentation,
`/hub`, network messages, reload, and scheduled graceful restarts.

## Build

```sh
./gradlew jar
```

Output: `build/libs/oh-my-velocity-*.jar`

## Install

Copy the JAR into your Velocity `plugins/` folder. Requires Velocity 3.5+ and
Java 21. No other Minecraft or Velocity plugin is required.

## Verify

```sh
./scripts/verify.sh
```

Or:

```sh
docker compose -f docker-compose.verify.yml run --rm verify
```

## Configuration

After first start, edit `plugins/ohmyvelocity/config.yml`.

### Network messages

- `messages.join.to-player` — MiniMessage sent to the joining player
- `messages.join.broadcast` — MiniMessage broadcast to other players
- `messages.leave.broadcast` — MiniMessage broadcast after disconnect
- Placeholders: `{player}`, `{online}`, `{max}`, `{server}`

### MOTD, tab-list, and hub

- `motd.entries` — weighted server-list MOTDs
- `tab-list.header` / `tab-list.footer` — native tab header/footer lines
- `tab-list.display-name-format` — subject display name format
- `hub.target-server` — `/hub` destination, default `hub`

### Scheduled restart

- `restart.schedule-enabled` — scheduled restart toggle
- `restart.manual-command-enabled` — admin command toggle
- `restart.warning-minutes` — warning thresholds before shutdown
- `restart.mode` — `graceful_shutdown` or `external_hook`

**Important:** `proxy.shutdown()` stops the JVM. A host supervisor (Docker
`restart: unless-stopped`, systemd timer, etc.) must restart Velocity. See
[docs/operations/restart-supervisor.md](docs/operations/restart-supervisor.md).

## Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/ohmyvelocity`, `/omv version` | none | Show plugin version |
| `/omv reload` | `ohmyvelocity.admin` | Reload config |
| `/omv restart now` | `ohmyvelocity.admin` | Immediate restart |
| `/omv restart status` | `ohmyvelocity.admin` | Show next restart time |
| `/hub` | none | Connect to the configured hub server |

## Docs

See [docs/README.md](docs/README.md) for product contracts, architecture,
operations, and verification.

## License

Apache License 2.0 — see [LICENSE](LICENSE).
