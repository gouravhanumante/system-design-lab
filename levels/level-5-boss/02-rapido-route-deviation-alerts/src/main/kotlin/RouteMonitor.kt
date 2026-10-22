class RouteMonitor(private val thresholdMeters: Int, private val consecutivePings: Int) {

    fun startTrip(tripId: String, route: List<Pair<Int, Int>>) {
        TODO()
    }

    /** Returns true exactly when this ping raises a new alert. */
    fun ping(tripId: String, x: Int, y: Int, now: Long): Boolean {
        TODO()
    }

    fun alerts(tripId: String): List<Long> {
        TODO()
    }

    fun endTrip(tripId: String) {
        TODO()
    }
}
