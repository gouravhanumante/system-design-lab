# Continue watching

| App | Level | Concept |
|-----|-------|---------|
| Netflix | 1 — Warm-up | Per-profile state, recency ordering, completion threshold |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Stop an episode halfway and Netflix remembers exactly where you were — per **profile**, not per account. The "Continue Watching" row shows your most recently watched titles first, and a title disappears once you've (almost) finished it — nobody wants to resume the end credits.

## Build this (small scale)

Track playback positions and build the Continue Watching row.

## API

```kotlin
class ContinueWatching(finishedThreshold: Double = 0.95) {
    fun progress(profile: String, titleId: String, positionSec: Int, durationSec: Int, now: Long)
    fun resumePosition(profile: String, titleId: String): Int
    fun row(profile: String, limit: Int): List<String>
    fun remove(profile: String, titleId: String)
}
```

## Rules

- `progress` records the latest position for that profile + title, and `now` as the time it was last watched. A later `progress` call overwrites the earlier one (even if the position is smaller — the user rewound).
- A title is **finished** when `positionSec >= finishedThreshold × durationSec`. A finished title is not in the row and its resume position is `0`. Watching it again later (a new `progress` below the threshold) brings it back.
- `resumePosition` → the saved position, or `0` if unknown or finished.
- `row` → unfinished titles, most recently watched first (larger `now` first; ties by title id ascending), at most `limit`.
- `remove` ("remove from row") forgets the title for that profile.
- Profiles never share data.

## Example

Profile `kid` watches `bluey` to 300 / 420 s at time 10, `pingu` to 60 / 300 s at time 20 → row `[pingu, bluey]`. Then `bluey` to 410 / 420 (≥ 95 %) → row `[pingu]`.

## Think about (answer in NOTES.md)

- Players send progress every few seconds for millions of viewers. Which store would you use, and do you need every update to be saved?
- You watch on the TV and continue on the phone two minutes later. What if the TV's last update arrives *after* the phone's first one?

---

## Run the tests

```bash
./gradlew :l1-netflix-continue-watching:test
```

Your code goes in `src/main/kotlin/ContinueWatching.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
