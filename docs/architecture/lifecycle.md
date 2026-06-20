# Lifecycle

## Startup

The plugin composition root loads config, validates it, creates runtime stores,
wires adapters, and registers listeners plus commands.

## Reload

`/omv reload` loads config from disk, validates it, parses templates, clears
MOTD icon cache, and swaps active runtime config only after validation succeeds.

Invalid reload keeps the previous working config active and reports a clear
admin message.

## Scheduling

Runtime scheduling belongs outside domain. The scheduler asks domain restart
planning functions for decisions and then executes warning or shutdown effects.

## Shutdown

Restart shutdown must happen from runtime adapter code. Domain planners return a
restart decision; they do not call Velocity.
