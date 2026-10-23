class WebhookDispatcher(
    private val send: (endpoint: String, eventId: String) -> Boolean,
    private val maxAttempts: Int = 5,
    private val baseDelaySeconds: Long = 10,
) {

    // ───────────── State ─────────────

    private enum class DeliveryState { PENDING, DELIVERED, DEAD }

    /** One event that must reach one endpoint. */
    private class Delivery(val eventId: String, val endpoint: String, var dueAt: Long) {
        var attempts = 0
        var state = DeliveryState.PENDING
        /** How it appears in logs and lists: `"<eventId>-><endpoint>"`. */
        val name get() = "$eventId->$endpoint"
    }

    /** (eventId, endpoint) → delivery. */
    private val deliveries = LinkedHashMap<Pair<String, String>, Delivery>()

    /** Names of delivered deliveries, in delivery order. */
    private val deliveredNames = ArrayList<String>()

    /** Names in the dead-letter queue, in the order they died. */
    private val deadNames = ArrayList<String>()

    // ───────────── Helper ─────────────

    /** Step 3 — wait after failed attempt number n (1, 2, 3…): `baseDelaySeconds × 2^(n-1)` → 10, 20, 40, … */
    private fun retryDelay(attempt: Int): Long {
        TODO("Step 3")
    }

    // ───────────── API ─────────────

    /** Step 1 — new delivery, due right away (`dueAt = now`). The same (event, endpoint) again → ignore. */
    fun enqueue(eventId: String, endpoint: String, now: Long) {
        TODO("Step 1")
    }

    /**
     * Steps 2–3 — try every PENDING delivery with `dueAt <= now`, ordered by dueAt, then eventId, then endpoint.
     * Each one is tried once per tick: `attempts++`, call `send(endpoint, eventId)` (an exception = failure).
     * - success → DELIVERED, add to the delivered list, log `"<name> #<attempt> ok"`.
     * - failure → log `"<name> #<attempt> fail"`; if attempts reached [maxAttempts] → DEAD (add to dead letters),
     *   otherwise `dueAt = now + retryDelay(attempts)`.
     * Return this tick's log lines in order.
     */
    fun tick(now: Long): List<String> {
        TODO("Step 2")
    }

    /** Step 4 — retry a DEAD delivery from scratch: remove from dead letters, PENDING, attempts = 0, due now → `true`. Otherwise `false`. */
    fun redrive(eventId: String, endpoint: String, now: Long): Boolean {
        TODO("Step 4")
    }

    /** Step 1 — copy. */
    fun delivered(): List<String> {
        TODO("Step 1")
    }

    /** Step 1 — copy. */
    fun deadLetters(): List<String> {
        TODO("Step 1")
    }

    /** Step 1 — number of PENDING deliveries. */
    fun pending(): Int {
        TODO("Step 1")
    }
}
