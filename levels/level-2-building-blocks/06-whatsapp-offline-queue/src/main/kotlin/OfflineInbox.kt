class OfflineInbox(private val ttlSeconds: Long = 30L * 24 * 3600) {

    fun send(to: String, messageId: String, text: String, now: Long) {
        TODO()
    }

    /** Marks the user online and returns the texts delivered by this call (oldest first). */
    fun connect(user: String, now: Long): List<String> {
        TODO()
    }

    fun disconnect(user: String) {
        TODO()
    }

    fun isOnline(user: String): Boolean {
        TODO()
    }

    fun delivered(user: String): List<String> {
        TODO()
    }
}
