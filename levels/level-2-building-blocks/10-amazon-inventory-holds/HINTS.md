# Hints — Inventory holds during checkout

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Each order has one hold record with a status. Most rules become simple once you write a function `isActive(hold, now)`.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`stock: Map<sku, Int>`, `sold: Map<sku, Int>`, `holds: Map<orderId, Hold(sku, qty, createdAt, status)>` where status is HELD / CONFIRMED / RELEASED.

</details>

<details><summary>Hint 3 — the approach</summary>

`available(sku, now)` = stock − sold − sum of quantities of holds for that SKU that are HELD and not expired. Every decision calls it, so expiry is handled "lazily" for free.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Summing all holds on every check is O(holds). A real system keeps a running `held` counter and a queue of expiry times to subtract from it.

</details>
