data class Point(val id: String, val x: Int, val y: Int)

/** Returns riderId -> captainId. Unmatched riders are not in the map. */
fun matchBatch(riders: List<Point>, captains: List<Point>): Map<String, String> {
    TODO("Repeatedly lock in the globally closest free (rider, captain) pair")
}
