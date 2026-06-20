# Tab-List Behavior

## Scope

The shipped tab-list feature is native-only:

- Proxy-wide online player rows for every viewer.
- Per-viewer header and footer.
- Per-subject display name.
- Per-subject latency display.
- Per-subject list order.
- Group ordering based on permissions.

## Rendering

Header and footer render from the viewer context. Display names render from the
subject context. Supported placeholders are `{player}`, `{server}`, `{online}`,
`{max}`, `{date}`, and `{time}` plus `%player%` and `%server%` aliases.

## Player Rows

Velocity backend servers normally provide rows only for players visible from the
same backend connection. This plugin adds missing rows through Velocity's native
tab-list API so each viewer sees every online proxy player.

Rows added by this plugin are tracked per viewer and removed when the subject
leaves the proxy or the feature is disabled. Existing backend-provided rows are
updated in place and are not removed as plugin-managed rows.

## Group Ordering

Groups are evaluated in configured order. A player matches the first permission
found from:

- `ohmyvelocity.group.<group>`
- `group.<group>`

Unmatched players use the last configured group or `default`.

## Reload

Disabling the feature clears header and footer for online players when the
Velocity API allows it. Re-enabling applies the next scheduled update.
