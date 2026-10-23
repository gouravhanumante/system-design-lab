data class Job(
    val id: String,
    val durationSeconds: Int,
    val dependsOn: List<String> = emptyList(),
    val priority: Int = 0,
)

data class Schedule(
    val finishTimes: Map<String, Long>,
    val failed: Set<String>,
    val skipped: Set<String>,
    val makespan: Long,
)

/** A job attempt that is currently running on a worker. */
private data class RunningJob(val endsAt: Long, val jobId: String, val attempt: Int)

class JobScheduler(
    private val workers: Int,
    private val maxAttempts: Int = 3,
    private val failures: (jobId: String, attempt: Int) -> Boolean = { _, _ -> false },
) {

    /** The submitted jobs by id. */
    private var jobs: Map<String, Job> = emptyMap()

    // ───────────── Submitting ─────────────

    /**
     * Step 1 — check and store the jobs. Any problem → `IllegalArgumentException`:
     * - two jobs with the same id
     * - a `dependsOn` id that isn't one of the jobs
     * - a dependency cycle (Step 2: use [hasCycle])
     */
    fun submit(jobs: List<Job>) {
        TODO("Step 1")
    }

    /**
     * Step 2 — `true` if the dependencies contain a cycle (A needs B, B needs A).
     * Kahn's algorithm: keep removing jobs whose dependencies are all removed already;
     * if you get stuck before every job is removed, there is a cycle.
     */
    private fun hasCycle(jobs: Map<String, Job>): Boolean {
        TODO("Step 2")
    }

    // ───────────── Running (a simulation — time is just a number) ─────────────

    /**
     * Step 3 — the job a free worker should start next, or `null`:
     * among [pending] jobs whose dependencies are ALL in [done], the highest `priority`; tie → smaller id.
     */
    private fun pickNextReady(pending: Set<String>, done: Set<String>): String? {
        TODO("Step 3")
    }

    /** Step 5 — after [failedJob] fails for good: every job that depends on it (directly or indirectly) is skipped and removed from [pending]. */
    private fun skipDependents(failedJob: String, pending: MutableSet<String>, skipped: MutableSet<String>) {
        TODO("Step 5")
    }

    /**
     * Steps 4–5 — simulate. Start at `now = 0` and repeat:
     * 1. Every running attempt with `endsAt == now` ends (handle them in job-id order).
     *    - `failures(jobId, attempt)` is false → done; `finishTimes[jobId] = now`.
     *    - true and `attempt < maxAttempts` → back into pending (it retries, competing normally by priority).
     *    - true and `attempt == maxAttempts` → failed; skip everything that depends on it.
     * 2. While a worker is free (`running.size < workers`) and [pickNextReady] finds a job: start its next attempt
     *    (attempt numbers start at 1), ending at `now + durationSeconds`.
     * 3. Nothing running → stop. Otherwise jump `now` to the smallest `endsAt`.
     * `makespan` = `now` when the loop stops (0 if there were no jobs).
     */
    fun run(): Schedule {
        TODO("Step 4")
    }
}
