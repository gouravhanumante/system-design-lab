# Hints — Resumable chunked upload

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Think of an array of `totalChunks` slots, each empty or filled. Everything else follows from that picture.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`Array<ByteArray?>(totalChunks) { null }` — null means missing.

</details>

<details><summary>Hint 3 — the approach</summary>

Check the rules in a fixed order: range → checksum → duplicate → store. `assemble` can use a `ByteArrayOutputStream` or `reduce` over the filled slots.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Keep only a bitmap of received chunk numbers in the session store and write the bytes straight to object storage.

</details>
