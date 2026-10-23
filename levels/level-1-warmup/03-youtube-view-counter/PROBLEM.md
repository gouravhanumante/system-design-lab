# Video view counter and top videos

| App | Level | Concept |
|-----|-------|---------|
| YouTube | 1 — Warm-up | Hash map counting, ranking with tie-breaks |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Every YouTube page shows "1,234,567 views", and the home page ranks videos by popularity. Both start with one humble operation: *add one to a counter for this video*.

## Build this (small scale)

Count views per video and report the most viewed videos.

## API

```kotlin
class ViewCounter {
    fun view(videoId: String)
    fun views(videoId: String): Long
    fun top(n: Int): List<Pair<String, Long>>
}
```

## Rules

- `views` of a video nobody watched is `0`.
- `top(n)` returns up to `n` pairs `(videoId, views)`, most views first. Equal views → smaller id first. `n = 0` → empty list.
- Videos with zero views are never listed.

## Example

Views: `a, b, a, c, b, a` → `top(2) = [("a", 3), ("b", 2)]`.

## Think about (answer in NOTES.md)

- Time complexity of `top(n)` as written? If it is called on every page load, what could you do about it?
- A viral video gets 50,000 views per second. What happens to a single counter that every server updates?

---

## Run the tests

```bash
./gradlew :l1-youtube-view-counter:test
```

Your code goes in `src/main/kotlin/ViewCounter.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
