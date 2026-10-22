# Hints — Consumer groups, commits and replay

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

You need two numbers per `(group, partition)`. One lives only in memory (position). One survives a crash (committed).

</details>

<details><summary>Hint 2 — which data structure?</summary>

Two maps keyed by `Pair(group, partition)` (or a nested map): `position` and `committed`. Default value `0`.

</details>

<details><summary>Hint 3 — the approach</summary>

`poll` → `topic.read(partition, position, max)`, then position += result size. `restart` → for every partition of the group set position := committed. Because `restart` must touch *all* partitions of the group, loop over `0 until topic.partitionCount`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Kafka stores committed offsets in an internal compacted topic (`__consumer_offsets`) — itself a replicated log.

</details>
