class Step(val name: String, val action: () -> Unit, val compensate: () -> Unit)

enum class SagaStatus { COMPLETED, COMPENSATED, FAILED }

data class SagaResult(val status: SagaStatus, val log: List<String>)

class SagaOrchestrator(private val maxRetries: Int = 2) {

    /** saga id → its result. Running the same id again returns this instead of running anything. */
    private val finished = HashMap<String, SagaResult>()

    /** Step 1 — `maxRetries < 0` → `IllegalArgumentException`. */
    init {
        // TODO Step 1
    }

    /**
     * Step 1 — try [block] up to `1 + maxRetries` times; "fails" = throws an Exception. Stop at the first success.
     * Before each try, add a line to [log]: the first try logs [firstLine], retry number k (1, 2, …) logs `retryLine(k)`.
     * Returns `true` if a try succeeded.
     */
    private fun runWithRetries(log: MutableList<String>, block: () -> Unit, firstLine: String, retryLine: (Int) -> String): Boolean {
        TODO("Step 1")
    }

    /**
     * Steps 2–4 — run the steps in order. Log lines (exact text matters):
     * - each step: tries logged as `"do:<name>"`, `"retry:<name>#1"`, `"retry:<name>#2"`…; then `"ok:<name>"` or `"fail:<name>"`.
     * - all ok → COMPLETED.
     * - a step fails for good → compensate the COMPLETED steps in REVERSE order: tries logged as `"undo:<name>"`,
     *   `"retry-undo:<name>#1"`…; then `"undone:<name>"`. All undone → COMPENSATED.
     *   If a compensation fails for good → log `"stuck:<name>"`, stop, status FAILED (needs a human).
     * - Step 4: remember the result by [sagaId]; the same id again returns the stored result without running anything.
     */
    fun run(sagaId: String, steps: List<Step>): SagaResult {
        TODO("Step 2")
    }
}
