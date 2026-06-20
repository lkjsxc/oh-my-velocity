# Event Flow

## Startup

`ProxyInitializeEvent` → bootstrap services → register listeners and commands
→ start restart and tab schedulers.

## Join

`ServerConnectedEvent` without previous server → `JoinMessageListener` →
`JoinMessageService` → send player and broadcast components.

## Leave

`DisconnectEvent` → `JoinMessageListener` → `JoinMessageService` → broadcast
leave component.

## MOTD

`ProxyPingEvent` → MOTD listener → weighted entry render → ping response.

## Tab

Scheduler and server-switch events → tab listener → header/footer, list entry,
team, and ping objective updates.

## Restart Tick

Scheduler (1 minute) → `RestartScheduleService.tick()` → warnings or shutdown.

## Shutdown

`ProxyShutdownEvent` → cancel tasks, clear restarting flag.

## Reload

`/omv reload` → reload config → reschedule restart and tab tasks from
persisted state.
