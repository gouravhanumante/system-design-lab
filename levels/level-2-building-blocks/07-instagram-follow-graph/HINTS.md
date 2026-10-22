# Hints — Follow graph and "suggested for you"

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Keep each edge in **two** places so both directions are fast to look up: "who does X follow" and "who follows X".

</details>

<details><summary>Hint 2 — which data structure?</summary>

Two maps of sets: `out[user]` (following) and `into[user]` (followers). `follow`/`unfollow` update both.

</details>

<details><summary>Hint 3 — the approach</summary>

For suggestions: for every `f` in `following(user)`, for every `c` in `following(f)` (skipping `user` and already-followed), do `score[c]++`. Then sort by `(-score, id)`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Graph databases and "who to follow" systems precompute and cache candidate lists offline (Level 5 of Twitter looks at this).

</details>
