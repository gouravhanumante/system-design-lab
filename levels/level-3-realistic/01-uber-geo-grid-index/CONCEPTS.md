# Concepts — Grid index for nearby drivers

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Spatial index (grid, geohash, H3)

A spatial index answers "what is near this point?" without checking everything. The simplest one is a **grid**: split the map into equal square cells and keep, for each cell, the set of things inside it. A query only looks at the cells that overlap the search area.

- **Geohash** encodes a location as a string where a shared prefix means "nearby" (e.g. `tdr1y…`) — handy because databases index strings well.
- **Quadtree** splits crowded cells into 4 smaller ones, adapting to density.
- **H3** (by Uber) uses hexagons, which have equal distance to all neighbours.

## Integer division and negative numbers

To find a cell you divide the coordinate by the cell size. Careful: in Kotlin/Java `-1 / 10 == 0` (it truncates toward zero), but `-1` belongs in cell `-1`, not cell `0`. `Math.floorDiv(-1, 10) == -1` rounds **down**, which is what a grid needs. Bugs like this are classic in geo code.

## Keeping two views of the same data in sync

A moving driver must be findable by **cell** (for queries) and by **id** (to know which cell to remove them from when they move). Two maps holding the same facts is common (an *index* next to the *primary* data), but every write must update both, or they drift apart. Databases do exactly this with secondary indexes.

## Bounding box first, exact check second

The cells you scan form a square around the query circle, so some drivers in those cells are still too far away. The pattern is: **cheap coarse filter** (cells), then **exact filter** (distance). Most search systems work like this — a fast index narrows candidates, a precise check confirms.
