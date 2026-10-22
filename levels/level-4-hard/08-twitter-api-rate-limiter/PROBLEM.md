# API rate limiter (token buckets, per user and global)

| App | Level | Concept |
|-----|-------|---------|
| Twitter | 4 — Hard | Token bucket algorithm, layered limits, Retry-After |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

The X API lets each app make a limited number of requests ("300 per 15 minutes"), and the whole service also has a global capacity it must protect. Short bursts are fine; sustained floods are not. The most common algorithm is the **token bucket**: a bucket holds up to `capacity` tokens, refills at a steady rate, and every request spends a token. A refused client is told how long to wait (`Retry-After`).

## Build this (small scale)

A limiter with one bucket **per client** and one **global** bucket shared by everyone. Time is passed in milliseconds.

## API

```kotlin
class RateLimiter(
    perClientCapacity: Int, perClientRefillPerSecond: Double,
    globalCapacity: Int, globalRefillPerSecond: Double,
) {
    fun tryAcquire(clientId: String, nowMillis: Long): Boolean
    fun retryAfterMillis(clientId: String, nowMillis: Long): Long
    fun tokens(clientId: String, nowMillis: Long): Double      // the client's bucket, after refill
}
```

## Rules

- Every bucket starts **full**. A new client's bucket is created full the first time the client is seen.
- **Refill** (do this before every decision): `tokens = min(capacity, tokens + elapsedSeconds × refillPerSecond)`, where `elapsedSeconds` is the time since that bucket was last refilled.
- `tryAcquire` succeeds only if **both** the client's bucket and the global bucket hold at least 1 token. On success **both** lose exactly 1 token. On failure **neither** changes (no partial spending).
- `retryAfterMillis`: `0` if a request would succeed right now; otherwise the smallest number of whole milliseconds after which **both** buckets will have ≥ 1 token (round **up**). For each bucket that is short: `ceil((1 − tokens) / refillPerSecond × 1000)`; take the maximum.
- `tokens` reports the client bucket's current (refilled) level; it does not spend anything.
- Calls arrive with non-decreasing `nowMillis`.

## Example

Client capacity 2, refill 1 token/s; global huge. Requests at 0, 0, 0 ms → `true, true, false`. At 0 ms `retryAfterMillis` = 1000. At 500 ms the client has 0.5 tokens → retry after 500 ms.

## Think about (answer in NOTES.md)

- Compare token bucket with **fixed window** ("max 100 per minute starting at :00") — what burst problem does a fixed window have at the boundary?
- With 50 API servers, where does the bucket live? What does a Redis-based limiter look like, and what happens when Redis is slow?
- Why must failed requests not spend from either bucket?

---

## Run the tests

```bash
./gradlew :l4-twitter-api-rate-limiter:test
```

Your code goes in `src/main/kotlin/RateLimiter.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
