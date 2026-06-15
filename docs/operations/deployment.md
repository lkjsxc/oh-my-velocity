# Deployment

## Build

```sh
./gradlew jar
```

Artifact: `build/libs/oh-my-velocity-0.1.0.jar`

## Install

Copy the JAR to `plugins/` on a Velocity 3.5+ proxy.

Live home-server deployment notes (paths, backup, rollback) are recorded in the
workspace private docs at
`private/docs/operations/infrastructure/oh-my-velocity.md`.

## Config

Edit `plugins/ohmyvelocity/config.yml` after first start.

## Permissions

- `ohmyvelocity.admin` — `/omv` commands

## Verify

```sh
docker compose -f docker-compose.verify.yml run --rm verify
```
