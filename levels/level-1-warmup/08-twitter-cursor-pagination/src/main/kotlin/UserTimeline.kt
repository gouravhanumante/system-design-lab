data class Tweet(val id: Long, val userId: String, val text: String)
data class Page(val tweets: List<Tweet>, val nextCursor: Long?)

class UserTimeline {

    fun post(userId: String, tweetId: Long, text: String) {
        TODO()
    }

    fun delete(tweetId: Long) {
        TODO()
    }

    /** Newest first. cursor = null for the first page, otherwise only ids < cursor. */
    fun page(userId: String, cursor: Long?, limit: Int): Page {
        TODO()
    }
}
