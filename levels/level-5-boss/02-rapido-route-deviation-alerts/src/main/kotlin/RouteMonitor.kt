class RouteMonitor(private val thresholdMeters: Int, private val consecutivePings: Int) {

    // ───────────── State ─────────────

    private class Trip(val route: List<Pair<Int, Int>>) {
        /** Off-route pings in a row (an on-route ping resets it to 0). */
        var offRouteStreak = 0
        /** Already alerted for the current streak? (Only one alert per streak.) */
        var alertedThisStreak = false
        /** Times of all alerts raised for this trip. */
        val alerts = ArrayList<Long>()
    }

    /** trip id → trip (removed by `endTrip`). */
    private val trips = HashMap<String, Trip>()

    // ───────────── Geometry helpers ─────────────

    /**
     * Step 2 — squared distance from point P(px, py) to the line SEGMENT a→b (not the infinite line, not just the corners).
     * Recipe: t = ((P - a) · (b - a)) / |b - a|², clamp t to [0, 1], closest point C = a + t·(b - a), return |P - C|².
     * If a == b the segment is a point: just return |P - a|². Use Double.
     */
    private fun distanceToSegmentSquared(px: Int, py: Int, a: Pair<Int, Int>, b: Pair<Int, Int>): Double {
        TODO("Step 2")
    }

    /** Step 3 — the smallest [distanceToSegmentSquared] over every consecutive pair of route points. */
    private fun distanceToRouteSquared(route: List<Pair<Int, Int>>, x: Int, y: Int): Double {
        TODO("Step 3")
    }

    // ───────────── API ─────────────

    /** Step 1 — start watching a trip. A route with fewer than 2 points → `IllegalArgumentException`. */
    fun startTrip(tripId: String, route: List<Pair<Int, Int>>) {
        TODO("Step 1")
    }

    /**
     * Step 4 — a GPS ping. Returns `true` exactly when this ping raises a NEW alert. Unknown/ended trip → `false`.
     * - Off route = distance to the route is MORE than [thresholdMeters] (exactly at the threshold is still on route).
     * - On route → reset the streak and `alertedThisStreak`, return `false`.
     * - Off route → streak + 1. When the streak reaches [consecutivePings] and you haven't alerted for this streak yet:
     *   record `now` in `alerts`, mark alerted, return `true`. Otherwise `false`.
     */
    fun ping(tripId: String, x: Int, y: Int, now: Long): Boolean {
        TODO("Step 4")
    }

    /** Step 1 — alert times for the trip (empty list if unknown). */
    fun alerts(tripId: String): List<Long> {
        TODO("Step 1")
    }

    /** Step 1 — forget the trip; later pings for it are ignored. */
    fun endTrip(tripId: String) {
        TODO("Step 1")
    }
}
