data class Message(val chatId: String, val seq: Long, val messageId: String, val text: String)

class ChatServer {

    /** Number of times [fetch] has been called. */
    var fetchCount: Int = 0
        private set

    fun send(chatId: String, messageId: String, text: String): Message {
        TODO()
    }

    fun fetch(chatId: String, afterSeq: Long, limit: Int): List<Message> {
        TODO()
    }
}

class DeviceClient(private val server: ChatServer, private val chatId: String, private val batchSize: Int = 50) {

    fun receive(message: Message) {
        TODO()
    }

    /** Messages 1..n in order, where n is the largest number with no gaps before it. */
    fun displayed(): List<Message> {
        TODO()
    }

    fun hasGap(): Boolean {
        TODO()
    }

    fun sync() {
        TODO()
    }
}
