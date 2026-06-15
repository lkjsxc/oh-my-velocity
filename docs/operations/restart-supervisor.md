# Restart Supervisor

## Responsibility Split

| Layer | Owns |
|-------|------|
| oh-my-velocity | Warnings, graceful `proxy.shutdown()` |
| Host / container | Process restart after JVM exit |

## Docker

Set `restart: unless-stopped` on the Velocity service, or use a compose
recreate timer for forced container refresh.

## Without Supervisor

`proxy.shutdown()` stops the proxy until an operator or supervisor starts it
again.

## external_hook Mode

Runs `restart.external-hook` immediately before shutdown. Use for compose
recreate scripts when the JVM cannot restart itself.
