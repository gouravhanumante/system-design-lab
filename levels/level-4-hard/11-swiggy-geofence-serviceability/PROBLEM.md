# Delivery zones (point in polygon)

| App | Level | Concept |
|-----|-------|---------|
| Swiggy | 4 — Hard | Geofencing, ray casting, boundary cases, bounding-box prefilter |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Open Swiggy at a new address and it instantly knows: "Delivering from the Koramangala hub" — or "Not serviceable here". Each delivery hub covers an area drawn as a **polygon** on the map. Deciding whether your pin is inside a polygon is the **point-in-polygon** problem, and with hundreds of zones you want to skip the far-away ones cheaply.

## Build this (small scale)

A set of polygon zones with priorities, answering "which zone serves this point?".

## API

```kotlin
class ServiceZones {
    fun addZone(zoneId: String, polygon: List<Pair<Int, Int>>, priority: Int = 0)
    fun zoneFor(x: Int, y: Int): String?
    val lastFullChecks: Int
}
```

## Rules

- A polygon is a list of corners in order (it closes automatically from the last corner back to the first). Fewer than 3 corners, or a duplicate zone id → `IllegalArgumentException`. Polygons may be **concave** (L-shapes, notches).
- A point **on the border** (an edge or a corner) counts as **inside**.
- `zoneFor`: among all zones containing the point, return the one with the **highest priority**; ties → smaller zone id. None → `null`.
- **Bounding-box prefilter**: for each zone, first check the point against the zone's bounding box (min/max x and y). Only zones whose box contains the point get the full polygon test. `lastFullChecks` = how many full polygon tests the last `zoneFor` call ran.

## Example

Zone `HSR` is the square (0,0)–(10,10). `(5, 5)` → `HSR`; `(10, 3)` (on the edge) → `HSR`; `(11, 5)` → `null` with `lastFullChecks = 0` (the box already rules it out).

## Think about (answer in NOTES.md)

- Real coordinates are latitude/longitude on a sphere. When does treating them as flat x/y go wrong?
- With 10,000 zones, checking every bounding box is still O(zones). Which index from earlier problems would help?
- Overlapping zones are normal (a dark store and a restaurant hub). Is "highest priority" the right rule, or should you return all of them?

---

## Run the tests

```bash
./gradlew :l4-swiggy-geofence-serviceability:test
```

Your code goes in `src/main/kotlin/ServiceZones.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
