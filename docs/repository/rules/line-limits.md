# Line Limits Contract

## Hard Limits

- Docs: `<= 200` lines for `README.md`, `CHANGELOG.md`, and every `docs/**/*.md`
- Source: `<= 200` lines for Java under `src/main/java` and `src/test/java`
- Scripts: `<= 200` lines for `scripts/*.py`

## Headroom

Prefer files below 180 lines. Split when a file approaches 190 lines.

## Split Rule

Split by ownership, not arbitrary line count. One class or contract per file
when possible.

## Enforcement

`scripts/check_lines.py` is mandatory in `scripts/verify.sh`.
