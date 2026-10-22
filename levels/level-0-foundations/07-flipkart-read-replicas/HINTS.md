# Hints — Read replicas and read-your-writes

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

You don't need real copies. Keep the full history of each key (time, value) once; a replica "is" just the primary seen `lagSeconds` in the past.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`HashMap<String, MutableList<Pair<Long, String>>>` for key history; `HashMap<String, Long>` for each user's last write time; an `Int` round-robin counter.

</details>

<details><summary>Hint 3 — the approach</summary>

`valueAt(key, t)` = the value of the last history entry with time `<= t`. Primary read = `valueAt(key, now)`; replica read = `valueAt(key, now − lag)`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Some systems pass the write's position in the replication log (an LSN / "session token") with each request; a replica serves the read only once it has applied that position.

</details>
