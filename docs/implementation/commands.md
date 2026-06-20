# Commands

Command classes are adapters. They translate Velocity command sources into
domain contexts and execute returned plans.

## `/omv`

- `/omv reload` reloads config and reports success or validation failure.
- `/omv restart status` reports the next scheduled restart state.
- `/omv restart now` triggers manual restart when enabled.

## `/hub`

`/hub` remains registered when disabled so it can return a configured disabled
message. It has no permission by default and no subcommands.
