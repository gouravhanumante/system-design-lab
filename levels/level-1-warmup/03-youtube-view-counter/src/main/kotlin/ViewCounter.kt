class ViewCounter {

    /** video id → number of views. */
    private val counts = HashMap<String, Long>()

    /** Step 1 — add one view to [videoId]. */
    fun view(videoId: String) {
        TODO("Step 1")
    }

    /** Step 1 — views of [videoId] (0 if it was never viewed). */
    fun views(videoId: String): Long {
        TODO("Step 1")
    }

    /** Step 2 — up to [n] (videoId, views) pairs, most viewed first; same views → smaller id first. `n = 0` → empty. */
    fun top(n: Int): List<Pair<String, Long>> {
        TODO("Step 2")
    }
}
