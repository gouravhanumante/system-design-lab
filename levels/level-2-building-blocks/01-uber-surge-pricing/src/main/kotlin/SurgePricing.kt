class SurgePricing(private val windowSeconds: Long = 60) {

    // ───────────── State ─────────────

    /** zone → times of its ride requests, oldest first (a queue: add at the back, drop old ones from the front). */
    private val requests = HashMap<String, ArrayDeque<Long>>()

    /** zone → number of free drivers (latest value set). */
    private val drivers = HashMap<String, Int>()

    // ───────────── API ─────────────

    /** Step 1 — remember that a rider asked for a ride in [zone] at time [now]. */
    fun recordRequest(zone: String, now: Long) {
        TODO("Step 1")
    }

    /** Step 1 — remember how many drivers are free in [zone] (replaces the old number). */
    fun setAvailableDrivers(zone: String, count: Int) {
        TODO("Step 1")
    }

    /**
     * Step 2 — requests in [zone] during the last [windowSeconds] seconds.
     * A request at time t still counts while `now - t < windowSeconds` (at exactly `windowSeconds` old it no longer counts).
     * Remove expired ones from the front of the queue so it doesn't grow forever.
     */
    private fun demand(zone: String, now: Long): Int {
        TODO("Step 2")
    }

    /**
     * Step 3 — ratio = demand / supply, where supply = free drivers but at least 1 (a zone with 0 drivers counts as 1).
     *
     * | ratio            | multiplier |
     * |------------------|-----------:|
     * | ≤ 1.0            | 1.0 |
     * | ≤ 1.5            | 1.2 |
     * | ≤ 2.0            | 1.5 |
     * | ≤ 3.0            | 2.0 |
     * | more than 3.0    | 3.0 |
     *
     * Tip: divide as Double (`demand.toDouble() / supply`), not Int.
     */
    fun multiplier(zone: String, now: Long): Double {
        TODO("Step 3")
    }
}
