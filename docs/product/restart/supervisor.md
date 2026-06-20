# Restart Supervisor Contract

The plugin stops the proxy process; it does not restart Java.

## Required Host Behavior

- Restart Velocity after normal process exit.
- Preserve the plugin data directory.
- Avoid running duplicate Velocity processes.

## Safe Manual Use

Run `/omv restart now` only in a controlled environment where a supervisor is
known to restart Velocity. Without a supervisor, the proxy remains stopped until
an operator starts it.

## External Hook

`restart.external-hook` is an admin-configured shell command. Output is
discarded to prevent pipe backpressure from stalling shutdown, and non-zero exit
codes are logged before shutdown continues.
