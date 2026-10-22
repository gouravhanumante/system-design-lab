# Hints — Live demand heatmap (hot zones)

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Don't remember individual requests. Remember "how many requests per zone happened in each 10-second slice".

</details>

<details><summary>Hint 2 — which data structure?</summary>

A queue of buckets (each bucket = bucket index + map zone→count), plus a *running total* map zone→count that you update as buckets enter and leave.

</details>

<details><summary>Hint 3 — the approach</summary>

On `record`: expire old buckets first (subtract their counts from the totals), then add to the newest bucket and to the totals. On `topZones`: expire, then sort the totals.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Sorting all zones for every query is wasteful if you only need `k`. A heap of size `k`, or a periodically refreshed cached result, scales better.

</details>
