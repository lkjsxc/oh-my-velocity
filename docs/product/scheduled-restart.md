# Scheduled Restart

## Config

| Key | Default | Behavior |
|-----|---------|----------|
| `restart.enabled` | `true` | Master toggle |
| `restart.interval-hours` | `24` | Hours between restarts |
| `restart.random-jitter-minutes` | `10` | Random delay added once per cycle |
| `restart.warning-minutes` | `60,15,5,1` | Broadcast warnings before restart |
| `restart.kick-message` | catalog | MiniMessage for shutdown kick |
| `restart.mode` | `graceful_shutdown` | Or `external_hook` |
| `restart.external-hook` | `""` | Shell command before shutdown |

## Flow

1. Schedule next restart with jitter; persist epoch to `next-restart.yml`.
2. Every minute, check warnings and fire undelivered ones.
3. At deadline, run optional hook, then `proxy.shutdown(Component)`.

## Safety

- Skip when `proxy.isShuttingDown()`.
- Atomic guard prevents double restart.
- Cancel scheduler tasks on plugin disable.

## Supervisor

Plugin shutdown does not restart the JVM. See
[operations/restart-supervisor.md](../operations/restart-supervisor.md).
