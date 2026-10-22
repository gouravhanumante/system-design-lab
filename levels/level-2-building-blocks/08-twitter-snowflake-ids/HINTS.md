# Hints — Snowflake IDs

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Remember two things between calls: the timestamp of the last id and the last sequence number.

</details>

<details><summary>Hint 2 — which data structure?</summary>

No collections needed — just a few `Long`/`Int` fields and bit operations: `shl`, `shr`, `or`, `and`.

</details>

<details><summary>Hint 3 — the approach</summary>

To extract bits use a shift and a mask: `sequence = (id and 0xFFF).toInt()`, `worker = ((id shr 12) and 0x3FF).toInt()`, `timestamp = id shr 22`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Compare with UUIDv4 (random, not sortable, 128 bits) and UUIDv7 / ULID (time-sorted, 128 bits, no coordination at all). Which would you pick for a database primary key and why?

</details>
