# Dispatch with offer timeouts

| App | Level | Concept |
|-----|-------|---------|
| Uber | 4 — Hard | Time-driven state machine, retry to next candidate |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Uber does not assign a ride to a driver — it **offers** it. The driver has about 10 seconds to accept. If they decline or ignore it, the offer moves to the next nearest driver. Meanwhile that driver must not receive a second offer, and the rider needs a definite answer: matched, or "no drivers".

## Build this (small scale)

A dispatcher that offers each ride to one driver at a time, handles accept / decline / timeout, and never offers the same ride to the same driver twice. Time is passed in (`now`, in seconds), so tests are deterministic.

## API

```kotlin
data class Offer(val rideId: String, val driverId: String)

class Dispatcher(offerTimeoutSeconds: Long = 10) {
    fun setDriverAvailable(driverId: String, x: Int, y: Int)
    fun requestRide(rideId: String, x: Int, y: Int, now: Long): Offer?
    fun accept(rideId: String, driverId: String, now: Long): Boolean
    fun decline(rideId: String, driverId: String, now: Long): Offer?
    fun tick(now: Long): List<Offer>
    fun rideStatus(rideId: String): String
}
```

## Rules

- `setDriverAvailable` adds a driver to the pool (or updates the position of one already there).
- `requestRide` offers the ride to the **nearest available** driver (squared distance, ties by driver id) and returns that `Offer`. No driver available → status `FAILED`, return `null`.
- A driver with an outstanding offer is **reserved**: not available to any other ride. A driver who accepted is gone from the pool until `setDriverAvailable` is called again.
- An offer made at time `t` is valid while `now < t + timeout`. `accept` before that returns `true` and the ride becomes `MATCHED`. After that (or for the wrong driver / unknown ride / ride not offered) it returns `false` and changes nothing.
- `decline` by the driver who currently holds the offer: that driver returns to the pool (same position) and the ride is offered to the next nearest driver **not yet tried for this ride**; returns that `Offer`, or `null` if nobody is left (status `FAILED`). A decline that does not match the current offer is ignored and returns `null`.
- `tick(now)` finds offers with `now >= offeredAt + timeout`. For each such ride in ascending ride id order: release the driver back to the pool, mark them as tried, offer to the next candidate (offered at `now`) or mark `FAILED`. Return the new offers in that order.
- `rideStatus` is one of `OFFERED:<driverId>`, `MATCHED:<driverId>`, `FAILED`, `UNKNOWN`.

## Example

Drivers `d1` (distance 1) and `d2` (distance 5). `requestRide("r", …, now = 0)` → offer to `d1`. `tick(10)` → `d1` timed out, new offer to `d2`. `tick(20)` → `d2` timed out too → status `FAILED`.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`setDriverAvailable()`** — put the driver in the free pool and remember its position.
2. **`offerToNextDriver()`** + **`requestRide()`** + **`rideStatus()`**. 🎯 `offersTheNearestDriverAndAcceptMatches` (with step 3), `noDriversFailsImmediately`, `reservedDriversAreNotOfferedToOtherRides`
3. **`accept()`**. 🎯 `offerExpiresExactlyAtTimeout`, `acceptRemovesDriverFromPoolUntilTheyAreAvailableAgain`, `wrongOrStaleCallsAreIgnored`
4. **`releaseDriver()`** + **`decline()`**. 🎯 `declineMovesToNextNearestAndNeverReoffersToSameDriver`, `declinedDriverBecomesAvailableForOtherRides`
5. **`tick()`** — expire old offers. 🎯 `timeoutPassesOfferToNextDriverWithFreshTimer`, `tickHandlesSeveralRidesInRideIdOrder`

## Think about (answer in NOTES.md)

- What if two rides want the same nearest driver at the same moment? How does your design prevent a double offer?
- Where would you keep this state in a real system, and what happens if the dispatcher server crashes mid-offer?

---

## Run the tests

```bash
./gradlew :l4-uber-dispatch-timeouts:test
```

Your code goes in `src/main/kotlin/Dispatcher.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
