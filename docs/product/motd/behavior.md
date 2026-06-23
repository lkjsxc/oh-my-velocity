# MOTD Behavior

## Host Matching

`motd.target-hosts` matches the virtual host from the ping request.

- `*` matches every host.
- Exact host matches are case-insensitive.
- Empty or unavailable host matches only `*`.
- Ports are ignored for matching.

## Entry Selection

Enabled MOTD entries have positive integer weights. The adapter supplies random
input, and the domain planner chooses one weighted entry.

## Placeholders

Supported placeholders are `{online}`, `{max}`, `{host}`, `{date}`, and
`{time}`. Unknown placeholders remain unchanged.

## Player Count Hover

`motd.max-players` sets the visible max player count. `hide-player-count` hides
the count when Velocity supports a hidden count response. The hover shows online
player names only. It shows up to 15 names and appends `and {n} more` when more
players are online.

## Reload

Reload validates templates, rebuilds MOTD entries, and clears cached icons.
