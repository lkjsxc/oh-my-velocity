#!/usr/bin/env bash
set -euo pipefail

echo "=== gradle test/build ==="
./gradlew --no-daemon clean test jar

echo "=== docs topology ==="
python3 scripts/validate_docs_topology.py

echo "=== line limits ==="
python3 scripts/check_lines.py

echo "=== verify complete ==="
