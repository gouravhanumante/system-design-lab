# Hints — Delivery zones (point in polygon)

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Ray casting: shoot a ray from the point to the right. Count how many polygon edges it crosses. Odd = inside, even = outside.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Per zone: its points, priority, and a precomputed bounding box `(minX, minY, maxX, maxY)`.

</details>

<details><summary>Hint 3 — the approach</summary>

Handle the border first: the point is on edge A→B if the cross product `(B−A) × (P−A)` is 0 **and** P lies within the edge's x and y range. Then ray casting for each edge (i, j): if `(yi > y) != (yj > y)` and `x < (xj − xi) × (y − yi) / (yj − yi) + xi` → toggle inside. Use `Long`/`Double` to avoid overflow.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Databases like PostGIS and Elasticsearch store polygons with spatial indexes (R-trees) so "which polygons contain this point" doesn't check every zone.

</details>
