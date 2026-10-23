enum class EventType { PLACED, ACCEPTED, PICKED_UP, DELIVERED, CANCELLED }

data class OrderEvent(val orderId: String, val version: Int, val type: EventType, val value: String = "")

data class OrderView(
    val orderId: String,
    val status: EventType,
    val items: List<String>,
    val rider: String?,
    val cancelReason: String?,
    val version: Int,
)

enum class ApplyResult { APPLIED, DUPLICATE, BUFFERED, REJECTED }

class OrderProjection {

    // ───────────── State ─────────────

    /** order id → current view (built only from events). */
    private val views = HashMap<String, OrderView>()

    /** order id → version of the last APPLIED event (0 if none). The next expected version is this + 1. */
    private val appliedVersion = HashMap<String, Int>()

    /** order id → events that arrived too early, by version. */
    private val buffered = HashMap<String, java.util.TreeMap<Int, OrderEvent>>()

    // ───────────── Helpers ─────────────

    /**
     * Step 1 — the view after applying [e] to [current], or `null` if this event isn't allowed now.
     * - No view yet: only PLACED is allowed → new view with status PLACED, items = `e.value` split on `,` (empty pieces dropped).
     * - ACCEPTED only after PLACED · PICKED_UP only after ACCEPTED (value = rider) · DELIVERED only after PICKED_UP
     *   · CANCELLED only after PLACED or ACCEPTED (value = reason) · PLACED again → not allowed.
     * - The new view's `version` = `e.version`. Tip: `current.copy(status = ..., version = ...)`.
     */
    private fun nextView(current: OrderView?, e: OrderEvent): OrderView? {
        TODO("Step 1")
    }

    /** Step 2 helper — run [nextView]; if allowed, store the view and the version and return `true`. */
    private fun applyNow(e: OrderEvent): Boolean {
        TODO("Step 2")
    }

    // ───────────── API ─────────────

    /**
     * Steps 2–4 — events can arrive late, early or twice. expected = appliedVersion + 1.
     * - version < expected → `DUPLICATE` (already applied).
     * - version > expected → keep it in `buffered` (once per version) → `BUFFERED`.
     * - version == expected → [applyNow]; not allowed → `REJECTED` (the version is NOT used up).
     *   Allowed → then DRAIN: while the buffer has the next expected version, remove and apply it; stop at a missing
     *   version or a rejected event. → `APPLIED`.
     */
    fun apply(e: OrderEvent): ApplyResult {
        TODO("Step 2")
    }

    /** Step 2 */
    fun view(orderId: String): OrderView? {
        TODO("Step 2")
    }

    /** Step 4 — events waiting in the buffer for that order. */
    fun bufferedCount(orderId: String): Int {
        TODO("Step 4")
    }
}

/** Step 5 — event sourcing: a brand-new projection with every event applied, in the given order. */
fun rebuild(events: List<OrderEvent>): OrderProjection {
    TODO("Step 5")
}
