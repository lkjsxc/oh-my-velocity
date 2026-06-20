# Proxy Message Localization

Messages are locale maps keyed by BCP 47 tags such as `en` or `en-US`.
The shipped default config defines only `en`, so default runtime messages are
English for every player. Operators may add more locale keys later.

## Fallback Order

1. Exact locale tag from the player.
2. Language part from the player locale.
3. `en`.
4. First configured value in insertion order.

If the player locale is unavailable, fallback starts at `en`.

## Reload

Reload replaces configured locale maps after validation. It does not persist or
reload first-seen player state.
