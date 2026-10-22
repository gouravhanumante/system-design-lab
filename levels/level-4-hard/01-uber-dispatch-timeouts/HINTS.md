# Hints — Dispatch with offer timeouts

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Each ride has a small life story: *offered to X at time T, already tried [A, B]*. Model that story explicitly.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A map `rideId → RideState` (current driver, offeredAt, set of tried drivers, status) and a map of available drivers with positions.

</details>

<details><summary>Hint 3 — the approach</summary>

Write one private function `offerNext(ride, now)` that picks the nearest untried available driver, reserves them (remove from the pool) and records `offeredAt`. `requestRide`, `decline` and `tick` all call it.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

With thousands of rides per second the timeouts become a priority queue ordered by expiry time instead of a scan of every ride on each tick.

</details>
