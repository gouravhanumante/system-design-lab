# Hints — Grid index for nearby drivers

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

You need two views of the same data: "which drivers are in this cell?" (for searching) and "where is this driver?" (for moving them).

</details>

<details><summary>Hint 2 — which data structure?</summary>

A map from cell → set of driver ids, plus a map from driver id → current position (so you can find the old cell on move).

</details>

<details><summary>Hint 3 — the approach</summary>

For a query, the circle fits inside a square from `x-radius..x+radius`. Convert the two corners to cells, loop over the cells in between, and only compute distances for drivers in those cells. `Math.floorDiv` handles negatives.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

A fixed grid is uneven: downtown is crowded, the desert is empty. Quadtrees, geohash prefixes, or Uber's H3 hexagons adapt to that.

</details>
