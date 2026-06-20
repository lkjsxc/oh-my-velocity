# Restart Behavior

## Schedule State

The runtime adapter stores the next restart epoch in
`plugins/ohmyvelocity/restart-state.yml`. The planner receives persisted state,
current time, and random jitter as inputs.

## Scheduled Restart

When `restart.schedule-enabled` is true, the planner schedules the next restart
from `interval-hours` plus optional jitter. Warnings are sent once per configured
threshold before shutdown.

## Manual Restart

Manual restart is controlled by `restart.manual-command-enabled`. It works even
when scheduled restarts are disabled.

## Hook and Shutdown

When `restart.mode` is `external_hook`, the runtime adapter executes
`external-hook` with the configured timeout before Velocity shutdown. Hook
failure is logged and shutdown continues.

## Reload

Reload revalidates config and reschedules when schedule settings change.
Delivered warnings are preserved only when the next restart epoch is unchanged.
