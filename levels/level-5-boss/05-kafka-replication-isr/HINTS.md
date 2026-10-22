# Hints — Replication, ISR and high watermark

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

The two hardest ideas are only a line of code each: *committed = min over the ISR of log length*, and *new leader = first of the ISR*. Build everything else around them.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`Map<String, MutableList<String>>` for logs, a `MutableList<String>` for the ISR (order matters!), a `Set<String>` of dead replicas, and a nullable `leader`.

</details>

<details><summary>Hint 3 — the approach</summary>

Write the "state after each operation" on paper for the example scenarios before coding: for `fail(leader)`, list: ISR before, ISR after, new leader, which logs get truncated.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

In real Kafka followers fetch continuously and the leader tracks each follower's progress; a follower that lags too long is dropped from the ISR automatically (`replica.lag.time.max.ms`).

</details>
