# Hints — LRU cache on an edge server

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

You need two things fast: find an item by key, and find the *least recently used* item. Order changes on every `get`.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`LinkedHashMap(initialCapacity, 0.75f, accessOrder = true)` keeps entries in access order — the first entry is the least recently used. (For extra learning, build your own doubly linked list + `HashMap`.)

</details>

<details><summary>Hint 3 — the approach</summary>

In `put`: remove the old entry (subtract its size), check the "too big" rule, insert, add the size, then evict from the front while over capacity — skipping the key you just inserted.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

With many edge servers, which server caches which title? That's consistent hashing — Level 3 of this app.

</details>
