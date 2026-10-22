# Hints — Load balancer with health checks

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Keep the servers in a list (order matters for both strategies) and a small state object per server.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`LinkedHashMap<String, Server(healthy, failStreak, passStreak, active)>` and an `Int` cursor for round robin.

</details>

<details><summary>Hint 3 — the approach</summary>

Round robin: try positions `cursor, cursor+1, …` (mod size) up to `size` times; when you find a healthy server at position `i`, set `cursor = i + 1`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

**Consistent hashing** (Netflix L3) is another strategy: the same key always goes to the same server, which is great for caches.

</details>
