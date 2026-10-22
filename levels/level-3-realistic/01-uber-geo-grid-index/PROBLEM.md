# Grid index for nearby drivers

| App | Level | Concept |
|-----|-------|---------|
| Uber | 3 — Realistic | Spatial index, bucketing, avoiding full scans |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Millions of drivers send their location every few seconds. Scanning all of them for every ride request is impossible. Uber-style systems split the map into **cells** (a grid, or geohash / H3 hexagons) and only look at the cells near the rider.

## Build this (small scale)

A grid index that stores driver positions, lets drivers move, and answers "who is within `radius` of this point?" while only looking at nearby cells.

## API

```kotlin
class GridIndex(cellSize: Int) {
    fun update(driverId: String, x: Int, y: Int)          // add a driver, or move an existing one
    fun remove(driverId: String)                          // unknown id: do nothing
    fun withinRadius(x: Int, y: Int, radius: Int): List<String>
    val lastScanCount: Int                                // see below
}
```

## Rules

- A cell is a square of `cellSize × cellSize`. The cell of `(x, y)` is `(floorDiv(x, cellSize), floorDiv(y, cellSize))` — **negative coordinates must work**.
- `withinRadius` returns drivers with `dx² + dy² <= radius²` (the boundary counts), nearest first, ties by id.
- `update` on an existing driver **moves** it: it must disappear from its old cell.
- `lastScanCount` = how many drivers you computed a distance for during the most recent `withinRadius` call. The tests check that this stays **far below the total number of drivers** — that is how we know your grid is doing its job.

## Example

`cellSize = 10`. Driver `A` at `(5, 5)` is in cell `(0, 0)`; `B` at `(-1, 12)` is in cell `(-1, 1)`.

## Think about (answer in NOTES.md)

- How do you pick `cellSize`? What goes wrong if it is far too small or far too large?
- What happens in a very busy cell (a stadium after a concert)?
- How would you shard this index across many servers?

---

## Run the tests

```bash
./gradlew :l3-uber-geo-grid-index:test
```

Your code goes in `src/main/kotlin/GridIndex.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
