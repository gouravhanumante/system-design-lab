# Hints — Key-value store with transactions

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Think in **layers**: committed data at the bottom, one layer of changes per open transaction on top. Reading looks from the top layer down.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `HashMap<String, String>` for committed data and an `ArrayList<HashMap<String, String?>>` used as a stack of layers. A `null` value inside a layer means "deleted in this transaction" (a **tombstone**).

</details>

<details><summary>Hint 3 — the approach</summary>

`get`: walk layers from top to bottom; the first layer that **contains the key** decides (even if its value is `null`). `commit`: pop the top layer and copy its entries into the layer below (or apply them to the committed map, removing tombstoned keys).

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Real databases use MVCC (multi-version concurrency control): each row keeps several versions so readers never block writers.

</details>
