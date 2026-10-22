class HotZones(private val windowSeconds: Int = 300, private val bucketSeconds: Int = 10) {

    fun record(zone: String, now: Long) {
        TODO()
    }

    /** Up to k (zone, count) pairs, highest count first, ties by zone name. */
    fun topZones(k: Int, now: Long): List<Pair<String, Int>> {
        TODO()
    }
}
