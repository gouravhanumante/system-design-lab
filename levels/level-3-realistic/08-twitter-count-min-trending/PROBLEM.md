# Trending hashtags with a Count-Min Sketch

| App | Level | Concept |
|-----|-------|---------|
| Twitter | 3 — Realistic | Probabilistic counting in fixed memory, heavy hitters |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Millions of different hashtags appear every hour. Keeping an exact counter for each one costs a lot of memory, most of it for tags used once. To find **trending** tags you only need good estimates for the **frequent** ones. A **Count-Min Sketch** counts everything in a fixed-size table — it can *overestimate* a count (because of collisions) but never *underestimate* it.

## Build this (small scale)

1. A Count-Min Sketch with `depth` rows of `width` counters.
2. A tracker of the top `k` tags built on top of it.

## API

```kotlin
fun bucket(item: String, row: Int, width: Int): Int       // provided: which column `item` uses in `row`

class CountMinSketch(width: Int, depth: Int) {
    fun add(item: String, count: Long = 1)
    fun estimate(item: String): Long
}

class TrendingTags(k: Int, width: Int = 1024, depth: Int = 4) {
    fun observe(tag: String)
    fun top(): List<Pair<String, Long>>
}
```

## Rules — sketch

- The table has `depth` rows and `width` columns, all starting at 0. `width` and `depth` must be ≥ 1 (else `IllegalArgumentException`).
- `add(item, count)`: in **every** row `r`, increase the counter at column `bucket(item, r, width)` by `count`.
- `estimate(item)`: the **minimum** over all rows of the counter at `bucket(item, r, width)`.

## Rules — trending

- `observe(tag)` adds 1 to the sketch, then keeps a set of at most `k` **candidate** tags:
  - if the tag is already a candidate, nothing else to do;
  - else if there are fewer than `k` candidates, add it;
  - else find the candidate with the **lowest current estimate** (ties: the alphabetically **largest** tag) and replace it if the new tag's estimate is **strictly greater**.
- `top()` returns the candidates with their **current** estimates, highest first, ties by tag ascending.

## Example

`width = 1000`, few tags → no collisions, estimates are exact. With a tiny `width = 4`, unrelated tags share counters and estimates go **up**, never down.

## Think about (answer in NOTES.md)

- Why does taking the **minimum** over several rows reduce the error? What do `width` and `depth` each control?
- Memory: exact counting of 10 million distinct tags vs a 4 × 100,000 sketch. Rough numbers?
- "Trending" usually means *growing fast*, not *most used*. How would you add time decay (e.g. last hour only)?

---

## Run the tests

```bash
./gradlew :l3-twitter-count-min-trending:test
```

Your code goes in `src/main/kotlin/CountMin.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
