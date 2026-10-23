class Inventory(private val holdSeconds: Long = 600) {

    // ───────────── State ─────────────

    private enum class HoldStatus { HELD, CONFIRMED, RELEASED }

    /** Stock reserved for one order while the customer pays. */
    private class Hold(val sku: String, val quantity: Int, val createdAt: Long) {
        var status = HoldStatus.HELD
    }

    /** sku → total units ever added. */
    private val stock = HashMap<String, Int>()

    /** sku → units sold (confirmed holds). */
    private val soldUnits = HashMap<String, Int>()

    /** order id → its hold (at most one per order). */
    private val holds = HashMap<String, Hold>()

    // ───────────── Helper ─────────────

    /** Step 1 — a hold blocks stock while it is HELD and not expired (`now < createdAt + holdSeconds`). */
    private fun isActive(hold: Hold, now: Long): Boolean {
        TODO("Step 1")
    }

    // ───────────── API ─────────────

    /** Step 1 — add units. quantity < 1 → `IllegalArgumentException`. */
    fun addStock(sku: String, quantity: Int) {
        TODO("Step 1")
    }

    /**
     * Steps 1 + 4 — reserve stock for [orderId]. Returns `false` if not enough is available.
     * - quantity < 1 → `IllegalArgumentException`.
     * - Step 4, the order already has a hold:
     *   - CONFIRMED → `IllegalArgumentException`;
     *   - still active and same sku + quantity → `true` (a retry, nothing changes); different sku/quantity → `IllegalArgumentException`;
     *   - expired or released → treat it like a new hold (below).
     * - New hold: if `available(sku, now) >= quantity` store a new Hold (createdAt = now) and return `true`.
     */
    fun hold(orderId: String, sku: String, quantity: Int, now: Long): Boolean {
        TODO("Step 1")
    }

    /**
     * Step 2 — payment succeeded: turn the hold into a sale. Already confirmed → `true` (idempotent).
     * Unknown order, or the hold is no longer active (expired/released) → `false`.
     */
    fun confirm(orderId: String, now: Long): Boolean {
        TODO("Step 2")
    }

    /** Step 3 — customer gave up: a HELD hold becomes RELEASED (confirmed sales stay sold). Unknown: do nothing. */
    fun release(orderId: String) {
        TODO("Step 3")
    }

    /** Step 1 — stock − sold − units in ACTIVE holds for that sku. */
    fun available(sku: String, now: Long): Int {
        TODO("Step 1")
    }

    /** Step 2 */
    fun sold(sku: String): Int {
        TODO("Step 2")
    }
}
