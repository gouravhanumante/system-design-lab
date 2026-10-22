class ContinueWatching(private val finishedThreshold: Double = 0.95) {

    fun progress(profile: String, titleId: String, positionSec: Int, durationSec: Int, now: Long) {
        TODO()
    }

    fun resumePosition(profile: String, titleId: String): Int {
        TODO()
    }

    /** Unfinished titles, most recently watched first. */
    fun row(profile: String, limit: Int): List<String> {
        TODO()
    }

    fun remove(profile: String, titleId: String) {
        TODO()
    }
}
