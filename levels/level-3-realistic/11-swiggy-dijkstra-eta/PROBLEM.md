# Fastest route and ETA (Dijkstra)

| App | Level | Concept |
|-----|-------|---------|
| Swiggy | 3 — Realistic | Weighted graph, Dijkstra's shortest path, priority queue, road closures |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

"Your order will arrive in 27 minutes" starts with: what's the fastest way from the restaurant to you? The city is a **graph** — junctions are nodes, roads are edges with travel times. **Dijkstra's algorithm** finds the fastest route by always expanding the closest unexplored junction next. Roads close, one-way streets exist, and the answer has to be deterministic.

## Build this (small scale)

A city map with roads, one-way streets and closures, answering fastest-route queries.

## API

```kotlin
data class Route(val minutes: Int, val path: List<String>)

class CityMap {
    fun addRoad(a: String, b: String, minutes: Int, oneWay: Boolean = false)
    fun closeRoad(a: String, b: String)
    fun reopenRoad(a: String, b: String)
    fun fastestRoute(from: String, to: String): Route?
}
```

## Rules

- `minutes > 0` and `a != b`, else `IllegalArgumentException`. A two-way road creates edges `a→b` and `b→a`; a one-way road only `a→b`. Adding a road again **replaces** the travel time of those edges.
- `closeRoad(a, b)` closes the edges `a→b` and `b→a` (whichever exist); `reopenRoad` opens them again. Unknown roads: nothing happens.
- `fastestRoute`: the minimum total minutes over open edges, and the path including both ends.
  - `from == to` → `Route(0, [from])`.
  - Unreachable or unknown junction → `null`.
  - If several routes are equally fast, return the one whose path is **lexicographically smallest** (compare junction names one by one).

## Example

`A–B 5`, `B–C 5`, `A–C 15`: `A → C` is `Route(10, [A, B, C])`. Close `B–C` → `Route(15, [A, C])`.

## Think about (answer in NOTES.md)

- Why does Dijkstra fail with **negative** edge weights? Can travel times ever be negative?
- Google Maps answers routes across a whole country in milliseconds. Look up **A\*** and **contraction hierarchies**: what extra information do they use?
- Travel times change with traffic. How does that change what you store per edge?

---

## Run the tests

```bash
./gradlew :l3-swiggy-dijkstra-eta:test
```

Your code goes in `src/main/kotlin/CityMap.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
