class ViewTracker(private val windowSeconds: Long = 1800) {

    /** Returns true if this watch counted as a new view. */
    fun watch(userId: String, videoId: String, now: Long): Boolean {
        TODO()
    }

    fun views(videoId: String): Long {
        TODO()
    }
}
