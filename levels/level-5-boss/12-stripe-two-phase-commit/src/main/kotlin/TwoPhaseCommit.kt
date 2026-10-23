/** Provided. */
interface Participant {
    fun prepare(txId: String): Boolean
    fun commit(txId: String)
    fun abort(txId: String)
}

enum class Outcome { COMMITTED, ABORTED, IN_DOUBT }

/*
 * The coordinator writes everything important to [log] (its "disk") BEFORE acting, so it can recover after a crash.
 * Exact log lines:
 *   "begin <txId> <p1,p2,...>"     – before asking anyone
 *   "decision <txId> COMMIT"        or "decision <txId> ABORT"
 *   "done <txId>"                   – every participant finished
 */
class Coordinator(
    private val participants: Map<String, Participant>,
    private val log: MutableList<String>,
    private val commitRetries: Int = 3,
) {

    /** Step 1 — tell every involved participant to abort (ignore exceptions), log `"done <txId>"`, return ABORTED. */
    private fun abortAll(txId: String, involved: List<String>): Outcome {
        TODO("Step 1")
    }

    /**
     * Step 2 — commit on each participant in order. A commit may throw: try it up to `1 + commitRetries` times.
     * If any participant still fails → stop and return IN_DOUBT WITHOUT logging "done" (recovery must finish it).
     * All committed → log `"done <txId>"`, return COMMITTED.
     */
    private fun commitAll(txId: String, involved: List<String>): Outcome {
        TODO("Step 2")
    }

    /**
     * Steps 1–2 — run one transaction.
     * - [involved] empty or containing an unknown participant, or [txId] already begun in the log → `IllegalArgumentException`.
     * - Log `begin`. Phase 1: ask each participant to `prepare`, in order; `false` or an exception = NO → stop asking.
     * - All YES → log `decision … COMMIT`, then [commitAll]. Any NO → log `decision … ABORT`, then [abortAll].
     */
    fun execute(txId: String, involved: List<String>): Outcome {
        TODO("Step 1")
    }

    /**
     * Step 3 — after a crash, using ONLY the log: for every transaction with `begin` but no `done` (in log order):
     * - no decision → log `decision … ABORT` and abort (nobody was told to commit, so aborting is safe);
     * - decision ABORT → [abortAll]; decision COMMIT → [commitAll].
     * Return txId → outcome for the transactions handled.
     */
    fun recover(): Map<String, Outcome> {
        TODO("Step 3")
    }
}
