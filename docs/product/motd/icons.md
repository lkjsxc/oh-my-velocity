# MOTD Icons

`motd.entries[].icon` is an optional path to a server-list favicon.

## Path Rules

- Empty string means no icon.
- Relative paths resolve under the plugin data directory.
- Absolute paths are allowed only if the JVM can read them.

## Failure Behavior

If icon loading fails, the adapter logs a warning and sends the MOTD without a
favicon. A bad icon must not block the ping response.

## Cache

Loaded icons are cached by resolved path. `/omv reload` clears the cache so
updated files can be loaded.
