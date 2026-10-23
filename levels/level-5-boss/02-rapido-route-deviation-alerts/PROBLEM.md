# "Share trip" route deviation alerts

| App | Level | Concept |
|-----|-------|---------|
| Rapido | 5 — Boss | Point-to-segment geometry, streak counting, alert de-duplication |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Ride apps let you share a live trip with family. If the bike leaves the expected route and stays off it, the app raises a safety alert. Too sensitive and everyone gets false alarms every time a driver takes a one-way street shortcut; too lax and the alert comes too late. You also must not spam the same alert on every GPS ping.

## Build this (small scale)

A monitor that knows each trip's planned route (a polyline), receives GPS pings, and raises **one** alert when the vehicle has been off-route for several pings in a row.

## API

```kotlin
class RouteMonitor(thresholdMeters: Int, consecutivePings: Int) {
    fun startTrip(tripId: String, route: List<Pair<Int, Int>>)       // route points in meters
    fun ping(tripId: String, x: Int, y: Int, now: Long): Boolean     // true exactly when a NEW alert is raised
    fun alerts(tripId: String): List<Long>                           // times at which alerts were raised
    fun endTrip(tripId: String)
}
```

## Rules

- The route is a polyline: consecutive points form **segments**. The distance from a ping to the route is the distance to the **nearest point on any segment** (not just the nearest corner).
- A ping is **off-route** if its distance is **strictly greater** than `thresholdMeters`. Exactly on the threshold is still on-route.
- An alert is raised when `consecutivePings` off-route pings happen in a row (the last of them returns `true`).
- After an alert, no further alert is raised until the vehicle comes **back on route** (one on-route ping resets everything). Then it can alert again.
- `startTrip` needs at least 2 points, otherwise throw `IllegalArgumentException`.
- Pings for an unknown or ended trip return `false`. `alerts` of an unknown trip is an empty list.
- Trips are independent of each other.

## Example

Route `(0,0) → (1000,0)`, threshold 50, 3 pings. Pings at `(500, 60)`, `(500, 80)`, `(500, 90)` → the third returns `true`. A 4th ping at `(500, 100)` returns `false` (already alerted).

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`startTrip()`** / **`endTrip()`** / **`alerts()`**. 🎯 `routeNeedsAtLeastTwoPoints`
2. **`distanceToSegmentSquared()`** — the geometry part. 🎯 `distanceIsMeasuredToTheSegmentNotJustTheCorners` (with step 4)
3. **`distanceToRouteSquared()`** — closest of all segments. 🎯 `polylineWithACorner` (with step 4)
4. **`ping()`** — off-route streak and one alert per streak. 🎯 `onRoutePingsNeverAlert`, `alertsOnTheNthConsecutiveOffRoutePingOnlyOnce`, `thresholdIsStrict`, `anOnRoutePingResetsTheStreak`, `canAlertAgainAfterReturningToTheRoute`, `tripsAreIndependentAndEndedTripsAreIgnored`

## Think about (answer in NOTES.md)

- GPS is noisy. Why is "N pings in a row" better than "one bad ping"? What are other options (time-based, speed-aware)?
- For a route with 5,000 points, checking every segment per ping is slow. How could you narrow down which segments to test?
- Where would this logic run — phone, or server? What are the trade-offs?

---

## Run the tests

```bash
./gradlew :l5-rapido-route-deviation-alerts:test
```

Your code goes in `src/main/kotlin/RouteMonitor.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
