# Hints — Group chat fan-out and "read by all"

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Each message carries its own little record: *who it was for*. Membership changes later don't touch old records.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `Message` class with `text`, `recipients: Set<String>`, `readers: MutableSet<String>`. A `LinkedHashMap<String, Message>` keeps send order. A `Set<String>` for current members.

</details>

<details><summary>Hint 3 — the approach</summary>

`send`: `recipients = members - sender`. `inbox(u)`: filter messages whose recipients contain `u`. `isReadByAll`: `(recipients ∩ members) ⊆ readers`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Filtering all messages for `inbox` is O(messages). Real systems keep a per-user inbox (fan-out on write) or a per-group log plus a per-user cursor (fan-out on read) — Level 4 of Instagram explores that trade-off.

</details>
