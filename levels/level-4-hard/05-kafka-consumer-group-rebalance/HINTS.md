# Hints — Group coordinator and rebalancing

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Separate **membership** (who is in the group, when did we last hear from them) from **assignment** (a pure function of the sorted member list and the partition count).

</details>

<details><summary>Hint 2 — which data structure?</summary>

`HashMap<String, Long>` member → last heartbeat; an `assignment` map you rebuild on every rebalance; an `Int` generation.

</details>

<details><summary>Hint 3 — the approach</summary>

Write `rebalance()` that increments the generation and recomputes the assignment from `members.keys.sorted()`. Every event that changes membership calls it exactly once.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Real coordinators run a join *protocol* (collect everyone, then assign) so that members never act on a half-finished assignment.

</details>
