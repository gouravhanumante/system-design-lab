# Hints — Message ticks (sent, delivered, read)

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

You only ever *raise* a status. "Raise" means: new status is applied only if it is greater than the current one.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`HashMap<String, Status>`. Kotlin enums are `Comparable` by declaration order.

</details>

<details><summary>Hint 3 — the approach</summary>

One private function `raise(id, to)`: `val cur = map[id] ?: return; if (to > cur) map[id] = to`. Both `markDelivered` and `markRead` call it.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

This "only move forward" rule is a tiny **CRDT** (a max-register): merging updates in any order gives the same result.

</details>
