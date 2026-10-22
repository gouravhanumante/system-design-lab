class WebhookDispatcher(
    private val send: (endpoint: String, eventId: String) -> Boolean,
    private val maxAttempts: Int = 5,
    private val baseDelaySeconds: Long = 10,
) {

    fun enqueue(eventId: String, endpoint: String, now: Long) {
        TODO()
    }

    /** Attempts due deliveries and returns this tick's log lines. */
    fun tick(now: Long): List<String> {
        TODO()
    }

    fun redrive(eventId: String, endpoint: String, now: Long): Boolean {
        TODO()
    }

    fun delivered(): List<String> {
        TODO()
    }

    fun deadLetters(): List<String> {
        TODO()
    }

    fun pending(): Int {
        TODO()
    }
}
