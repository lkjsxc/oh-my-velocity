#!/usr/bin/env bash
set -euo pipefail

echo "=== gradle clean test jar ==="
./gradlew --no-daemon clean test jar

echo "=== docs topology ==="
python3 scripts/validate_docs_topology.py

echo "=== docs links ==="
python3 scripts/check_docs_links.py

echo "=== line limits ==="
python3 scripts/check_lines.py

echo "=== forbidden dependencies ==="
python3 scripts/check_forbidden_dependencies.py

echo "=== architecture boundaries ==="
python3 scripts/check_architecture_boundaries.py

echo "=== planning terminology ==="
python3 scripts/check_forbidden_planning_terms.py

echo "=== verify complete ==="
