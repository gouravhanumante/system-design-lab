# Hints — Version history and restore

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Keep two separate things: the **list of retained versions** and a **counter** for "the next number to hand out".

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `LinkedHashMap<Int, String>` (version → content) keeps insertion order; remove from the front when it grows past `maxVersions`.

</details>

<details><summary>Hint 3 — the approach</summary>

`restore` = `save(read(version))`. The only special logic lives in `save`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Store snapshots occasionally and small diffs in between (like Git packfiles or video keyframes).

</details>
