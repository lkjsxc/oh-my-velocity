#!/usr/bin/env python3
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]
DOC_LIMIT = 200
SRC_LIMIT = 200


def count_lines(path: Path) -> int:
    return path.read_text(encoding="utf-8").count("\n") + 1


def iter_files(base: Path, suffixes: tuple[str, ...]):
    for path in base.rglob("*"):
        if path.is_file() and path.suffix in suffixes:
            yield path


def main() -> int:
    failures: list[str] = []
    for path in iter_files(ROOT / "docs", (".md",)):
        lines = count_lines(path)
        if lines > DOC_LIMIT:
            failures.append(f"docs limit exceeded: {path} ({lines}>{DOC_LIMIT})")

    for name in ("README.md", "CHANGELOG.md"):
        path = ROOT / name
        if path.exists():
            lines = count_lines(path)
            if lines > DOC_LIMIT:
                failures.append(f"docs limit exceeded: {path} ({lines}>{DOC_LIMIT})")

    for root in (ROOT / "src" / "main" / "java", ROOT / "src" / "test" / "java"):
        if not root.exists():
            continue
        for path in iter_files(root, (".java",)):
            lines = count_lines(path)
            if lines > SRC_LIMIT:
                failures.append(f"source limit exceeded: {path} ({lines}>{SRC_LIMIT})")

    for path in iter_files(ROOT / "scripts", (".py",)):
        lines = count_lines(path)
        if lines > SRC_LIMIT:
            failures.append(f"script limit exceeded: {path} ({lines}>{SRC_LIMIT})")

    if failures:
        for line in failures:
            print(line)
        return 1

    print("line limits ok")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
