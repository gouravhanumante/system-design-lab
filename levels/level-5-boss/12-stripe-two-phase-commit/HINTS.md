# Hints — Two-phase commit across banks (UPI-style)

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

`execute` and `recover` share two jobs: "abort everyone and write done" and "commit everyone with retries and write done if it worked". Write those as private functions first.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Only the log. To recover, scan it once and build `txId → (involved, decision?, done?)` in begin order (`LinkedHashMap`).

</details>

<details><summary>Hint 3 — the approach</summary>

Parse lines with `split(" ")`: the first word is the record type, the second the txId, the third the participants (split by `,`) or the decision.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Modern databases replace the single coordinator with a replicated log (Raft/Paxos) so the "coordinator crashed" problem disappears — Google Spanner does 2PC over Paxos groups.

</details>
