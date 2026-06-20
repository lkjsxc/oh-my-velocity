# Verification Gates

`scripts/verify.sh` must run these gates:

- Gradle clean test jar.
- Docs topology.
- Docs link/index consistency.
- Line limits.
- Forbidden dependency scan.
- Architecture boundary scan.
- Forbidden planning terminology scan.

## Required Command

```sh
docker compose -f docker-compose.verify.yml run --rm verify
```

Host-only commands may be useful while editing, but Docker Compose is the
acceptance evidence.
