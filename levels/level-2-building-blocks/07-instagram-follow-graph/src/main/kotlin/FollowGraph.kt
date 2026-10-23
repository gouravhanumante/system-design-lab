class FollowGraph {

    // ───────────── State: the same edges stored in both directions ─────────────

    /** user → people they follow ("following"). */
    private val followingOf = HashMap<String, MutableSet<String>>()

    /** user → people who follow them ("followers"). Kept in sync with [followingOf] so both lookups are fast. */
    private val followersOf = HashMap<String, MutableSet<String>>()

    // ───────────── API ─────────────

    /** Step 1 — `true` if the graph changed. Following yourself or following twice → `false`. */
    fun follow(follower: String, followee: String): Boolean {
        TODO("Step 1")
    }

    /** Step 1 — `true` if the edge existed and was removed (from BOTH maps). */
    fun unfollow(follower: String, followee: String): Boolean {
        TODO("Step 1")
    }

    /** Step 2 — copy of the set (empty if none). */
    fun followers(user: String): Set<String> {
        TODO("Step 2")
    }

    /** Step 2 — copy of the set (empty if none). */
    fun following(user: String): Set<String> {
        TODO("Step 2")
    }

    /** Step 3 — users that both [a] and [b] follow. */
    fun commonFollowing(a: String, b: String): Set<String> {
        TODO("Step 3")
    }

    /**
     * Step 4 — "suggested for you": people followed by the people [user] follows (friends of friends).
     * - Score of a candidate = how many of [user]'s followees follow them.
     * - Never suggest [user] or someone they already follow.
     * - Highest score first; tie → smaller id first. At most [limit].
     */
    fun suggestions(user: String, limit: Int): List<String> {
        TODO("Step 4")
    }
}
