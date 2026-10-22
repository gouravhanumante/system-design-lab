# LRU cache on an edge server

| App | Level | Concept |
|-----|-------|---------|
| Netflix | 2 — Building blocks | Least-recently-used eviction with a byte budget, O(1) operations |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Netflix puts cache servers (Open Connect appliances) inside internet providers' networks so popular video files are served from nearby. Disk and memory are limited, so when a new file arrives and space runs out, something must go. The classic policy is **LRU — evict the least recently used** item: what nobody watched for the longest time is the least likely to be needed next.

## Build this (small scale)

An LRU cache whose capacity is measured in **bytes**, not item count.

## API

```kotlin
class EdgeCache(capacityBytes: Long) {
    fun get(key: String): ByteArray?
    fun put(key: String, value: ByteArray)
    val usedBytes: Long
    val hits: Int
    val misses: Int
    val evictions: Int
}
```

## Rules

- `get`: found → it becomes the **most recently used**, `hits + 1`, return it. Not found → `misses + 1`, return `null`.
- `put`: stores the value as the most recently used. If the key already exists its old value is replaced (and its old size no longer counts).
- After a `put`, while `usedBytes > capacityBytes`, evict the **least recently used** entry (`evictions + 1` per evicted entry). The entry just put is never evicted by its own `put`.
- A value **larger than the whole capacity** is not stored at all (and an existing entry with that key is removed — that removal does not count as an eviction).
- `usedBytes` = sum of the sizes of all stored values.

## Example

Capacity 10. Put `a`(4), `b`(4) → used 8. `get(a)` → `a` is now most recent. Put `c`(4) → used 12 > 10 → evict `b` (least recent) → used 8.

## Think about (answer in NOTES.md)

- What is the time complexity of your `get` and `put`? The classic O(1) design uses a hash map **plus** a doubly linked list — why both?
- LRU is fooled by a "scan": someone binge-watches a rarely watched series once and pushes popular titles out. What do LFU, LRU-K or "segmented LRU" do about it?
- Should a 4 GB movie and a 2 KB thumbnail compete for space equally?

---

## Run the tests

```bash
./gradlew :l2-netflix-lru-edge-cache:test
```

Your code goes in `src/main/kotlin/EdgeCache.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
