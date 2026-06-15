# Source Layout Contract

## Java Packages

- `com.ohmyvelocity.plugin` — entry, bootstrap, registries
- `com.ohmyvelocity.domain` — pure logic, no Velocity API
- `com.ohmyvelocity.command` — Brigadier commands
- `com.ohmyvelocity.feature.join` — join message listener
- `com.ohmyvelocity.feature.restart` — restart scheduler

## Resources

- `config.yml`, `lang/en.json`
- `velocity-plugin.json` is generated at build time from `@Plugin`

## Tests

`src/test/java` mirrors `domain` ownership.
