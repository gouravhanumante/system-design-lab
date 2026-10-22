# Hints — Idempotency keys for a payment API

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Store a small record per key: the request, the response and when it was stored.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`HashMap<String, Stored(request, response, storedAt)>`. Data classes give you `==` on requests for free.

</details>

<details><summary>Hint 3 — the approach</summary>

Call the processor **before** writing the record, so an exception naturally leaves nothing behind. Return `response.copy(replayed = true)` for replays.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Stripe stores keys in the same database transaction as the charge, so "charge exists" and "key recorded" can never disagree.

</details>
