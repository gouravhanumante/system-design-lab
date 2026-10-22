class FeedService(private val celebrityThreshold: Int = 3) {

    /** Number of times a post id was written into a follower's precomputed feed. */
    var pushWrites: Int = 0
        private set

    fun follow(follower: String, followee: String, now: Long) {
        TODO()
    }

    fun post(author: String, postId: String, timestamp: Long) {
        TODO()
    }

    /** Newest first; ties by post id ascending. */
    fun feed(user: String, limit: Int): List<String> {
        TODO()
    }
}
