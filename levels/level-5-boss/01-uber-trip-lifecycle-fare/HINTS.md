# Hints — Trip lifecycle, fare and cancellation fees

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Write down, on paper, a table of "from state → allowed actions → to state". The code is that table.

</details>

<details><summary>Hint 2 — which data structure?</summary>

One small private class per ride holding state, surge, matchedAt, startedAt, driverId, fare. A `HashMap<String, Ride>`.

</details>

<details><summary>Hint 3 — the approach</summary>

Create a private helper `ride(id)` (throws for unknown) and `require(ride.state == X)` at the top of each function (throw `IllegalStateException` otherwise). Compute fare in `complete`. Keep the cancellation-fee rule in one small function so it is easy to test.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

In production each transition is an event written to a log; "current state" is derived from it. That gives audit trails and safe retries.

</details>
