# Deployment

## Requirements

- Velocity 3.5+.
- Java 21.
- A host supervisor if scheduled restarts are enabled.
- No other Minecraft or Velocity plugin dependency.

## Build

```sh
./gradlew jar
```

Copy `build/libs/oh-my-velocity-*.jar` into Velocity's `plugins/` folder.

## First Start

Start Velocity once to create:

```text
plugins/ohmyvelocity/config.yml
plugins/ohmyvelocity/restart-state.yml
```

Stop Velocity, edit the config, then start it again. The plugin should load in a
clean Velocity proxy with no extra plugin dependencies.

## Replacement

Remove older plugin JARs that provide the same commands or listeners. Keep one
active `oh-my-velocity` JAR in the `plugins/` directory.
