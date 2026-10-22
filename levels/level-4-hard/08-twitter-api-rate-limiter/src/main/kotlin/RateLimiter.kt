class RateLimiter(
    private val perClientCapacity: Int,
    private val perClientRefillPerSecond: Double,
    private val globalCapacity: Int,
    private val globalRefillPerSecond: Double,
) {

    fun tryAcquire(clientId: String, nowMillis: Long): Boolean {
        TODO()
    }

    /** 0 if allowed now; otherwise whole milliseconds (rounded up) until both buckets have a token. */
    fun retryAfterMillis(clientId: String, nowMillis: Long): Long {
        TODO()
    }

    fun tokens(clientId: String, nowMillis: Long): Double {
        TODO()
    }
}
