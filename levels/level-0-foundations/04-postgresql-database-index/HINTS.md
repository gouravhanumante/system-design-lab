# Hints — Database indexes vs full table scans

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

The table itself is just `id → row`. Each index is an *extra* structure pointing back to ids.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Sorted index: `java.util.TreeMap<Int, MutableSet<Int>>` (age → ids), and `subMap(min, true, max, true)` gives the range. Hash index: `HashMap<String, MutableSet<Int>>`.

</details>

<details><summary>Hint 3 — the approach</summary>

Every `insert`/`delete` updates the table **and** every existing index. `createIndex` fills the new index from all current rows.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

A **composite** index on `(city, age)` answers "city = Pune AND age between 25 and 30" in one lookup — column order matters.

</details>
