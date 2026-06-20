# Error Handling

## Config Errors

Config loading should fail fast with precise paths, for example
`restart.interval-hours must be positive`.

## Runtime Errors

- Event handlers should log unexpected errors and avoid crashing Velocity.
- Invalid templates must be discovered during reload, not during player events.
- Failed MOTD icon loads log a warning and continue without a favicon.
- Failed external hooks log exit details when available and continue shutdown.

## Operator Messages

Admin command failures should be concise and actionable. Player-facing failures
come from configured localized messages.
