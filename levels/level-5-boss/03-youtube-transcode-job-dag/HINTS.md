# Hints — Video processing pipeline (job DAG scheduler)

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Don't think in "steps of 1 second". Think in **events**: the only moments anything changes are when a job ends. Jump from one such moment to the next.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A set of `done` jobs, a list of currently `running` attempts `(endTime, jobId, attemptNo)`, a collection of `pending` jobs, and an attempt counter per job.

</details>

<details><summary>Hint 3 — the approach</summary>

Loop: (1) complete everything ending at `now`; (2) while a worker is free, pick the best ready pending job and start it; (3) if nothing is running, stop — otherwise set `now` to the earliest end time. Cycle check: repeatedly remove jobs with no remaining dependencies (Kahn's algorithm); leftovers mean a cycle.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Real pipelines (Airflow, Temporal, Borg) store job state in a database and workers *pull* ready jobs, instead of one process pushing them.

</details>
