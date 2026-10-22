# Hints — Stories: 24-hour expiry, audiences and highlights

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Nothing is stored about "who can see what". The permission is **computed** from three facts every time: the story's audience, the owner's *current* close-friends list, and the clock.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `Story` class (`owner`, `audience`, `postedAt`, `highlighted`, `viewers: LinkedHashSet<String>`), a `LinkedHashMap<String, Story>` by id, and `closeFriends: Map<owner, Set<friend>>`.

</details>

<details><summary>Hint 3 — the approach</summary>

Implement `canView` first (all other functions call it). `visibleStories` = owner's stories filtered by `canView`, sorted by `(postedAt, id)`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Index stories by expiry time (a sorted structure or a TTL in the store) so a cleanup job removes expired ones without scanning everything.

</details>
