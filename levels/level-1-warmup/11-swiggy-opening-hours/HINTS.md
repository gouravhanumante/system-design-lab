# Hints — Is the restaurant open?

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Turn `(day, "HH:MM")` into a single number: `day × 1440 + hours × 60 + minutes`. A week has 10,080 minutes.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A list of `(startMinute, endMinute)` pairs where `end` may be larger than 10,080 for intervals that wrap past Sunday night.

</details>

<details><summary>Hint 3 — the approach</summary>

A moment `m` is inside `[s, e)` if `m in s until e` **or** `m + 10080 in s until e` (the second check handles the wrap). For `nextOpening`, the distance to a start is `Math.floorMod(s - m, 10080)`; pick the smallest.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Store hours in local time plus the restaurant's time-zone id; convert "now" into that zone before checking (daylight-saving changes make fixed offsets wrong in many countries).

</details>
