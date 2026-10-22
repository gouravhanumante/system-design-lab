# Hints — Consistent hashing for cache servers

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

You need "the first point at or after `h`" quickly. That's a sorted structure with a ceiling lookup.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`TreeMap<Long, String>` from position → node. `ceilingEntry(h)` finds the first point ≥ h; `firstEntry()` handles the wrap-around.

</details>

<details><summary>Hint 3 — the approach</summary>

When adding a point whose position is already taken, keep whichever node name is smaller. On removal, only remove positions that currently map to the removed node.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Each key is usually stored on the next **N distinct** servers clockwise (replication), so losing one server does not lose data.

</details>
