# Live demand heatmap (hot zones)

| App | Level | Concept |
|-----|-------|---------|
| Rapido | 4 — Hard | Ring buffer of time buckets, running totals, top-k |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

The Rapido captain app shows a heatmap: "these zones are busy *right now*". Behind it, millions of ride requests stream in. You cannot keep every event for 5 minutes and recount on every map refresh — you need counts that **forget** old events cheaply, using fixed memory.

## Build this (small scale)

Record ride requests per zone and answer "which `k` zones had the most requests in the last `windowSeconds`?" using **time buckets** instead of storing each request.

## API

```kotlin
class HotZones(windowSeconds: Int = 300, bucketSeconds: Int = 10) {
    fun record(zone: String, now: Long)
    fun topZones(k: Int, now: Long): List<Pair<String, Int>>   // (zone, count), best first
}
```

## Rules

- Time is split into buckets of `bucketSeconds`. A request at time `t` belongs to bucket `t / bucketSeconds` (integer division).
- `windowBuckets = windowSeconds / bucketSeconds`. At time `now` a request in bucket `b` still counts if `now / bucketSeconds - b < windowBuckets`.
  With the default settings a request at `t = 5` (bucket 0) counts at `now = 299` and is gone at `now = 300`.
- `topZones` sorts by count descending, then zone name ascending, and returns at most `k` zones. Zones with zero counted requests are not returned.
- `record` calls arrive with non-decreasing `now`; `topZones` may be called at any `now >=` the latest record.
- Memory must depend on the number of **buckets** (and distinct zones), not on the number of requests.

## Example

Window 30 s, bucket 10 s (3 buckets). Records: `A@0, A@5, B@12, A@25`. At `now = 29` → `A:3, B:1`. At `now = 30` bucket 0 expired → `A:1, B:1`.

## Think about (answer in NOTES.md)

- What is the trade-off of a bigger bucket size? (Accuracy of "exactly last 5 minutes" vs memory and speed.)
- How would you run this across 50 servers, each seeing a share of the traffic? How do you merge the answers?

---

## Run the tests

```bash
./gradlew :l4-rapido-hot-zones-window:test
```

Your code goes in `src/main/kotlin/HotZones.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
