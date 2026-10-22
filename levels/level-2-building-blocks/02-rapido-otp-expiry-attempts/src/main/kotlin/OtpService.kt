enum class VerifyResult { OK, WRONG, EXPIRED, LOCKED, UNKNOWN_RIDE, ALREADY_USED }

class OtpService(
    private val ttlSeconds: Long = 600,
    private val maxAttempts: Int = 3,
    private val generator: () -> String,
) {

    /** Always creates a fresh code (replacing any previous one) and returns it. */
    fun issue(rideId: String, now: Long): String {
        TODO()
    }

    fun verify(rideId: String, code: String, now: Long): VerifyResult {
        TODO()
    }
}
