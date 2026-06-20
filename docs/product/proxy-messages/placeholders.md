# Proxy Message Placeholders

Supported placeholders:

- `{player}` — player username.
- `{online}` — current online player count for the event.
- `{max}` — configured visible max player count.
- `{server}` — current backend server name, or `proxy` when unavailable.

Unknown placeholders remain unchanged. This makes typos visible to operators
instead of silently deleting text.

Both `{name}` and `%name%` forms are accepted for the supported keys to ease
format reuse in tab-list templates.
