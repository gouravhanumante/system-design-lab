class ViewTracker(private val windowSeconds: Long = 1800) {

    // ───────────── State ─────────────

    /**
     * (userId, videoId) → time of that user's last COUNTED view of that video.
     * Use a Pair as the key — NOT a glued string like "$userId:$videoId" (ids containing ':' would collide).
     */
    private val lastCounted = HashMap<Pair<String, String>, Long>()

    /** video id → counted views. */
    private val counts = HashMap<String, Long>()

    // ───────────── API ─────────────

    /**
     * Step 2 — returns `true` if this watch counts as a new view.
     * - It counts if this user never had a counted view of this video, or the last counted one was at least
     *   [windowSeconds] ago (`now - last >= windowSeconds`).
     * - When it counts: remember `now` as the last counted time and add 1 to the video's views.
     * - When it doesn't count: change nothing (an uncounted watch does NOT restart the window).
     */
    fun watch(userId: String, videoId: String, now: Long): Boolean {
        TODO("Step 2")
    }

    /** Step 1 — counted views of [videoId] (0 if none). */
    fun views(videoId: String): Long {
        TODO("Step 1")
    }
}
