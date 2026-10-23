# Trip lifecycle, fare and cancellation fees

| App | Level | Concept |
|-----|-------|---------|
| Uber | 5 — Boss | State machine, invalid-transition guards, pricing rules |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

A ride is not one event but a **life story**: requested, matched, driver arrives, trip starts, trip ends — with cancellations possible along the way, each with different money consequences. Bugs here cost real money (charging a rider twice, forgetting the cancellation fee, completing a trip that never started).

## Build this (small scale)

A trip service that enforces the legal order of steps, computes the final fare, and decides cancellation fees. Time is passed in as `now` (seconds).

## API

```kotlin
enum class RideState { REQUESTED, MATCHED, ARRIVED, STARTED, COMPLETED, CANCELLED }
enum class Party { RIDER, DRIVER }

class TripService {
    fun request(rideId: String, now: Long, surge: Double = 1.0)
    fun match(rideId: String, driverId: String, now: Long)
    fun arrive(rideId: String, now: Long)
    fun start(rideId: String, now: Long)
    fun complete(rideId: String, distanceMeters: Int, now: Long): Long   // returns the fare
    fun cancel(rideId: String, by: Party, now: Long): Long              // returns the cancellation fee
    fun state(rideId: String): RideState
    fun fare(rideId: String): Long?                                      // null until completed
}
```

## Rules

**Transitions**

| call | allowed from | result |
|------|--------------|--------|
| `request` | (new ride) | `REQUESTED` |
| `match` | `REQUESTED` | `MATCHED` (remember the time) |
| `arrive` | `MATCHED` | `ARRIVED` |
| `start` | `ARRIVED` | `STARTED` (remember the time) |
| `complete` | `STARTED` | `COMPLETED`, returns fare |
| `cancel` by `RIDER` | `REQUESTED`, `MATCHED`, `ARRIVED` | `CANCELLED` |
| `cancel` by `DRIVER` | `MATCHED`, `ARRIVED` | back to `REQUESTED` (driver removed, ride can be matched again) |

- Any other call throws `IllegalStateException`. An unknown ride id throws `IllegalArgumentException`. Requesting an id that already exists throws `IllegalStateException`.

**Fare** (whole currency units)

`fare = round((50 + 12 × km + 2 × minutes) × surge)` where `km = distanceMeters / 1000.0` (not rounded) and `minutes = ceil((completeTime − startTime) / 60.0)`, at least `1`. Use `Math.round`. After surge and rounding, the fare is never below `80`. `surge` is fixed when the ride is requested.

**Cancellation fee**

- Rider cancels in `REQUESTED`: fee `0`.
- Rider cancels in `MATCHED` or `ARRIVED`: fee `30` if `now − matchedAt > 120` seconds, otherwise `0`.
- Driver cancels: fee `0` (the rider is never charged for that).

## Example

Surge `1.5`, distance 5000 m, trip took 14 min 10 s (→ 15 minutes): `(50 + 60 + 30) × 1.5 = 210`.

## Think about (answer in NOTES.md)

- Why store the *surge* with the ride instead of looking it up at completion time?
- What if the same `complete` request arrives twice (a mobile retry)? How would you make it **idempotent**?
- Which of these rules would you want to change without redeploying code?

---

## Run the tests

```bash
./gradlew :l5-uber-trip-lifecycle-fare:test
```

Your code goes in `src/main/kotlin/TripService.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
