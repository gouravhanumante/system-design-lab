# Hints — Double-entry ledger

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Validate the arguments first (exceptions), then check the idempotency record, then decide success/failure, then write.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`accounts: Map<String, Account(allowNegative, entries: MutableList<Long to String>)>` and `results: Map<txId, Boolean>`.

</details>

<details><summary>Hint 3 — the approach</summary>

Balances can be computed by summing entries each time — fine at this scale. `sumOfBalances` is the sum of every account's balance.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

In a database both entries must be written in **one transaction** — a crash between the two would break the zero-sum invariant.

</details>
