# Dependency Removal

The plugin must run on a clean Velocity proxy.

## Required Removals

- No third-party plugin dependency in build files.
- No third-party plugin dependency in plugin metadata.
- No docs instructing operators to install another plugin.
- No source imports from non-native scoreboard packages.
- No config keys for unsupported scoreboard-team or ping-objective behavior.

## Verification

The forbidden dependency script fails if removed dependency strings reappear in
active docs, build files, plugin metadata, source, or tests.
