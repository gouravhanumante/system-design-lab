class LikeService {

    /**
     * post id → users who currently like it, in the order they liked (oldest first).
     * A LinkedHashSet gives you both: no duplicates (one like per user) and remembered order.
     */
    private val likers = HashMap<String, LinkedHashSet<String>>()

    /** Step 1 — add the like. Returns `true` if it's new, `false` if this user already likes the post (nothing changes). */
    fun like(userId: String, postId: String): Boolean {
        TODO("Step 1")
    }

    /** Step 2 — remove the like. `true` if it existed, `false` otherwise (safe to call twice). */
    fun unlike(userId: String, postId: String): Boolean {
        TODO("Step 2")
    }

    /** Step 1 — number of current likes (0 for an unknown post). */
    fun likeCount(postId: String): Int {
        TODO("Step 1")
    }

    /** Step 1 */
    fun hasLiked(userId: String, postId: String): Boolean {
        TODO("Step 1")
    }

    /** Step 3 — current likers, most recent first. (Unlike + like again = most recent; a repeated like doesn't move you.) */
    fun likedBy(postId: String): List<String> {
        TODO("Step 3")
    }
}
