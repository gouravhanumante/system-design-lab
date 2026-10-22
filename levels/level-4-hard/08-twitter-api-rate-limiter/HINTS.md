# Hints — API rate limiter (token buckets, per user and global)

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

You never need a timer. A bucket only needs two numbers — `tokens` and `lastRefillMillis` — and you "catch up" the refill whenever someone asks.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A small private `Bucket(capacity, rate)` class with `tokens: Double`, `last: Long` and a `refill(now)` method. A `HashMap<String, Bucket>` for clients, one extra `Bucket` for the global limit.

</details>

<details><summary>Hint 3 — the approach</summary>

`tryAcquire`: refill both → check both `>= 1.0` → only then subtract from both. `retryAfterMillis`: refill both, compute the wait for each short bucket with `Math.ceil`, return the max.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

In Redis the whole check-and-spend must happen atomically (a Lua script), or two servers can both see "1 token left" and both spend it.

</details>
