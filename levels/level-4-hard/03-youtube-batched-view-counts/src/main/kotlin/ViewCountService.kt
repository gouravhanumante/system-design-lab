/** The "database". Provided — do not change. */
interface ViewStore {
    fun add(videoId: String, delta: Long)
    fun get(videoId: String): Long
}

class ViewCountService(private val store: ViewStore, private val flushThreshold: Int = 100) {

    /** video id → views counted in memory but NOT yet written to the store. */
    private val buffer = HashMap<String, Long>()

    /**
     * Step 1 — add 1 to the video's buffer. If the buffer reaches [flushThreshold],
     * call `store.add(videoId, bufferedCount)` once and remove the video from the buffer.
     */
    fun recordView(videoId: String) {
        TODO("Step 1")
    }

    /** Step 2 — exact views: `store.get(videoId)` + whatever is still buffered. */
    fun views(videoId: String): Long {
        TODO("Step 2")
    }

    /** Step 3 — write every non-empty buffer to the store (one `add` per video, never `add(…, 0)`), then clear the buffer. */
    fun flushAll() {
        TODO("Step 3")
    }
}
