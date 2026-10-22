# View counts at scale — write batching

| App | Level | Concept |
|-----|-------|---------|
| YouTube | 4 — Hard | Write coalescing, buffering in front of a database |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

A viral video receives tens of thousands of views per second. If every view did `UPDATE videos SET views = views + 1` the database would melt. The standard trick: **buffer increments in memory and write them in batches**. The cost: a view is briefly "only in memory", so readers must add the buffer to what the database says.

## Build this (small scale)

A service in front of a "database" (a `ViewStore` interface you are given) that collapses many `+1`s into few `+N` writes — without ever showing a wrong total.

## API

```kotlin
interface ViewStore {                      // the "database" (provided)
    fun add(videoId: String, delta: Long)
    fun get(videoId: String): Long
}

class ViewCountService(store: ViewStore, flushThreshold: Int = 100) {
    fun recordView(videoId: String)
    fun views(videoId: String): Long       // always exact: store + still-buffered
    fun flushAll()
}
```

## Rules

- `recordView` adds to an in-memory buffer **per video**. The moment a video's buffer reaches `flushThreshold`, write it with **one** `store.add(videoId, bufferedCount)` call and reset that buffer to 0.
- `views(videoId)` = `store.get(videoId)` + that video's current buffer. Never lose or double-count a view.
- `flushAll()` writes every non-empty buffer with one `add` call per video, then empties all buffers. A buffer of 0 is **never** written.
- The service must make **far fewer** `store.add` calls than views (the tests count them).

## Example

Threshold 100: 250 views of one video cause 2 `add(…, 100)` calls; 50 views stay buffered. `views()` still reports 250. After `flushAll()` there is a third call `add(…, 50)`.

## Think about (answer in NOTES.md)

- What happens to buffered views if the server crashes? How could you reduce that loss (write-ahead log, flush every N seconds, replicate the buffer)?
- A bigger threshold means fewer writes but more risk and staler database numbers. How would you pick one?
- Your counter is in one process. With 40 servers, who holds "the buffer"? (Hint: each holds its own — what does that mean for reads?)

---

## Run the tests

```bash
./gradlew :l4-youtube-batched-view-counts:test
```

Your code goes in `src/main/kotlin/ViewCountService.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
