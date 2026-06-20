# Documentation

This tree is the source of truth for shipped behavior. Update the relevant
contract before changing code, then make the implementation match the contract.

## Index

- [project](project/README.md) — mission, dependency policy, LLM maintenance.
- [product](product/README.md) — feature behavior and configuration contracts.
- [architecture](architecture/README.md) — pure domain and effectful adapter boundaries.
- [implementation](implementation/README.md) — implementation notes for config, rendering, scheduling, and commands.
- [operations](operations/README.md) — deployment and supervisor expectations.
- [verification](verification/README.md) — required gates and manual smoke checks.
- [work](work/README.md) — current focus, capability map, dependency-removal work.

## Contract Rules

- Active docs describe only shipped behavior.
- Research-only ideas must be labeled outside the active product contract.
- No active doc may require another Minecraft or Velocity plugin.
- Each docs directory has exactly one `README.md` index.
- Markdown files stay at or below 200 lines.
- Docker Compose verification is the acceptance gate.
