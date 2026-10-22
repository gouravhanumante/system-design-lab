# Hints — Order saga with compensations

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

"Try up to N times, logging each attempt" happens twice (actions and compensations). Write it once as a helper that takes the log words to use.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `MutableList<String>` log, a list of completed steps (a stack), and a `HashMap<String, SagaResult>` of finished sagas.

</details>

<details><summary>Hint 3 — the approach</summary>

`fun attempt(block: () -> Unit, firstLog: String, retryLog: (Int) -> String): Boolean` returns whether one of the tries succeeded. Then the forward loop and the reversed compensation loop are a few lines each.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Workflow engines (AWS Step Functions, Temporal, Camunda) persist every log entry so a saga survives crashes and can be resumed exactly where it stopped.

</details>
