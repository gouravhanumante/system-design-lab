# Hints — "Share trip" route deviation alerts

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Split the job: (1) "how far is this point from the route?" (pure geometry), (2) "should this make an alert?" (a small counter). Test them mentally one at a time.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Per trip: the route list, an `offStreak` counter, and an `alerted` flag, plus a list of alert times.

</details>

<details><summary>Hint 3 — the approach</summary>

Distance from point P to segment A→B: project P onto the line, clamp the projection factor `t` into `[0, 1]`, measure to that closest point. Compare **squared** distances to avoid square roots. On an on-route ping set `offStreak = 0` and `alerted = false`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Index route segments in a grid (like Uber Level 3) so each ping only tests nearby segments.

</details>
