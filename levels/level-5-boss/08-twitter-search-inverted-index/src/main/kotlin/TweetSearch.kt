/** A query split into its parts, e.g. `kotlin "jetpack compose" -java` → terms [kotlin], phrases [[jetpack, compose]], excluded [java]. */
private data class ParsedQuery(val terms: List<String>, val phrases: List<List<String>>, val excluded: List<String>)

class TweetSearch {

    // ───────────── State ─────────────

    /** The inverted index: token → (tweet id → positions of that token in the tweet, 0-based). Positions make phrase search possible. */
    private val postings = HashMap<String, HashMap<Long, MutableList<Int>>>()

    /** tweet id → its tokens (so `delete` knows which postings to clean up). */
    private val tokensOf = HashMap<Long, List<String>>()

    // ───────────── Helpers ─────────────

    /**
     * Step 1 — lowercase the text, split on every character that is NOT a-z, 0-9, `#` or `@`, drop empty pieces.
     * `"Hello, #Kotlin @Dev!"` → `["hello", "#kotlin", "@dev"]`.
     */
    private fun tokenize(text: String): List<String> {
        TODO("Step 1")
    }

    /**
     * Step 2 — split a query:
     * - text inside double quotes → a phrase (tokenized; skip empty phrases);
     * - outside quotes, split on whitespace: a word starting with `-` → excluded tokens (tokenize the rest), otherwise → terms.
     */
    private fun parseQuery(query: String): ParsedQuery {
        TODO("Step 2")
    }

    /** Step 5 — does tweet [id] contain [phrase]'s tokens right after each other (positions p, p+1, p+2, …) at least once? */
    private fun containsPhrase(id: Long, phrase: List<String>): Boolean {
        TODO("Step 5")
    }

    // ───────────── API ─────────────

    /** Step 1 — tokenize and add every token's position to `postings`. Indexing an existing id REPLACES it (delete first). */
    fun index(tweetId: Long, text: String) {
        TODO("Step 1")
    }

    /** Step 1 — remove the tweet from every posting list (drop tokens with no tweets left). Unknown: do nothing. */
    fun delete(tweetId: Long) {
        TODO("Step 1")
    }

    /**
     * Steps 3–5 — matching tweet ids, newest (largest id) first, at most [limit].
     * - A tweet matches if it contains EVERY term and every phrase's tokens (AND), every phrase appears in order
     *   (step 5), and it contains NO excluded token (step 4).
     * - No terms and no phrases → empty list.
     * Tip: start from the smallest posting list and intersect — don't scan every tweet.
     */
    fun search(query: String, limit: Int): List<Long> {
        TODO("Step 3")
    }
}
