# Verification Gates

## Local

```sh
./scripts/verify.sh
```

## Docker

```sh
docker compose -f docker-compose.verify.yml run --rm verify
```

## Gates

1. Gradle `clean test jar`
2. Docs topology (`validate_docs_topology.py`)
3. Line limits (`check_lines.py`)
