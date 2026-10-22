# Hints — Online status, last seen and privacy

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

`view` is a small decision tree: first "may the viewer see?", then "what is the target's state?". Write the first part as its own private function.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`lastBeat: Map<String, Long>`, `visibility: Map<String, Visibility>` (default EVERYONE), `contacts: Map<String, Set<String>>`.

</details>

<details><summary>Hint 3 — the approach</summary>

`canSee(viewer, target)`: self → true; viewer's own visibility NOBODY → false; then check the target's visibility (EVERYONE / CONTACTS with the *target's* contact list / NOBODY → false). The output strings follow directly.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Presence is ephemeral: keep it in memory/Redis with a TTL equal to the timeout instead of a durable database.

</details>
