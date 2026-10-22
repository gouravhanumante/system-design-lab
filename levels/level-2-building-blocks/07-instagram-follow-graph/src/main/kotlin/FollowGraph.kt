class FollowGraph {

    fun follow(follower: String, followee: String): Boolean {
        TODO()
    }

    fun unfollow(follower: String, followee: String): Boolean {
        TODO()
    }

    fun followers(user: String): Set<String> {
        TODO()
    }

    fun following(user: String): Set<String> {
        TODO()
    }

    /** Users that both a and b follow. */
    fun commonFollowing(a: String, b: String): Set<String> {
        TODO()
    }

    /** Friends-of-friends, best first. */
    fun suggestions(user: String, limit: Int): List<String> {
        TODO()
    }
}
