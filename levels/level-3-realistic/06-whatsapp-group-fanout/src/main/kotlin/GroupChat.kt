class GroupChat {

    // ───────────── State ─────────────

    /** One group message. [recipients] = the members at the moment it was sent, minus the sender (fixed forever). */
    private class GroupMessage(val text: String, val recipients: Set<String>) {
        /** Recipients who have read it. */
        val readers = LinkedHashSet<String>()
    }

    /** Current members. */
    private val members = LinkedHashSet<String>()

    /** message id → message, in send order. */
    private val messages = LinkedHashMap<String, GroupMessage>()

    // ───────────── API ─────────────

    /** Step 1 */
    fun addMember(user: String) {
        TODO("Step 1")
    }

    /** Step 1 — they stop receiving new messages, but keep what they already received. */
    fun removeMember(user: String) {
        TODO("Step 1")
    }

    /**
     * Step 1 — fan-out: the message goes to every CURRENT member except the sender.
     * Sender not a member → `IllegalStateException`. A message id that already exists → ignore (retry).
     */
    fun send(sender: String, messageId: String, text: String) {
        TODO("Step 1")
    }

    /** Step 1 — texts of all messages [user] was a recipient of, in send order (even if they left later). */
    fun inbox(user: String): List<String> {
        TODO("Step 1")
    }

    /** Step 2 — record a read receipt. Only counts if [user] was a recipient; unknown message → ignore. */
    fun markRead(user: String, messageId: String) {
        TODO("Step 2")
    }

    /** Step 2 — who has read it (empty set for an unknown message). */
    fun readBy(messageId: String): Set<String> {
        TODO("Step 2")
    }

    /**
     * Step 3 — blue ticks for the group: every recipient who is STILL a member has read it.
     * (People who left don't block it; people who joined later were never recipients.)
     * No recipients left → `true`. Unknown message → `false`.
     */
    fun isReadByAll(messageId: String): Boolean {
        TODO("Step 3")
    }
}
