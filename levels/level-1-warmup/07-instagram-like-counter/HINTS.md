# Hints — Likes that can't be double-counted

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

A "set" gives you the *at most once* rule for free. The only extra requirement is remembering the order.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`HashMap<String, LinkedHashSet<String>>` (post → users in insertion order). `LinkedHashSet.add` returns `false` for a duplicate.

</details>

<details><summary>Hint 3 — the approach</summary>

`likedBy` = the insertion-ordered set reversed.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Keep the exact membership in a store sharded by `postId`, but serve the *count* from a counter cache that tolerates being a little stale.

</details>
