class RateLimiter(
    private val perClientCapacity: Int,
    private val perClientRefillPerSecond: Double,
    private val globalCapacity: Int,
    private val globalRefillPerSecond: Double,
) {

    // ───────────── A token bucket ─────────────

    /** Holds up to [capacity] tokens; gains [refillPerSecond] tokens per second (fractions allowed). Starts FULL. */
    private class TokenBucket(val capacity: Int, val refillPerSecond: Double, createdAtMillis: Long) {
        var tokens: Double = capacity.toDouble()
        /** Last time tokens were added. */
        var lastRefillMillis: Long = createdAtMillis

        /** Step 1 — add the tokens earned since `lastRefillMillis` (never above capacity), and move `lastRefillMillis` to [nowMillis]. Time going backwards → do nothing. */
        fun refill(nowMillis: Long) {
            TODO("Step 1")
        }

        /** Step 4 — 0 if there's at least 1 token; otherwise milliseconds until there will be 1, rounded UP to a whole number. */
        fun millisUntilOneToken(): Long {
            TODO("Step 4")
        }
    }

    // ───────────── State ─────────────

    /** One bucket shared by all clients — created on the first request ever. */
    private var globalBucket: TokenBucket? = null

    /** client id → its own bucket — created (full) on that client's first request. */
    private val clientBuckets = HashMap<String, TokenBucket>()

    /** Step 1 — get (or create) the client's bucket and the global bucket, refill both to [nowMillis], return (client, global). */
    private fun bucketsFor(clientId: String, nowMillis: Long): Pair<TokenBucket, TokenBucket> {
        TODO("Step 1")
    }

    // ───────────── API ─────────────

    /**
     * Steps 1 + 3 — allowed only if the client bucket AND the global bucket each have at least 1 token;
     * then take 1 from each and return `true`. If either is short → `false` and take NOTHING from either.
     */
    fun tryAcquire(clientId: String, nowMillis: Long): Boolean {
        TODO("Step 1")
    }

    /** Step 4 — 0 if a request would be allowed now; otherwise the LONGER wait of the two buckets. */
    fun retryAfterMillis(clientId: String, nowMillis: Long): Long {
        TODO("Step 4")
    }

    /** Step 2 — the client bucket's tokens after refilling to [nowMillis]. */
    fun tokens(clientId: String, nowMillis: Long): Double {
        TODO("Step 2")
    }
}
