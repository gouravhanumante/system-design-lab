/** The "database". Provided — do not change. */
interface ViewStore {
    fun add(videoId: String, delta: Long)
    fun get(videoId: String): Long
}

class ViewCountService(private val store: ViewStore, private val flushThreshold: Int = 100) {

    fun recordView(videoId: String) {
        TODO()
    }

    /** Exact number of views: what the store holds plus what is still buffered. */
    fun views(videoId: String): Long {
        TODO()
    }

    fun flushAll() {
        TODO()
    }
}
