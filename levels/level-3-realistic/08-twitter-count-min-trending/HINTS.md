# Hints — Trending hashtags with a Count-Min Sketch

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

The sketch is just a 2D array of `Long`. `add` writes to one cell per row; `estimate` reads one cell per row.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`Array(depth) { LongArray(width) }` for the sketch; a `HashSet<String>` (or map) for the trending candidates.

</details>

<details><summary>Hint 3 — the approach</summary>

For eviction, compute `estimate` for every candidate (at most `k` of them) and pick the minimum with `minWithOrNull(compareBy({ est }, { reverse name }))` — or sort by `(estimate, -name)`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Sketches are **mergeable**: two servers' sketches of the same size can be added cell by cell. That makes them perfect for distributed counting.

</details>
