# Hints — Nested comments with soft delete

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Every comment knows its parent (or `null`). To render you also need to go the other way — from a comment to its children.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `Comment` class with `children: MutableList<Comment>`, `deleted`, `postId`; plus a map `id → Comment` and a per-post list of top-level comments.

</details>

<details><summary>Hint 3 — the approach</summary>

Write a recursive function `hasLiveDescendant(c)` and a recursive `renderNode(c, depth)` that skips a deleted node when it has no live descendant. Compute liveness bottom-up so you do not repeat work.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Store comments as rows `(postId, id, parentId, createdAt)` and load one level at a time on demand.

</details>
