# Hints — Delivery slots with capacity and a waitlist

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

A slot is just two ordered lists (confirmed, waiting) plus a capacity. Promotion and demotion move names between the two lists.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Per slot: `confirmed: MutableList<String>`, `waiting: ArrayDeque<String>`, `capacity: Int`. A map `orderId → slotId` to find an order fast.

</details>

<details><summary>Hint 3 — the approach</summary>

Write one private `rebalance(slot)` that first demotes from the end of `confirmed` to the front of `waiting` while over capacity, then promotes from the front of `waiting` while there is room. Call it after every change.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Capacity checks need to be atomic per slot: one row per slot with a counter updated by `UPDATE … SET used = used + 1 WHERE used < capacity`.

</details>
