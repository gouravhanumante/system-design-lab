# Hints — The append-only log

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

An offset is just a position in a list. What is the simplest structure where position = number?

</details>

<details><summary>Hint 2 — which data structure?</summary>

An `ArrayList<String>`. The offset of a record *is* its index.

</details>

<details><summary>Hint 3 — the approach</summary>

`read` = slice from `fromOffset` to `min(fromOffset + max, size)`, mapped to `Record(index, value)`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Real logs are split into **segment files**; to find offset N you pick the right file by its starting offset and then use a sparse index inside it.

</details>
