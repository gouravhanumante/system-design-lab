class GridIndex(private val cellSize: Int) {

    // ───────────── State ─────────────

    /** Grid cell (cx, cy) → ids of the drivers inside it. The whole map is split into squares of size [cellSize]. */
    private val cells = HashMap<Pair<Int, Int>, MutableSet<String>>()

    /** driver id → where it is: its (x, y) and the cell it's stored in. */
    private val positions = HashMap<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>()

    /** Number of drivers whose distance was computed in the most recent [withinRadius] call. */
    var lastScanCount: Int = 0
        private set

    // ───────────── Helpers ─────────────

    /**
     * Step 1 — the cell that contains point (x, y): `(x / cellSize, y / cellSize)` rounded DOWN.
     * Careful: in Kotlin `-1 / 10 == 0`, but the point -1 is in cell -1. Use `Math.floorDiv(x, cellSize)`.
     */
    private fun cellOf(x: Int, y: Int): Pair<Int, Int> {
        TODO("Step 1")
    }

    // ───────────── API ─────────────

    /** Step 2 — add the driver, or move it if it already exists (remove it from its old cell first). */
    fun update(driverId: String, x: Int, y: Int) {
        TODO("Step 2")
    }

    /** Step 2 — forget the driver (unknown id: do nothing). Remove it from both `cells` and `positions`. */
    fun remove(driverId: String) {
        TODO("Step 2")
    }

    /**
     * Step 3 — driver ids with distance ≤ [radius] from (x, y) (boundary counts), nearest first, ties by id.
     * - Only look at cells from `cellOf(x - radius, y - radius)` to `cellOf(x + radius, y + radius)` — nothing else can match.
     * - Count every driver you compute a distance for → `lastScanCount`.
     * - Compare squared distances; use Long (`dx * dx` can overflow Int).
     */
    fun withinRadius(x: Int, y: Int, radius: Int): List<String> {
        TODO("Step 3")
    }
}
