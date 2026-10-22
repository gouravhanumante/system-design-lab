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

class JobScheduler(
    private val workers: Int,
    private val maxAttempts: Int = 3,
    private val failures: (jobId: String, attempt: Int) -> Boolean = { _, _ -> false },
) {

    /** Validates the jobs (duplicates, unknown dependencies, cycles) and stores them. */
    fun submit(jobs: List<Job>) {
        TODO()
    }

    /** Simulates execution and reports what happened. */
    fun run(): Schedule {
        TODO()
    }
}
