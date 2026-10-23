class Autocomplete(private val maxSuggestions: Int = 5) {

    // ───────────── State ─────────────

    /** One trie node = one prefix. */
    private class Node {
        val children = HashMap<Char, Node>()
        /** The best queries starting with this prefix: (query, total count), best first, at most maxSuggestions. Precomputed so `suggest` is fast. */
        val top = ArrayList<Pair<String, Long>>()
    }

    /** The empty prefix. */
    private val root = Node()

    /** normalized query → its total count so far. */
    private val totals = HashMap<String, Long>()

    /** Nodes touched (root included) by the most recent suggest() call. */
    var lastNodesVisited: Int = 0
        private set

    // ───────────── Helpers ─────────────

    /** Step 1 — lowercase, trim, and turn runs of spaces into one space: `"  iPhone   15 "` → `"iphone 15"`. */
    private fun normalize(query: String): String {
        TODO("Step 1")
    }

    /**
     * Step 2 — put (query, total) into [node]'s top list: remove the query's old entry if any, add the new one,
     * sort by count (high first) then query A→Z, and keep only the first [maxSuggestions].
     */
    private fun updateTopList(node: Node, query: String, total: Long) {
        TODO("Step 2")
    }

    // ───────────── API ─────────────

    /**
     * Step 2 — someone searched [query] [count] times.
     * - count < 1 → `IllegalArgumentException`. Normalize; empty after normalizing → ignore.
     * - Add to `totals`, then walk the trie character by character (creating nodes as needed) and call
     *   [updateTopList] on the root AND every node on the path.
     */
    fun addQuery(query: String, count: Long = 1) {
        TODO("Step 2")
    }

    /**
     * Step 3 — suggestions for [prefix] (lowercased): follow one child per character from the root.
     * Missing child → empty list. Otherwise return that node's top queries.
     * `lastNodesVisited` = nodes you touched, root included (so `"ip"` that exists = 3).
     */
    fun suggest(prefix: String): List<String> {
        TODO("Step 3")
    }
}
