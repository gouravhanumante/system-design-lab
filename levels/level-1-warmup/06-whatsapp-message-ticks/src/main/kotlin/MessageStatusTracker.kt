enum class Status { SENT, DELIVERED, READ }

class MessageStatusTracker {

    fun send(messageId: String) {
        TODO()
    }

    fun markDelivered(messageId: String) {
        TODO()
    }

    fun markRead(messageId: String) {
        TODO()
    }

    fun status(messageId: String): Status? {
        TODO()
    }
}
