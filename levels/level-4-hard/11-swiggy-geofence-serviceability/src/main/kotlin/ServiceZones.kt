class ServiceZones {

    /** Number of full point-in-polygon tests run by the most recent zoneFor() call. */
    var lastFullChecks: Int = 0
        private set

    fun addZone(zoneId: String, polygon: List<Pair<Int, Int>>, priority: Int = 0) {
        TODO()
    }

    fun zoneFor(x: Int, y: Int): String? {
        TODO()
    }
}
