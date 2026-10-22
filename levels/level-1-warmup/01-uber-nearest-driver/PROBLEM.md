# Find the nearest drivers

| App | Level | Concept |
|-----|-------|---------|
| Uber | 1 — Warm-up | Brute-force scan, sorting, tie-breaking |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

You open Uber and drop a pin. Within a second the app has to know which free drivers are closest to you. Real systems never scan every driver on earth, but every one of them starts from this question: *"who is nearest?"*

## Build this (small scale)

A handful of drivers stand on a flat 2D map. Given a rider's position, return the ids of the `k` nearest drivers, nearest first.

## API

```kotlin
data class Driver(val id: String, val x: Int, val y: Int)

class DriverFinder(drivers: List<Driver>) {
    fun nearest(x: Int, y: Int, k: Int): List<String>
}
```

## Rules

- Distance is the **squared** Euclidean distance `dx² + dy²` (no square roots needed).
- Nearest first. If two drivers are equally far, the smaller id (alphabetical) comes first.
- If there are fewer than `k` drivers, return all of them. `k = 0` returns an empty list.
- Never modify the list you were given.

## Example

Drivers `A(0,0)`, `B(3,4)`, `C(1,1)`. A rider at `(0,0)` asking for `k = 2` gets `["A", "C"]`.

## Think about (answer in NOTES.md)

- What is the time complexity per query? What if there are 1 million drivers and 10,000 queries per second?
- You only need the top `k`, not a full sort. Is there a cheaper way?

---

## Run the tests

```bash
./gradlew :l1-uber-nearest-driver:test
```

Your code goes in `src/main/kotlin/NearestDriver.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
