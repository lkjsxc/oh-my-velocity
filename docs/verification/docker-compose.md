# Docker Compose Verification

Run:

```sh
docker compose -f docker-compose.verify.yml run --rm verify
```

The `verify` service runs the repository script in a clean container. This
guards against hidden host dependencies and stale Gradle state.

## Expected Result

The command exits zero and prints `verify complete`.

If it fails, fix the first failing gate and rerun the same command.
