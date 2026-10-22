# Hints — A REST API for issues

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

First decide *which route* a request is (collection `/issues` or item `/issues/{id}`), then *which method*. That's two small `when`s.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `TreeMap<Int, MutableMap<String, String>>` keeps issues sorted by id; a counter for the next id.

</details>

<details><summary>Hint 3 — the approach</summary>

Split the path on `/`: `["", "issues"]` or `["", "issues", "42"]`. Validate the whole request before changing anything, so a `400` never leaves half an update behind.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Real APIs add authentication, rate limiting (Twitter L4), versioning (`/v2/issues`) and ETags for caching and optimistic concurrency (Drive L5).

</details>
