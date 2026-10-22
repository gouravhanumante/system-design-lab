# Hints — Deduplicated chunk storage

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Separate "what does a file consist of" (an ordered list of chunk ids) from "where do the bytes of a chunk live" (a shared store).

</details>

<details><summary>Hint 2 — which data structure?</summary>

`files: Map<String, List<ChunkId>>` and `chunks: Map<ChunkId, Entry(bytes, refCount)>`. A chunk id can simply be the chunk's content as a `String` (ISO-8859-1), or its SHA-256.

</details>

<details><summary>Hint 3 — the approach</summary>

When adding a file: for every chunk, `refCount++` (insert the bytes if new). When removing a file: for every chunk id in its list, `refCount--`, and drop the entry at 0. Replacing a file = remove then add.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

The chunk table itself must be sharded by chunk hash, and reference counts need careful atomic updates (or periodic garbage-collection sweeps).

</details>
