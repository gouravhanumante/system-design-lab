data class Message(val id: String, val body: String, val receipt: String, val receiveCount: Int)

class WorkQueue(private val visibilityTimeoutSeconds: Long = 30, private val maxReceives: Int = 3) {

    fun send(body: String): String {
        TODO()
    }

    fun receive(now: Long): Message? {
        TODO()
    }

    fun ack(receipt: String, now: Long): Boolean {
        TODO()
    }

    fun extendVisibility(receipt: String, extraSeconds: Long, now: Long): Boolean {
        TODO()
    }

    fun deadLetters(): List<String> {
        TODO()
    }

    fun visibleCount(now: Long): Int {
        TODO()
    }

    fun inFlightCount(now: Long): Int {
        TODO()
    }
}
