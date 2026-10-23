data class Point(val id: String, val x: Int, val y: Int)

/** One possible match and how far apart they are. */
private data class Candidate(val distanceSquared: Long, val riderId: String, val captainId: String)

/** Step 1 — squared distance between two points, as Long (`dx*dx + dy*dy`). */
private fun distanceSquared(a: Point, b: Point): Long {
    TODO("Step 1")
}

/**
 * Step 2 — every rider paired with every captain (riders × captains candidates),
 * sorted by distance, then rider id, then captain id (this makes ties predictable).
 */
private fun allPairs(riders: List<Point>, captains: List<Point>): List<Candidate> {
    TODO("Step 2")
}

/**
 * Step 3 — riderId → captainId, matched "globally greedy":
 * go through [allPairs] from closest to farthest; whenever both the rider and the captain are still free, match them.
 * - Each rider and each captain is used at most once. Unmatched riders are not in the map.
 * - Don't modify the input lists.
 */
fun matchBatch(riders: List<Point>, captains: List<Point>): Map<String, String> {
    TODO("Step 3")
}
