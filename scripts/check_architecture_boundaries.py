#!/usr/bin/env python3
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]
DOMAIN = ROOT / "src" / "main" / "java" / "com" / "ohmyvelocity" / "domain"

FORBIDDEN = [
    "import com.velocitypowered",
    "import net.kyori",
    "import org.slf4j",
    "import java.nio.file",
    "import java.io",
    "ProcessBuilder",
    "LocalDateTime.now",
    "System.currentTimeMillis",
]


def main() -> int:
    failures: list[str] = []
    if not DOMAIN.exists():
        print("domain boundary scan ok")
        return 0

    for path in sorted(DOMAIN.rglob("*.java")):
        for number, line in enumerate(path.read_text(encoding="utf-8").splitlines(), start=1):
            if any(token in line for token in FORBIDDEN):
                rel = path.relative_to(ROOT)
                failures.append(f"{rel}:{number}: domain boundary violation")

    if failures:
        for failure in failures:
            print(failure)
        return 1

    print("domain boundary scan ok")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
