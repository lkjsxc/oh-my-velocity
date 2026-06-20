# MOTD, Tab, And Hub

`oh-my-velocity` owns the proxy MOTD, tab list presentation, proxy join/leave
messages, and `/hub` command.

## MOTD

`motd.enabled` controls `ProxyPingEvent` handling. Entries are selected by
weight and rendered with MiniMessage. Supported placeholders are `{player}`,
`{online}`, `{max}`, `{server}`, `{ping}`, `{date}`, and `{time}`.

`motd.max-players` defaults to `128`. `motd.hover` becomes server-list sample
players. `motd.hide-player-count` removes the count from the ping response.
`motd.target-servers` matches raw virtual hosts; `*` matches all.

## Tab

`tab.enabled` controls periodic header/footer, display-name, list-order,
scoreboard team, and ping objective updates.

`tab.display-name-format` accepts MiniMessage plus legacy `&` or `§` color
codes. `%player%`, `%online%`, `%server%`, and `%ping%` aliases are accepted
for imported TAB-style values.

Group sorting uses `tab.group-order`. A player matches the first group for
which they have `ohmyvelocity.group.<group>` or `group.<group>` permission.

## Proxy Messages

`proxy-messages` replaces the legacy `join-messages` section. Locale maps
fall back from exact tag to language, then `en`, then the first configured
value. The old `join-messages` keys remain supported as a fallback.

## Hub

`hub-command.enabled` registers `/hub` behavior against
`hub-command.target-server`, default `hub`. Messages are localized with the
same fallback rules as proxy messages.
