# Adapters

Adapters translate external state into domain inputs and execute domain plans.

## Velocity Adapters

- Listen for proxy events.
- Convert Components through MiniMessage.
- Apply native tab-list changes.
- Build server-list ping responses.
- Resolve registered servers and connection results.

## Runtime Adapters

- Read and write config files.
- Persist restart state.
- Supply current time and random values.
- Register scheduler tasks.
- Execute external hooks.
- Call Velocity shutdown.

Adapter classes may hold mutable runtime state when the state represents an
external resource or lifecycle concern.
