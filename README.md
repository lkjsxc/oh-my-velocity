# oh-my-velocity

Velocity proxy plugin: join messages, scheduled graceful restarts, and more.

## Build

```sh
./gradlew jar
```

Output: `build/libs/oh-my-velocity-0.1.0.jar`

## Install

Copy the JAR into your Velocity `plugins/` folder. Requires Velocity 3.5+ and Java 21.

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

### Join messages

- `join-messages.to-player` — MiniMessage sent to the joining player
- `join-messages.broadcast` — MiniMessage broadcast to other players
- Placeholders: `{player}`, `{online}`, `{max}`, `{server}`

### Scheduled restart

- `restart.interval-hours` — default 24
- `restart.warning-minutes` — warnings before shutdown
- `restart.mode` — `graceful_shutdown` or `external_hook`

**Important:** `proxy.shutdown()` stops the JVM. A host supervisor (Docker
`restart: unless-stopped`, systemd timer, etc.) must restart Velocity. See
[docs/operations/restart-supervisor.md](docs/operations/restart-supervisor.md).

## Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/omv reload` | `ohmyvelocity.admin` | Reload config |
| `/omv restart now` | `ohmyvelocity.admin` | Immediate restart |
| `/omv restart status` | `ohmyvelocity.admin` | Show next restart time |

## Docs

See [docs/README.md](docs/README.md) for contracts and architecture.

## License

Apache License 2.0 — see [LICENSE](LICENSE).
