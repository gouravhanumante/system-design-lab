# Hints — Webhook delivery with backoff and a dead-letter queue

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Each delivery needs: its key, attempt count, next due time and state (pending / delivered / dead).

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `LinkedHashMap<Pair<String, String>, Delivery>` for all deliveries, plus two lists for the delivered and dead-letter order.

</details>

<details><summary>Hint 3 — the approach</summary>

In `tick`, first **collect and sort** the due deliveries, then attempt them. A delivery rescheduled during this tick won't be retried in the same tick because you are iterating over the list you collected at the start. `1L shl (attempt - 1)` is `2^(attempt-1)`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Pending deliveries live in a durable queue sorted by due time (a "delay queue"); many workers pull from it. Endpoints failing for days are automatically disabled.

</details>
