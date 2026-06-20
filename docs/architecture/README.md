# Architecture

The architecture keeps product decisions in pure domain planners and moves all
effects to adapters. This is the main maintainability boundary for future LLM
changes.

## Index

- [boundaries.md](boundaries.md) — package ownership and allowed dependencies.
- [domain.md](domain.md) — pure planner rules.
- [adapters.md](adapters.md) — Velocity and runtime effect adapters.
- [lifecycle.md](lifecycle.md) — plugin startup, reload, shutdown, and scheduling.
- [error-handling.md](error-handling.md) — validation, logging, and failure behavior.
