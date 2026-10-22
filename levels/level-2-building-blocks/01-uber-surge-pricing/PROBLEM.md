# Surge pricing for a zone

| App | Level | Concept |
|-----|-------|---------|
| Uber | 2 — Building blocks | Sliding time window, queue eviction |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

When lots of people want rides in the same area and few drivers are free, Uber raises the price ("surge") to pull more drivers in and cool demand down. The multiplier depends on **recent** demand versus supply, not on what happened yesterday.

## Build this (small scale)

Track ride requests per zone and compute a price multiplier from the last `windowSeconds` seconds of demand and the number of available drivers.

## API

```kotlin
class SurgePricing(windowSeconds: Long = 60) {
    fun recordRequest(zone: String, now: Long)
    fun setAvailableDrivers(zone: String, count: Int)
    fun multiplier(zone: String, now: Long): Double
}
```

## Rules

- A request made at time `t` counts at time `now` only if `now - t < windowSeconds`. With a 60 s window a request at `t = 0` counts at `now = 59` but not at `now = 60`.
- `demand` = number of counted requests in that zone. `supply` = `max(availableDrivers, 1)` (a zone with no drivers counts as 1; unknown zones have 0 drivers).
- `ratio = demand / supply` as a `Double`. Multiplier:

| ratio | multiplier |
|-------|-----------:|
| ≤ 1.0 | 1.0 |
| ≤ 1.5 | 1.2 |
| ≤ 2.0 | 1.5 |
| ≤ 3.0 | 2.0 |
| > 3.0 | 3.0 |

- Zones never affect each other.
- `recordRequest` calls for a zone arrive with non-decreasing timestamps.

## Example

3 drivers, 5 requests in the window → ratio 1.67 → multiplier `1.5`.

## Think about (answer in NOTES.md)

- Memory: if a zone gets 50,000 requests a minute, what do you keep? Do you need to remember *every* request?
- Could you store just one counter per second instead of one entry per request?

---

## Run the tests

```bash
./gradlew :l2-uber-surge-pricing:test
```

Your code goes in `src/main/kotlin/SurgePricing.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
