# Hints — Video view counter and top videos

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

You are counting things by name. What structure maps a name to a number?

</details>

<details><summary>Hint 2 — which data structure?</summary>

`HashMap<String, Long>`; Kotlin's `merge` or `getOrDefault` keeps the increment short.

</details>

<details><summary>Hint 3 — the approach</summary>

For `top`, sort the entries by `(-views, id)` and take `n`. (A heap of size `n` is the efficient version.)

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

A single hot counter becomes a bottleneck. Level 4 of this app tackles exactly that.

</details>
