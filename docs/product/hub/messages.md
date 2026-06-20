# Hub Messages

Hub messages use the same locale fallback as proxy messages:
the shipped default config defines only `en`, so defaults are English for every
player.

1. Exact locale tag.
2. Language tag.
3. `en`.
4. First configured value.

Supported placeholders:

- `{server}` — configured target server.
- `{player}` — player username when available.

Unknown placeholders remain unchanged.
