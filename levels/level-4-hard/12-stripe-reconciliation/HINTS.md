# Hints — Reconciling the ledger with the bank statement

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Implement the passes literally, one after another, with two `used` markers (one per side).

</details>

<details><summary>Hint 2 — which data structure?</summary>

`BooleanArray(bank.size)` for used bank lines, a `HashSet<String>` for used ledger ids. A map `ref → list of bank indexes` speeds up pass 1.

</details>

<details><summary>Hint 3 — the approach</summary>

For pass 2, filter candidates by amount, tolerance and unused, then `minWithOrNull(compareBy({ |diff| }, { day }, { index }))`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Group both sides by amount (or by `(amount, day)`) so pass 2 only looks at a handful of candidates — the same "index first, then check exactly" idea as the geo grid.

</details>
