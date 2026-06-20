# Operations Verification

Use this checklist before installing an updated JAR on a server.

## Local Build Gate

```sh
docker compose -f docker-compose.verify.yml run --rm verify
```

The Docker gate is authoritative because it uses the repository-defined runtime
instead of the operator's host Gradle cache.

## Clean Proxy Gate

Before adopting a new build, test it on a clean Velocity proxy:

- Only Velocity and this plugin are installed.
- Default config is generated.
- `/omv reload` succeeds with the generated config.
- `/hub` reports the configured disabled or unavailable state.
- Server-list MOTD and native tab-list presentation render.

Do not claim integration coverage unless Velocity is actually started.
