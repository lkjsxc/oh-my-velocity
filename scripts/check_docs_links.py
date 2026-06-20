#!/usr/bin/env python3
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1] / "docs"


def children(directory: Path):
    return sorted(
        [p for p in directory.iterdir() if not p.name.startswith(".")],
        key=lambda p: p.name,
    )


def main() -> int:
    failures: list[str] = []
    for directory in [ROOT, *sorted([p for p in ROOT.rglob("*") if p.is_dir()])]:
        readme = directory / "README.md"
        if not readme.exists():
            failures.append(f"missing README.md: {directory.relative_to(ROOT)}")
            continue

        readmes = [p for p in directory.glob("README.md")]
        if len(readmes) != 1:
            failures.append(f"expected one README.md: {directory.relative_to(ROOT)}")

        text = readme.read_text(encoding="utf-8")
        for child in children(directory):
            if child.name == "README.md":
                continue
            if child.is_dir() and child.name not in text:
                failures.append(f"{readme.relative_to(ROOT)} missing child dir {child.name}")
            if child.is_file() and child.suffix == ".md" and child.name not in text:
                failures.append(f"{readme.relative_to(ROOT)} missing child file {child.name}")

    if failures:
        for failure in failures:
            print(failure)
        return 1

    print("docs links ok")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
