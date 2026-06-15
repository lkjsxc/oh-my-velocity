# Event Flow

## Startup

`ProxyInitializeEvent` → bootstrap services → register listeners and commands
→ `RestartScheduler.start()`.

## Join

`ServerConnectedEvent` → `JoinMessageListener` → `JoinMessageService` → send
player and broadcast components.

## Restart Tick

Scheduler (1 minute) → `RestartScheduleService.tick()` → warnings or shutdown.

## Shutdown

`ProxyShutdownEvent` → cancel tasks, clear restarting flag.

## Reload

`/omv reload` → reload config → reschedule restart from persisted state.
