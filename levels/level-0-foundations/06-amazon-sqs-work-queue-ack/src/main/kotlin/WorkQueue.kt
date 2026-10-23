data class Message(val id: String, val body: String, val receipt: String, val receiveCount: Int)

class WorkQueue(private val visibilityTimeoutSeconds: Long = 30, private val maxReceives: Int = 3) {

    // ───────────── State ─────────────

    /** One message waiting in the queue (or currently being worked on). */
    private class Entry(val id: String, val body: String) {
        /** How many times it has been received so far. */
        var count = 0
        /** Hidden while `now < invisibleUntil`. A new message is visible immediately. */
        var invisibleUntil = Long.MIN_VALUE
        /** The receipt from the LATEST receive — only this one can ack/extend. */
        var receipt = ""
    }

    /** id → entry, oldest first (LinkedHashMap keeps send order = FIFO). Acked messages are removed. */
    private val entries = LinkedHashMap<String, Entry>()

    /** Ids moved to the dead-letter queue, in the order they were moved. */
    private val dead = ArrayList<String>()

    /** Next number for ids `m1`, `m2`, … */
    private var nextId = 1

    // ───────────── API ─────────────

    /** Step 1 — add a message and return its id (`"m1"`, `"m2"`, …). */
    fun send(body: String): String {
        TODO("Step 1")
    }

    /**
     * Steps 2 + 5 — give the oldest *visible* message to a worker, or `null` if none.
     * - Visible means `now >= invisibleUntil`.
     * - Step 5: if a visible message was already received `maxReceives` times, move its id to `dead`,
     *   remove it from `entries`, and keep looking.
     * - Otherwise: `count++`, `invisibleUntil = now + visibilityTimeoutSeconds`,
     *   `receipt = "<id>#<count>"` (e.g. `"m1#2"`), and return it as a [Message].
     */
    fun receive(now: Long): Message? {
        TODO("Step 2")
    }

    /**
     * Step 3 helper — the entry this receipt belongs to, but only if the receipt is still valid:
     * the entry exists, [receipt] equals its latest receipt, and it is still invisible (`now < invisibleUntil`). Otherwise `null`.
     * Tip: the id is the part before `#`.
     */
    private fun findByReceipt(receipt: String, now: Long): Entry? {
        TODO("Step 3")
    }

    /** Step 3 — work finished: delete the message. Invalid/expired/unknown receipt → `false`. */
    fun ack(receipt: String, now: Long): Boolean {
        TODO("Step 3")
    }

    /** Step 4 — "I need more time": `invisibleUntil = now + extraSeconds`. Invalid receipt → `false`. */
    fun extendVisibility(receipt: String, extraSeconds: Long, now: Long): Boolean {
        TODO("Step 4")
    }

    /** Step 5 — ids in the dead-letter queue, oldest first. */
    fun deadLetters(): List<String> {
        TODO("Step 5")
    }

    /** Step 6 — messages (not dead, not acked) that are visible at [now]. */
    fun visibleCount(now: Long): Int {
        TODO("Step 6")
    }

    /** Step 6 — messages that are hidden (being worked on) at [now]. */
    fun inFlightCount(now: Long): Int {
        TODO("Step 6")
    }
}
