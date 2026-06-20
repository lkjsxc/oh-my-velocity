# Hub Behavior

## Command Scope

`/hub` is registered even when disabled so it can return a configured disabled
message. The command has no permission by default.

## Result States

- Non-player source receives `players-only`.
- Disabled command receives `disabled`.
- Missing target server receives `unavailable`.
- Player already connected to target receives `already-connected`.
- Valid target sends `connecting`, then `connected` or `failed` based on the
  Velocity connection result.

## Suggestions

`/hub` has no subcommands, so suggestions are empty.
