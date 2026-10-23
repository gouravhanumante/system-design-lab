data class Tweet(val id: Long, val userId: String, val text: String)
data class Page(val tweets: List<Tweet>, val nextCursor: Long?)

class UserTimeline {

    // ───────────── State ─────────────

    /** user → their tweets by id. TreeMap = sorted by id, and bigger id = newer. */
    private val timelines = HashMap<String, java.util.TreeMap<Long, Tweet>>()

    /** tweet id → tweet (to find the owner when deleting). */
    private val tweetsById = HashMap<Long, Tweet>()

    // ───────────── API ─────────────

    /** Step 1 — add a tweet. An id that already exists → `IllegalArgumentException`. */
    fun post(userId: String, tweetId: Long, text: String) {
        TODO("Step 1")
    }

    /** Step 1 — remove it from both maps (unknown id: do nothing). */
    fun delete(tweetId: Long) {
        TODO("Step 1")
    }

    /**
     * Step 2 — one page of [userId]'s timeline, newest (largest id) first.
     * - [cursor] null → start from the newest tweet. Otherwise only tweets with id < [cursor].
     * - Return at most [limit] tweets. If MORE tweets exist after this page, `nextCursor` = the id of the last
     *   tweet on this page; if this page reaches the end, `nextCursor = null`.
     * - `limit ≤ 0` or unknown user → empty page, `nextCursor = null`.
     * Why a cursor and not "page number"? Tweets added or deleted between requests can't shift the next page.
     * Tip: `timeline.headMap(cursor, false).descendingMap()` = tweets below the cursor, newest first.
     */
    fun page(userId: String, cursor: Long?, limit: Int): Page {
        TODO("Step 2")
    }
}
