class OfflineInbox(private val ttlSeconds: Long = 30L * 24 * 3600) {

    // ───────────── State ─────────────

    /** A message waiting for an offline user. */
    private class QueuedMessage(val text: String, val sentAt: Long)

    /** Users currently connected. */
    private val online = HashSet<String>()

    /** user → messages waiting for them, oldest first. */
    private val waiting = HashMap<String, ArrayDeque<QueuedMessage>>()

    /** user → texts already delivered to them, in delivery order. */
    private val deliveredTexts = HashMap<String, MutableList<String>>()

    /** user → message ids already accepted for them (to ignore retries of the same message). */
    private val seenIds = HashMap<String, MutableSet<String>>()

    // ───────────── API ─────────────

    /**
     * Steps 1 + 3 — send a message to [to].
     * - Step 3: if this message id was already sent to this user → ignore it (the sender retried).
     *   (The same id sent to a DIFFERENT user is a different message.)
     * - Recipient online → delivered immediately. Offline → queued with `sentAt = now`.
     */
    fun send(to: String, messageId: String, text: String, now: Long) {
        TODO("Step 1")
    }

    /**
     * Steps 1 + 2 — the user comes online; return the texts delivered by THIS call, oldest first.
     * - Already online → return an empty list (nothing to redeliver).
     * - Deliver everything waiting, except (step 2) messages that are too old: a message expires when
     *   `now - sentAt >= ttlSeconds` — expired messages are dropped, not delivered.
     */
    fun connect(user: String, now: Long): List<String> {
        TODO("Step 1")
    }

    /** Step 1 — the user goes offline; new messages queue again. */
    fun disconnect(user: String) {
        TODO("Step 1")
    }

    /** Step 1 */
    fun isOnline(user: String): Boolean {
        TODO("Step 1")
    }

    /** Step 1 — everything delivered to [user] so far, in order (copy). */
    fun delivered(user: String): List<String> {
        TODO("Step 1")
    }
}
