# Join Messages

## Config

| Key | Default | Behavior |
|-----|---------|----------|
| `join-messages.enabled` | `true` | Master toggle |
| `join-messages.to-player` | catalog | MiniMessage to joining player; empty disables |
| `join-messages.broadcast` | catalog | Broadcast to proxy; empty disables |
| `join-messages.first-join-only` | `false` | In-memory first-seen filter |
| `join-messages.suppress-vanilla` | `false` | Reserved; Velocity 3.5 has no stable hook yet |

## Placeholders

`{player}`, `{online}`, `{max}`, `{server}`

## Events

Fire on `ServerConnectedEvent` so `{server}` reflects the backend when known.

## Permissions

No permission required for receiving messages.
