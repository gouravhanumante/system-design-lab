# Hints — View counts at scale — write batching

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Imagine a tally sheet next to the database. Views go on the sheet; every so often you copy a whole sheet entry into the database.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`HashMap<String, Long>` as the buffer, in addition to the injected `store`.

</details>

<details><summary>Hint 3 — the approach</summary>

In `recordView`: increment the buffer; if it reaches the threshold, call `store.add` and remove that buffer entry. `views` is `store.get(id) + (buffer[id] ?: 0)`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Sharded counters: split one logical counter into N sub-counters so concurrent writers never touch the same row.

</details>
