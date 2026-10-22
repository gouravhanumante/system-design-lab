# Adding problems (rules for humans **and** AI assistants)

This repo grows over time. These rules keep it consistent and stop it from filling up with near-duplicate problems.

## 0. Before writing anything: check the registry

1. Open **[ROADMAP.md](ROADMAP.md)**. Every problem — built or planned — has exactly one row with its **core concept**.
2. Search **[CONCEPTS.md](CONCEPTS.md)** for the concept you want to teach.
3. A new problem must teach a **new core concept**, or a clearly **new twist** on an existing one (write the twist in the ROADMAP row). "Rate limiter again, but for a different app" is not allowed; "rate limiting across 50 servers with a shared store" might be.

## 1. Where new problems go

- A new app gets **one problem per level** (1–5), or a single foundations problem in Level 0.
- New folders are always **appended at the end of the level** (`13-…`, `14-…`). Never renumber or move existing folders — people's solved work lives in them.
- Use the scaffolder: `python3 tools/new_problem.py --level 2 --app Spotify --slug shuffle-fairness --title "Fair shuffle"`. It picks the next number and copies the templates.
- Gradle discovers modules automatically (`settings.gradle.kts`). Module name = `l<level>-<folder name without the number>`.

## 2. What every problem folder must contain

| File | Content |
|------|---------|
| `PROBLEM.md` | Header table (App, Level, Concept) · **The real-world situation** · **Build this (small scale)** · **API** (Kotlin signatures) · **Rules** (complete and unambiguous — every rule tested) · **Example** · **Think about** (3 scale/trade-off questions) · **Run the tests** |
| `CONCEPTS.md` | 3–6 `## Concept name` sections explaining *how things work*, never the solution. Reuse existing concept names from CONCEPTS.md where the idea is the same, so the index groups them |
| `HINTS.md` | Exactly 3 hints (nudge → data structure → approach) + 1 scale-up thought, each in `<details>` |
| `src/main/kotlin/*.kt` | The public API with `TODO()` bodies. Provided helpers (hash functions, crypto helpers, interfaces) are allowed and marked `Provided` |
| `src/test/kotlin/*Test.kt` | `kotlin.test` tests. Deterministic: inject clocks (`now` parameters) and randomness; seeded `Random` only. Each test name states the rule it checks |
| `NOTES.md`, `REVIEW.md` | Copied unchanged from `templates/` |

## 3. Quality bar for tests

- Every rule in `PROBLEM.md` is covered by at least one test, and every test follows from the written rules (no hidden requirements).
- With the `TODO()` starter, **every test must fail** (no test passes by accident).
- A correct reference solution must exist and pass all tests **before** the problem is committed — but the reference solution is **never committed** to this repo.
- Include boundary tests (`== timeout`), duplicate/retry tests, and at least one randomised test against a brute-force answer where it makes sense.

## 4. Rules for AI assistants working in this repo

- **Never write or complete solution code in `src/main` of a problem the owner is solving**, unless explicitly asked for that specific problem. Explaining concepts, reviewing finished work and writing new problems are fine.
- When reviewing, follow [RATING.md](RATING.md) and fill in only the *Reviewer* parts of `REVIEW.md`.
- When adding problems, follow sections 0–3 above and add the ROADMAP row in the same change.
