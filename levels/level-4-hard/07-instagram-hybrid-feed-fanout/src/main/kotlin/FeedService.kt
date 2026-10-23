class FeedService(private val celebrityThreshold: Int = 3) {

    // ───────────── State ─────────────

    private class Post(val id: String, val author: String, val timestamp: Long)

    /** Number of times a post id was written into a follower's precomputed feed. */
    var pushWrites: Int = 0
        private set

    /** author → all their posts. */
    private val postsBy = HashMap<String, MutableList<Post>>()

    /** author → (follower → time they started following). */
    private val followersOf = HashMap<String, MutableMap<String, Long>>()

    /** user → authors they follow. */
    private val followingOf = HashMap<String, MutableSet<String>>()

    /** user → posts pushed into their feed (fan-out on write). */
    private val pushedFeed = HashMap<String, MutableList<Post>>()

    /** Authors with at least [celebrityThreshold] followers. Once a celebrity, always a celebrity. */
    private val celebrities = HashSet<String>()

    // ───────────── API ─────────────

    /**
     * Step 1 — [follower] starts following [followee] at time [now].
     * Self-follow or an existing follow → ignore. When the followee reaches [celebrityThreshold] followers, they become a celebrity.
     */
    fun follow(follower: String, followee: String, now: Long) {
        TODO("Step 1")
    }

    /**
     * Step 2 — store the post. If the author is NOT a celebrity: add it to the pushed feed of each CURRENT follower
     * (+1 `pushWrites` each). Celebrity posts are not pushed (their followers pull them when reading).
     */
    fun post(author: String, postId: String, timestamp: Long) {
        TODO("Step 2")
    }

    /**
     * Step 3 — the user's feed:
     * - everything in their pushed feed, plus
     * - posts of the CELEBRITIES they follow with `timestamp >= the time they followed` (pull).
     * - The same post id only once. Newest first; equal timestamps → post id ascending. At most [limit].
     */
    fun feed(user: String, limit: Int): List<String> {
        TODO("Step 3")
    }
}
