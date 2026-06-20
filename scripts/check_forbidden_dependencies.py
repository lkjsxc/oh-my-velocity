#!/usr/bin/env python3
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]
SCAN_ROOTS = [
    ROOT / "README.md",
    ROOT / "build.gradle.kts",
    ROOT / "settings.gradle.kts",
    ROOT / "docs",
    ROOT / "scripts",
    ROOT / "src" / "main",
    ROOT / "src" / "test",
]

FORBIDDEN = [
    "Velocity" + "ScoreboardAPI",
    "velocity" + "scoreboardapi",
    "velocity-" + "scoreboard-api",
    "net." + "william278",
    "com.velocitypowered.api." + "scoreboard",
    "Scoreboard" + "Manager",
    "Proxy" + "Scoreboard",
    "Proxy" + "Objective",
]


def iter_files(path: Path):
    if path.is_file():
        yield path
        return
    if not path.exists():
        return
    for child in path.rglob("*"):
        if child.is_file() and ".git" not in child.parts:
            yield child


def main() -> int:
    failures: list[str] = []
    for root in SCAN_ROOTS:
        for path in iter_files(root):
            try:
                lines = path.read_text(encoding="utf-8").splitlines()
            except UnicodeDecodeError:
                continue
            for number, line in enumerate(lines, start=1):
                if any(token in line for token in FORBIDDEN):
                    rel = path.relative_to(ROOT)
                    failures.append(f"{rel}:{number}: forbidden dependency reference")

    if failures:
        for failure in failures:
            print(failure)
        return 1

    print("forbidden dependency scan ok")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
