# Hints — Partitions and key-based routing

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

A topic is just an array of independent logs. Your job is to pick which one.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`Array<ArrayList<Record>>` (one list per partition) plus an `Int` counter for round-robin.

</details>

<details><summary>Hint 3 — the approach</summary>

`Math.floorMod` (not `%`) matters because `hashCode()` can be negative. The record's offset is the current size of its partition's list.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Each partition lives on a *broker* machine and has replicas on others. Producers need a metadata cache of "which broker owns partition 3".

</details>
