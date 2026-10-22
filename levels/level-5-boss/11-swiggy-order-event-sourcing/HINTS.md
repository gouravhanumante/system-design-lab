# Hints — Order tracking with event sourcing

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

There are two separate concerns: **ordering** (versions, buffer, duplicates) and **business rules** (which transition is allowed). Write a pure function `transition(view?, event): OrderView?` that returns `null` when invalid.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`views: HashMap<String, OrderView>`, `versions: HashMap<String, Int>`, `buffers: HashMap<String, java.util.TreeMap<Int, OrderEvent>>`.

</details>

<details><summary>Hint 3 — the approach</summary>

In `apply`, after an APPLIED event: `while (buffer contains expected) { take it, remove it, transition; if invalid break }`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Kafka with the order id as key gives per-order ordering for free — then versions mostly guard against duplicates and replays.

</details>
