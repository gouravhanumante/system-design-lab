# Hints — Sharing, groups and inherited permissions

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Don't compute permissions when sharing. Just store the explicit grants. All the cleverness happens at query time in `permissionOf`.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `Folder` node with `parent`, `grants: MutableMap<String, Permission>` and `inheritanceBroken: Boolean`, plus `groups: Map<String, Set<String>>` or `memberOf: Map<user, Set<group>>`.

</details>

<details><summary>Hint 3 — the approach</summary>

For a user, the set of "names I answer to" is `{user} ∪ groupsOf(user)`. Walk from the folder to the root: at each folder take the max over grants for those names; if the folder has broken inheritance, stop after it. Reuse `permissionOf(by, path)` for all the authority checks.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Google's Zanzibar paper describes exactly this problem at planet scale: relation tuples, a check API, and consistency tokens ("zookies").

</details>
