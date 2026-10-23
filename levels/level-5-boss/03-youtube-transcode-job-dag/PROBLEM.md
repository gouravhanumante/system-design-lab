# Video processing pipeline (job DAG scheduler)

| App | Level | Concept |
|-----|-------|---------|
| YouTube | 5 — Boss | Dependency graph, worker pool simulation, retries, failure propagation |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

After you upload a video YouTube does *a lot*: transcode into 360p / 720p / 1080p, generate thumbnails, run content checks, and only then publish. Some steps depend on others, a worker machine can only do one job at a time, jobs sometimes fail and must be retried, and if a step fails for good everything depending on it must be abandoned.

## Build this (small scale)

A scheduler that takes a set of jobs with dependencies and **simulates** running them on `workers` machines, returning when each job finished.

## API

```kotlin
data class Job(val id: String, val durationSeconds: Int, val dependsOn: List<String> = emptyList(), val priority: Int = 0)

data class Schedule(
    val finishTimes: Map<String, Long>,   // only successful jobs
    val failed: Set<String>,              // jobs that ran out of attempts
    val skipped: Set<String>,             // jobs never run because something they depend on failed
    val makespan: Long,                   // time of the last event (0 if there were no jobs)
)

class JobScheduler(workers: Int, maxAttempts: Int = 3, failures: (jobId: String, attempt: Int) -> Boolean = { _, _ -> false }) {
    fun submit(jobs: List<Job>)
    fun run(): Schedule
}
```

## Rules

- `submit` throws `IllegalArgumentException` for duplicate ids, a dependency on an unknown job, or a dependency **cycle**.
- Time starts at `0`. A job is **ready** when all of its dependencies finished successfully.
- Whenever a worker is free and a job is ready, start the ready job with the **highest `priority`** (ties: smaller id). Never run more than `workers` jobs at once.
- A job attempt runs for `durationSeconds`. When it ends, `failures(jobId, attemptNumber)` (attempt numbers start at 1) says whether that attempt failed.
  - Success → record `finishTimes[job] = endTime`.
  - Failure with attempts left → the job becomes ready again **immediately** (same priority) and will be re-started like any ready job.
  - Failure on the last allowed attempt (`attempt >= maxAttempts`) → the job is `failed`, and every job that depends on it, directly or indirectly, is `skipped`.
- Events at the same instant: first process **all** jobs ending at that time (in id order), then start new jobs.
- `makespan` is the time at which the last running attempt ended.

## Example

`a(10)`, `b(5, after a)`, `c(5, after b)` with 2 workers → finish times `a=10, b=15, c=20`.

## Think about (answer in NOTES.md)

- Retrying instantly can hammer a struggling dependency. What would **backoff** change in your simulation?
- A worker machine dies halfway through a job. How would a real scheduler notice and recover?
- How would you persist this so a restart does not lose progress?

---

## Run the tests

```bash
./gradlew :l5-youtube-transcode-job-dag:test
```

Your code goes in `src/main/kotlin/JobScheduler.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
