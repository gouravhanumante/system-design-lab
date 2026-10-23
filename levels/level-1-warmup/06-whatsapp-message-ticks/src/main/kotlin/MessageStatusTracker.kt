enum class Status { SENT, DELIVERED, READ }   // ordered: SENT < DELIVERED < READ, so `a > b` works

class MessageStatusTracker {

    /** message id → its current tick status. */
    private val statuses = HashMap<String, Status>()

    /** Step 1 — a new message starts as SENT. Sending an id that already exists changes nothing. */
    fun send(messageId: String) {
        TODO("Step 1")
    }

    /**
     * Step 2 — change the status to [newStatus] only if it is further along than the current one.
     * Updates arrive out of order on real networks — a late "delivered" after "read" must NOT move it back.
     * Unknown message → ignore.
     */
    private fun moveForwardTo(messageId: String, newStatus: Status) {
        TODO("Step 2")
    }

    /** Step 2 — two grey ticks. */
    fun markDelivered(messageId: String) {
        TODO("Step 2")
    }

    /** Step 2 — two blue ticks (may come without DELIVERED first). */
    fun markRead(messageId: String) {
        TODO("Step 2")
    }

    /** Step 1 — current status, or `null` for an unknown message. */
    fun status(messageId: String): Status? {
        TODO("Step 1")
    }
}
