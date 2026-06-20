# Scheduling

Restart scheduling is a runtime adapter around a pure planner.

## Adapter Inputs

- Current epoch milliseconds.
- Persisted next restart epoch.
- Random jitter milliseconds.
- Delivered warning thresholds.

## Planner Outputs

- Idle decision.
- Warning decision with threshold minutes.
- Restart decision.
- Next immutable schedule state.

The adapter persists state after planner decisions and before shutdown when
state changes.
