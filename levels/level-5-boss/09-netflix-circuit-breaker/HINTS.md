# Hints — Circuit breaker with fallbacks

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Draw the three states and the arrows between them before writing code. Each arrow is one `if`.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Just fields: `state`, `failures`, `successes`, `openedAt`, `rejected`.

</details>

<details><summary>Hint 3 — the approach</summary>

At the top of `call`: if OPEN and the cool-down passed, set state to HALF_OPEN. Then `when (state)` with three branches. Wrap the action in `try { … } catch (e: Exception) { … }`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Breakers combine with **timeouts** (a slow call counts as a failure) and **bulkheads** (separate thread pools per dependency) — Hystrix did all three; today Resilience4j does.

</details>
