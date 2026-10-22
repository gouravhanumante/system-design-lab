# Hints — Find the nearest drivers

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Compute a distance for every driver first. Then ask: how do I order them so that ties are handled?

</details>

<details><summary>Hint 2 — which data structure?</summary>

A plain list sorted with a comparator that looks at *two* things (distance, then id) is enough.

</details>

<details><summary>Hint 3 — the approach</summary>

Sort by `(distance², id)` and take the first `k`. Later you can try a max-heap of size `k` to avoid sorting everything.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Scanning every driver per request is O(n). What if drivers were grouped by area so you only look at nearby groups? (That is exactly Level 3 of this app.)

</details>
