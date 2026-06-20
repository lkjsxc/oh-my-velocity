#!/usr/bin/env python3
from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[1]
SCAN_ROOTS = [ROOT / "README.md", ROOT / "docs"]
FORBIDDEN = re.compile(r"\bphase\b|\bphases\b|roadmap|\bv[0-9]+(?:\.[0-9]+)*\b", re.I)


def iter_markdown(path: Path):
    if path.is_file() and path.suffix == ".md":
        yield path
        return
    for child in path.rglob("*.md"):
        yield child


def main() -> int:
    failures: list[str] = []
    for root in SCAN_ROOTS:
        for path in iter_markdown(root):
            for number, line in enumerate(path.read_text(encoding="utf-8").splitlines(), start=1):
                if FORBIDDEN.search(line):
                    rel = path.relative_to(ROOT)
                    failures.append(f"{rel}:{number}: forbidden planning term")

    if failures:
        for failure in failures:
            print(failure)
        return 1

    print("planning terminology ok")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
