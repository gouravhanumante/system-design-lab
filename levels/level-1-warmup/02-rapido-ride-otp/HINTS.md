# Hints — Ride-start OTP

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

You need to remember two things per ride: the code, and whether it was already used.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`HashMap<String, String>` for codes, and a `HashSet<String>` (or a small class) for used rides.

</details>

<details><summary>Hint 3 — the approach</summary>

`issue` → `getOrPut(rideId) { generator() }`. `verify` → look up, compare, mark used only on success.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Real systems store a *hash* of the code, never the code itself, and compare in constant time.

</details>
