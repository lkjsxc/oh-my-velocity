# Mission

`oh-my-velocity` is a small, self-contained Velocity proxy plugin for one
network. It owns network messages, MOTD, native tab-list presentation, `/hub`,
reload, and restart scheduling without requiring other Minecraft or Velocity
plugins.

## Goals

- Keep the shipped product useful on a clean Velocity proxy.
- Make active docs precise enough for LLM agents to change behavior safely.
- Prefer smaller real features over larger unsupported promises.
- Keep domain logic pure enough to test without Velocity.

## Non-Goals

- No third-party plugin runtime contract.
- No compatibility promise for obsolete config schemas.
- No placeholder feature paths.
- No fake integration coverage.
