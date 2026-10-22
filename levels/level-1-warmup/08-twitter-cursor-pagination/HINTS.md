# Hints — Timeline pages with a cursor

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

A cursor is just "the id where I stopped". The next page is "everything older than that".

</details>

<details><summary>Hint 2 — which data structure?</summary>

Per user, a sorted structure of tweet ids: `TreeMap<Long, Tweet>` gives you `headMap(cursor)` = all ids below the cursor, and `descendingMap()` for newest-first order.

</details>

<details><summary>Hint 3 — the approach</summary>

Take `limit + 1` tweets: if you got more than `limit`, there is a next page and the cursor is the id of item number `limit`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

The same "fetch limit + 1" trick and keyset condition (`WHERE id < ? ORDER BY id DESC LIMIT ?`) work directly in SQL and stay fast on huge tables, unlike `OFFSET 100000`.

</details>
