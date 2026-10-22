# Hints — Match many requests to many captains

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Imagine writing every possible (rider, captain) pair on a card with its distance. Which card do you pick first? Then what do you do with the cards that mention the same rider or captain?

</details>

<details><summary>Hint 2 — which data structure?</summary>

A list of all pairs sorted by `(distance, riderId, captainId)`. Two sets (or booleans) remember who is already taken.

</details>

<details><summary>Hint 3 — the approach</summary>

Walk the sorted pairs once. Take a pair only if both its rider and its captain are still free. You never need to re-sort.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

n×m pairs explode. Use the grid index from Uber Level 3 so each rider only considers captains in nearby cells.

</details>
