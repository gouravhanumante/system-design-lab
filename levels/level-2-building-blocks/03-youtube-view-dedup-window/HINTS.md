# Hints — Count a view only once per viewer

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

What is the minimum you need to remember about each `(user, video)` pair to make this decision?

</details>

<details><summary>Hint 2 — which data structure?</summary>

A map from the pair (e.g. `"$userId|$videoId"` or a `Pair`) to the time of the last *counted* view, plus a counter per video.

</details>

<details><summary>Hint 3 — the approach</summary>

If there is no entry, or `now - last >= windowSeconds`: count it, store `now`, increment the video counter, return `true`. Otherwise return `false` and change nothing.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Entries older than the window are useless — expire them (a TTL in Redis does this for free).

</details>
