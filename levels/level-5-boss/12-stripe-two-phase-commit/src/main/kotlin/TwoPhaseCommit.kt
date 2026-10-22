/** Provided. */
interface Participant {
    fun prepare(txId: String): Boolean
    fun commit(txId: String)
    fun abort(txId: String)
}

enum class Outcome { COMMITTED, ABORTED, IN_DOUBT }

class Coordinator(
    private val participants: Map<String, Participant>,
    private val log: MutableList<String>,
    private val commitRetries: Int = 3,
) {

    fun execute(txId: String, involved: List<String>): Outcome {
        TODO()
    }

    /** Finishes every transaction in the log that has no "done" line. */
    fun recover(): Map<String, Outcome> {
        TODO()
    }
}
