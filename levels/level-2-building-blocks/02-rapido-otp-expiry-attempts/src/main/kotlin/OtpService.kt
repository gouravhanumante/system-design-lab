enum class VerifyResult { OK, WRONG, EXPIRED, LOCKED, UNKNOWN_RIDE, ALREADY_USED }

class OtpService(
    private val ttlSeconds: Long = 600,
    private val maxAttempts: Int = 3,
    private val generator: () -> String,
) {

    // ───────────── State ─────────────

    /** Everything about the current code of one ride. */
    private class OtpRecord(val code: String, val expiresAt: Long) {
        /** Wrong guesses so far. */
        var wrongAttempts = 0
        /** Was it already verified successfully? */
        var used = false
    }

    /** ride id → its current record (a re-issue replaces it completely). */
    private val records = HashMap<String, OtpRecord>()

    // ───────────── API ─────────────

    /** Step 1 — new code from `generator()`, valid until `now + ttlSeconds`; replaces any old record (so lock/used are cleared). Return the code. */
    fun issue(rideId: String, now: Long): String {
        TODO("Step 1")
    }

    /**
     * Step 2 — check in THIS order and return the first that applies:
     * 1. no record for the ride → `UNKNOWN_RIDE`
     * 2. already used → `ALREADY_USED`
     * 3. `wrongAttempts >= maxAttempts` → `LOCKED` (even if the code is right, even if expired)
     * 4. `now >= expiresAt` → `EXPIRED` (does NOT count as a wrong attempt)
     * 5. code doesn't match → `wrongAttempts++`, `WRONG`
     * 6. otherwise → mark used, `OK`
     */
    fun verify(rideId: String, code: String, now: Long): VerifyResult {
        TODO("Step 2")
    }
}
