# Hints — Fastest route and ETA (Dijkstra)

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Keep a "best known time" per junction. Repeatedly pick the unsettled junction with the smallest time, settle it, and try to improve its neighbours.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Adjacency: `Map<String, MutableMap<String, Edge(minutes, open)>>`. A `PriorityQueue` of `(time, path)`.

</details>

<details><summary>Hint 3 — the approach</summary>

For the tie-break, order the priority queue by time **and then by the path list** compared element by element. When a junction is popped for the first time, its (time, path) is the best one — skip later pops of the same junction.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Delivery apps precompute travel times between grid cells (a distance matrix) and only run detailed routing for the final stretch.

</details>
