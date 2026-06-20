# Domain

The domain layer turns immutable inputs into immutable plans.

## Rules

- Prefer records for config, context, and plan objects.
- Use `List.copyOf`, `Map.copyOf`, and deep copies for nested config.
- Use `Optional` for absent values unless an empty string has documented
  meaning.
- Pass current time, random selections, player counts, and locale values in from
  adapters.
- Return precise validation errors instead of silently accepting invalid config.

## Planner Shape

Planners should look like:

```text
plan(config, context) -> plan/result
```

Adapters execute the returned plan. Domain planners do not send messages,
connect players, read files, schedule tasks, or shut down the proxy.
