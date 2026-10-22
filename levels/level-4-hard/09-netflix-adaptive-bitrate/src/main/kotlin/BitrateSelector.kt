class BitrateSelector(
    private val ladderKbps: List<Int>,
    private val safetyFactor: Double = 0.8,
    private val window: Int = 3,
) {

    fun onSegmentDownloaded(bytes: Long, millis: Long) {
        TODO()
    }

    /** Harmonic mean of the last [window] samples, or null. */
    fun estimateKbps(): Double? {
        TODO()
    }

    /** Picks (and remembers) the quality for the next segment. */
    fun choose(bufferSeconds: Double): Int {
        TODO()
    }
}
