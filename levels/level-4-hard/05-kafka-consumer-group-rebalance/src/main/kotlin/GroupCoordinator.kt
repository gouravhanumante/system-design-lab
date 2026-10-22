class GroupCoordinator(private val partitions: Int, private val sessionTimeoutSeconds: Long = 10) {

    fun join(member: String, now: Long) {
        TODO()
    }

    fun leave(member: String) {
        TODO()
    }

    /** True if the member is known (and its heartbeat was recorded). */
    fun heartbeat(member: String, now: Long): Boolean {
        TODO()
    }

    /** Removes silent members, rebalances once if any were removed, returns their names sorted. */
    fun checkTimeouts(now: Long): List<String> {
        TODO()
    }

    fun assignment(): Map<String, List<Int>> {
        TODO()
    }

    fun generation(): Int {
        TODO()
    }
}
