# Restart Supervisor

The plugin can request shutdown, but it does not start a new JVM. Scheduled and
manual restarts require a host process supervisor.

## Supervisor Options

- Docker Compose `restart: unless-stopped`.
- systemd `Restart=always`.
- A hosting panel restart policy.

## Plugin Responsibility

- Persist the next scheduled restart time.
- Broadcast configured warnings once per threshold.
- Run the configured external hook when `restart.mode` is `external_hook`.
- Call Velocity shutdown after the restart decision is reached.

## Host Responsibility

- Detect the stopped Java process.
- Start Velocity again.
- Preserve the plugin data directory between starts.
