class ReplicatedPartition(private val replicas: List<String>, private val minInSync: Int = 1) {

    fun leader(): String? {
        TODO()
    }

    fun isr(): List<String> {
        TODO()
    }

    /** Appends to the leader's log. Throws IllegalStateException if the ISR is smaller than minInSync. */
    fun produce(value: String): Long {
        TODO()
    }

    fun replicate(follower: String) {
        TODO()
    }

    fun highWatermark(): Long {
        TODO()
    }

    /** Records below the high watermark, from the leader's log. */
    fun consume(): List<String> {
        TODO()
    }

    fun fail(replica: String) {
        TODO()
    }

    fun recover(replica: String) {
        TODO()
    }

    fun logOf(replica: String): List<String> {
        TODO()
    }
}
