# Hub Messages

Hub messages use the same locale fallback as proxy messages:

1. Exact locale tag.
2. Language tag.
3. `en`.
4. First configured value.

Supported placeholders:

- `{server}` — configured target server.
- `{player}` — player username when available.

Unknown placeholders remain unchanged.
