# Match many requests to many captains

| App | Level | Concept |
|-----|-------|---------|
| Rapido | 3 — Realistic | Global greedy matching vs first-come-first-served |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

At a busy moment, 200 riders and 150 captains are waiting in the same area. If you serve riders one by one ("each rider takes the closest captain"), early riders can steal captains that would have been a *perfect* match for someone else, and pickup times get worse overall. Real dispatch systems collect requests for a few seconds and match them **together** in a batch.

## Build this (small scale)

Given all waiting riders and all free captains, produce the matching using this rule: repeatedly take the **globally closest** (rider, captain) pair, lock it in, remove both, and continue.

## API

```kotlin
data class Point(val id: String, val x: Int, val y: Int)

fun matchBatch(riders: List<Point>, captains: List<Point>): Map<String, String>   // riderId -> captainId
```

## Rules

- Distance is squared Euclidean (`dx² + dy²`).
- Among pairs with the same distance, prefer the smaller rider id, then the smaller captain id.
- If there are more riders than captains (or the reverse) the extra ones stay unmatched: riders without a captain are simply **missing** from the returned map.
- Inputs are not modified. Empty input → empty map.

## Example

Riders `R1(0,0)`, `R2(4,0)`; captains `C1(3,0)`, `C2(-5,0)`.
First-come-first-served gives `R1→C1`, `R2→C2` (total squared distance 9 + 81 = 90).
The global greedy rule picks the pair `(R2, C1)` first (distance 1), leaving `R1→C2` (25). Total 26.

## Think about (answer in NOTES.md)

- Is the global-greedy answer always the *best possible* total? (Look up the Hungarian algorithm.) Why do real systems often accept "good enough"?
- Your solution looks at every rider × captain pair. What would you do to avoid that when both lists are huge?

---

## Run the tests

```bash
./gradlew :l3-rapido-batch-matching:test
```

Your code goes in `src/main/kotlin/BatchMatching.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
