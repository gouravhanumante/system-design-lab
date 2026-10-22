class GridIndex(private val cellSize: Int) {

    /** Number of drivers whose distance was computed in the most recent [withinRadius] call. */
    var lastScanCount: Int = 0
        private set

    /** Add the driver, or move it if it already exists. */
    fun update(driverId: String, x: Int, y: Int) {
        TODO("Put the driver into the right cell; remove it from its old cell if it moved")
    }

    fun remove(driverId: String) {
        TODO("Forget the driver (no-op if unknown)")
    }

    /** Driver ids with distance <= radius from (x, y), nearest first, ties by id. */
    fun withinRadius(x: Int, y: Int, radius: Int): List<String> {
        TODO("Only look at cells that can contain matches; set lastScanCount")
    }
}
