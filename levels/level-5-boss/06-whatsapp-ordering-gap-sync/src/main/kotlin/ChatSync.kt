data class Message(val chatId: String, val seq: Long, val messageId: String, val text: String)

class ChatServer {

    /** chat id → its messages; the message with seq n is at index n - 1. */
    private val chats = HashMap<String, MutableList<Message>>()

    /** (chat id, message id) → the message, to make `send` idempotent. */
    private val byMessageId = HashMap<Pair<String, String>, Message>()

    /** Number of times [fetch] has been called. */
    var fetchCount: Int = 0
        private set

    /**
     * Step 1 — the server orders messages: seq = 1, 2, 3, … per chat.
     * Same (chat, messageId) sent again (a retry) → return the ORIGINAL message, don't add a new one.
     */
    fun send(chatId: String, messageId: String, text: String): Message {
        TODO("Step 1")
    }

    /**
     * Step 1 — messages with seq > [afterSeq], in order, at most [limit]. Every call adds 1 to `fetchCount`.
     * Unknown chat, `limit ≤ 0`, or nothing after [afterSeq] → empty list.
     */
    fun fetch(chatId: String, afterSeq: Long, limit: Int): List<Message> {
        TODO("Step 1")
    }
}

class DeviceClient(private val server: ChatServer, private val chatId: String, private val batchSize: Int = 50) {

    /** seq → message, everything this device has received (pushes can arrive out of order or twice). */
    private val received = HashMap<Long, Message>()

    /** Step 2 — a push notification arrived. Keep it (a duplicate seq changes nothing). */
    fun receive(message: Message) {
        TODO("Step 2")
    }

    /** Step 2 — the largest n such that messages 1..n are ALL here (0 if message 1 is missing). */
    private fun contiguousUpTo(): Long {
        TODO("Step 2")
    }

    /** Step 2 — messages 1..n in order, n = [contiguousUpTo]. Never show a message after a hole. */
    fun displayed(): List<Message> {
        TODO("Step 2")
    }

    /** Step 2 — is there a hole? True if any received seq is greater than [contiguousUpTo] (so something before it is missing). */
    fun hasGap(): Boolean {
        TODO("Step 2")
    }

    /**
     * Step 3 — fill holes from the server: repeatedly `fetch(chatId, contiguousUpTo(), batchSize)`, `receive` every message,
     * and stop when a batch comes back with FEWER than [batchSize] messages (a full batch means there may be more).
     */
    fun sync() {
        TODO("Step 3")
    }
}
