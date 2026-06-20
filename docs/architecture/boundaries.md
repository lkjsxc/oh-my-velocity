# Boundaries

Target package ownership:

```text
domain       pure records and planners
adapter      Velocity, filesystem, scheduling, process, random, and clock effects
plugin       composition root and registrations
support      small reusable helpers
```

## Domain Must Not

- Import Velocity, Adventure, SLF4J, or scheduler types.
- Read or write files.
- Execute processes.
- Read wall-clock time or random values.
- Store hidden mutable service state.

## Adapters Own

- Velocity event listeners and commands.
- MiniMessage parsing into Components.
- Proxy ping and tab-list application.
- Server connection requests.
- Scheduler lifecycle and process shutdown.
- Config file IO and state persistence.

Existing effectful code may live in `adapter`, `feature`, or `command`
packages. Treat all three as adapter surfaces; new effectful code should prefer
`adapter` unless moving an existing class would create churn without changing
behavior.
