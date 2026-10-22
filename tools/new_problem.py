#!/usr/bin/env python3
"""Scaffold a new problem folder at the END of a level (existing folders never move).

Usage:
  python3 tools/new_problem.py --level 2 --app Spotify --slug fair-shuffle --title "Fair shuffle" \
      --concept "Fisher-Yates shuffle, fairness constraints" --class FairShuffle

Then fill in PROBLEM.md, CONCEPTS.md, HINTS.md, the starter and the tests, and add the row to ROADMAP.md.
Read CONTRIBUTING.md first.
"""
import argparse
import os
import re
import shutil

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
LEVEL_DIRS = {0: "level-0-foundations", 1: "level-1-warmup", 2: "level-2-building-blocks",
              3: "level-3-realistic", 4: "level-4-hard", 5: "level-5-boss"}
LEVEL_NAMES = {0: "Foundations", 1: "Warm-up", 2: "Building blocks", 3: "Realistic", 4: "Hard", 5: "Boss"}


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--level", type=int, required=True, choices=range(0, 6))
    ap.add_argument("--app", required=True)
    ap.add_argument("--slug", required=True, help="kebab-case, e.g. fair-shuffle")
    ap.add_argument("--title", required=True)
    ap.add_argument("--concept", default="TODO")
    ap.add_argument("--class", dest="cls", default="Solution", help="main Kotlin class name")
    a = ap.parse_args()

    if not re.fullmatch(r"[a-z0-9]+(-[a-z0-9]+)*", a.slug):
        raise SystemExit("slug must be kebab-case")
    level_dir = os.path.join(ROOT, "levels", LEVEL_DIRS[a.level])
    os.makedirs(level_dir, exist_ok=True)
    existing = [d for d in os.listdir(level_dir) if re.match(r"\d\d-", d)]
    app_slug = re.sub(r"[^a-z0-9]+", "-", a.app.lower()).strip("-")
    for d in existing:
        if d.split("-", 1)[1] == f"{app_slug}-{a.slug}":
            raise SystemExit(f"already exists: {d}")
    number = max([int(d[:2]) for d in existing] + [0]) + 1
    folder = os.path.join(level_dir, f"{number:02d}-{app_slug}-{a.slug}")
    module = f"l{a.level}-{app_slug}-{a.slug}"

    os.makedirs(os.path.join(folder, "src", "main", "kotlin"))
    os.makedirs(os.path.join(folder, "src", "test", "kotlin"))
    t = os.path.join(ROOT, "templates")
    with open(os.path.join(t, "PROBLEM.md"), encoding="utf-8") as f:
        problem = f.read().replace("{title}", a.title).replace("{app}", a.app) \
            .replace("{level}", f"{a.level} — {LEVEL_NAMES[a.level]}").replace("{concept}", a.concept)
    problem += f"\n---\n\n## Run the tests\n\n```bash\n./gradlew :{module}:test\n```\n"
    with open(os.path.join(folder, "PROBLEM.md"), "w", encoding="utf-8") as f:
        f.write(problem)
    for name in ("NOTES.md", "REVIEW.md", "HINTS.md"):
        with open(os.path.join(t, name), encoding="utf-8") as f:
            text = f.read().replace("{title}", a.title)
        with open(os.path.join(folder, name), "w", encoding="utf-8") as f:
            f.write(text)
    with open(os.path.join(folder, "CONCEPTS.md"), "w", encoding="utf-8") as f:
        f.write(f"# Concepts — {a.title}\n\nBackground you need for this problem. It explains *how things work* — "
                "it does not solve the problem for you.\n\n## Concept name\n\nExplain it here.\n")
    with open(os.path.join(folder, "src", "main", "kotlin", f"{a.cls}.kt"), "w", encoding="utf-8") as f:
        f.write(f"class {a.cls} {{\n\n    fun example(): Int {{\n        TODO()\n    }}\n}}\n")
    with open(os.path.join(folder, "src", "test", "kotlin", f"{a.cls}Test.kt"), "w", encoding="utf-8") as f:
        f.write("import kotlin.test.Test\nimport kotlin.test.assertEquals\n\n"
                f"class {a.cls}Test {{\n\n    @Test\n    fun example() {{\n        assertEquals(42, {a.cls}().example())\n    }}\n}}\n")
    print(f"created {os.path.relpath(folder, ROOT)}  (module :{module})")
    print("next: write PROBLEM.md, CONCEPTS.md, HINTS.md, starter + tests; add a row to ROADMAP.md and CATALOG.md")


if __name__ == "__main__":
    main()
