enum class State { CLOSED, OPEN, HALF_OPEN }

class CircuitBreaker(
    private val failureThreshold: Int,
    private val openMillis: Long,
    private val halfOpenSuccesses: Int,
    private val clock: () -> Long,
) {

    // ───────────── State ─────────────

    private var state = State.CLOSED
    /** CLOSED: failures in a row. */
    private var consecutiveFailures = 0
    /** HALF_OPEN: successful probe calls so far. */
    private var probeSuccesses = 0
    /** When the breaker last opened (clock time). */
    private var openedAt = 0L

    /** Calls answered with the fallback WITHOUT calling the action (because the breaker was open). */
    var rejected: Int = 0
        private set

    /** Step 1 — `failureThreshold <= 0`, `openMillis < 0` or `halfOpenSuccesses <= 0` → `IllegalArgumentException`. */
    init {
        // TODO Step 1
    }

    /**
     * Steps 2–4 — call the remote service through the breaker. "Fails" = [action] throws an Exception.
     * Read `now = clock()` once at the start.
     *
     * OPEN: if `now - openedAt < openMillis` → `rejected++`, return `fallback()` (don't call the action).
     *       Otherwise the cooldown is over → switch to HALF_OPEN (probeSuccesses = 0) and continue below.
     * Call [action]:
     * - success: CLOSED → consecutiveFailures = 0. HALF_OPEN → probeSuccesses++; at [halfOpenSuccesses] → CLOSED (reset counters).
     *   Return the result.
     * - failure: CLOSED → consecutiveFailures++; at [failureThreshold] → OPEN (openedAt = now, reset the count).
     *            HALF_OPEN → straight back to OPEN with a fresh timer (openedAt = now).
     *   Return `fallback()`.
     */
    fun <T> call(action: () -> T, fallback: () -> T): T {
        TODO("Step 2")
    }

    /** Step 1 — the current state (as stored; it only changes inside `call`). */
    fun state(): State {
        TODO("Step 1")
    }
}
