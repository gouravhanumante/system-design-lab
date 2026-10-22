# Hints — Adaptive bitrate: picking video quality

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

There are two separate parts: **estimating** bandwidth (a small list of numbers) and **deciding** (a few ordered rules). Build and test them separately.

</details>

<details><summary>Hint 2 — which data structure?</summary>

An `ArrayDeque<Double>` of samples (drop the oldest when you have more than `window`), and an index into the ladder for the current rung.

</details>

<details><summary>Hint 3 — the approach</summary>

Work with ladder **indexes**, not kbps values: target index = last index whose value fits; switching up = `current + 1`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

The server side matters too: each segment is encoded in every rung ahead of time (and Netflix encodes per-title / per-shot to save bits).

</details>
