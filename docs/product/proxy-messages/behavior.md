# Proxy Message Behavior

## Join

When `messages.enabled` is true, a joining player may receive
`messages.join.to-player`. The network may also receive
`messages.join.broadcast`.

The join broadcast excludes the joining player. Use `to-player` for private
welcome text.

## First Seen

`messages.first-join-only` applies to the private join message only. First-seen
state is memory-only, preserved across `/omv reload`, and reset when the proxy
process restarts.

## Leave

`messages.leave.broadcast` is sent after a disconnect. The adapter supplies the
post-leave online count, so `{online}` means players remaining on the proxy.

## Empty Templates

An empty configured template disables that message path. Missing locale values
fall back through the localization contract.

## Reload

Reload validates templates before swapping config. Existing memory-only
first-seen state remains unless the proxy process restarts.
