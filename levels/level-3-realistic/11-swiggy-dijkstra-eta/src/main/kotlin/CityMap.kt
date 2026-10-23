data class Route(val minutes: Int, val path: List<String>)

class CityMap {

    // ───────────── State ─────────────

    /** A road in ONE direction. A two-way road = two Road objects. */
    private class Road(var minutes: Int) {
        var open = true
    }

    /** The graph: place → (neighbour → road from place to neighbour). Every place that appears in a road has an entry (maybe empty). */
    private val roads = HashMap<String, HashMap<String, Road>>()

    // ───────────── Helpers ─────────────

    /** Step 1 — add (or update the minutes of) the road from [a] to [b]; make sure [b] also has an entry in `roads`. */
    private fun putEdge(a: String, b: String, minutes: Int) {
        TODO("Step 1")
    }

    /** Step 2 — set `open` on the road a→b AND b→a, whichever exist. */
    private fun setOpen(a: String, b: String, open: Boolean) {
        TODO("Step 2")
    }

    /** Step 4 — compare two paths place by place (alphabetically); if one is a prefix of the other, the shorter is smaller. */
    private fun comparePaths(x: List<String>, y: List<String>): Int {
        TODO("Step 4")
    }

    // ───────────── API ─────────────

    /** Step 1 — minutes ≤ 0 or a == b → `IllegalArgumentException`. Two-way unless [oneWay]. Adding an existing road replaces its time. */
    fun addRoad(a: String, b: String, minutes: Int, oneWay: Boolean = false) {
        TODO("Step 1")
    }

    /** Step 2 — closed roads can't be used (both directions). */
    fun closeRoad(a: String, b: String) {
        TODO("Step 2")
    }

    /** Step 2 */
    fun reopenRoad(a: String, b: String) {
        TODO("Step 2")
    }

    /**
     * Steps 3–4 — the fastest route using only open roads.
     * - from == to → `Route(0, [from])`. Unknown place or unreachable → `null`.
     * - Dijkstra: a priority queue of (minutes so far, path so far), smallest minutes first (tie → [comparePaths]).
     *   Take the best; if its last place was already settled skip it; settle it; if it's [to] you're done;
     *   otherwise push (minutes + road, path + neighbour) for every open road to an unsettled neighbour.
     */
    fun fastestRoute(from: String, to: String): Route? {
        TODO("Step 3")
    }
}
