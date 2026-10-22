class Autocomplete(private val maxSuggestions: Int = 5) {

    /** Nodes touched (root included) by the most recent suggest() call. */
    var lastNodesVisited: Int = 0
        private set

    fun addQuery(query: String, count: Long = 1) {
        TODO()
    }

    fun suggest(prefix: String): List<String> {
        TODO()
    }
}
