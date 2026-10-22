# Hints — Surge pricing for a zone

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Old requests stop mattering. What should happen to them as time moves forward?

</details>

<details><summary>Hint 2 — which data structure?</summary>

Timestamps arrive in order and the oldest ones expire first. A queue (`ArrayDeque`) per zone fits perfectly.

</details>

<details><summary>Hint 3 — the approach</summary>

On every `multiplier` call, remove from the front of the queue while `now - front >= windowSeconds`, then use the queue size as demand.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Per-request storage grows with traffic. Per-second bucket counters use fixed memory (60 numbers per zone) no matter how many requests come in.

</details>
