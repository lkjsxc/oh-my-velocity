# Tab-List Behavior

## Scope

The shipped tab-list feature is native-only:

- Per-viewer header and footer.
- Per-subject display name.
- Per-subject latency display.
- Per-subject list order.
- Group ordering based on permissions.

## Rendering

Header and footer render from the viewer context. Display names render from the
subject context. Supported placeholders are `{player}`, `{server}`, `{online}`,
`{max}`, `{date}`, and `{time}` plus `%player%` and `%server%` aliases.

## Group Ordering

Groups are evaluated in configured order. A player matches the first permission
found from:

- `ohmyvelocity.group.<group>`
- `group.<group>`

Unmatched players use the last configured group or `default`.

## Reload

Disabling the feature clears header and footer for online players when the
Velocity API allows it. Re-enabling applies the next scheduled update.
