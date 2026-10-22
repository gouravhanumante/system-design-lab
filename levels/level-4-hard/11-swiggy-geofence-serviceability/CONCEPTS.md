# Concepts — Delivery zones (point in polygon)

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Geofencing

A **geofence** is a virtual boundary (a polygon on a map). Checking whether a location is inside one decides delivery availability, pricing zones, surge areas, alerts and more.

## Point in polygon (ray casting)

Shoot an imaginary ray from the point in one direction and count how many polygon edges it crosses. **Odd** = inside, **even** = outside. It works for concave shapes too. Careful edge cases: rays passing exactly through corners (the half-open rule `(yi > y) != (yj > y)` handles them) and points exactly on the border (check those separately).

## Cross product for "on the line"

For points A, B, P, the cross product `(B − A) × (P − A)` is zero exactly when P lies on the line through A and B. Combined with a range check, it tells you whether P is on the segment. Using integers or `Long` avoids floating-point surprises.

## Bounding-box prefilter

A polygon's **bounding box** (min/max x and y) is a cheap rectangle test. Most far-away zones are ruled out by it without running the expensive polygon check. This **coarse filter → exact check** pattern appears again and again (geo grids, search, reconciliation).

## Spatial indexes for polygons

With thousands of zones, even checking every bounding box is too much. **R-trees** (used by PostGIS, SQLite R*Tree) index rectangles so a point query only touches nearby ones.
