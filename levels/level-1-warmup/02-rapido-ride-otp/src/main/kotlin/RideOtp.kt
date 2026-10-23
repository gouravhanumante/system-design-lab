class RideOtp(private val generator: () -> String) {

    // ───────────── State ─────────────

    /** ride id → its OTP code. */
    private val codes = HashMap<String, String>()

    /** Ride ids whose code was already used successfully. */
    private val used = HashSet<String>()

    // ───────────── API ─────────────

    /**
     * Step 1 — the code for [rideId]. The first call creates it with `generator()`;
     * later calls return the SAME code (don't call the generator again). Tip: `getOrPut`.
     */
    fun issue(rideId: String): String {
        TODO("Step 1")
    }

    /**
     * Step 2 — `true` only if a code was issued for [rideId], [code] matches it, and it hasn't been used yet;
     * then mark it used. A wrong guess returns `false` but does NOT use up the real code.
     */
    fun verify(rideId: String, code: String): Boolean {
        TODO("Step 2")
    }
}
