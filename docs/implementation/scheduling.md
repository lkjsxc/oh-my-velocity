# Scheduling

Restart scheduling is a runtime adapter with isolated decision logic.

## Adapter Inputs

- Current epoch milliseconds.
- Persisted next restart epoch.
- Random jitter milliseconds.
- Delivered warning thresholds.

## Scheduler Decisions

- Idle decision.
- Warning decision with threshold minutes.
- Restart decision.
- Next immutable schedule state.

The adapter persists state after decisions and before shutdown when state
changes. Future work may split these decisions into a dedicated pure planner if
the scheduler grows.
