class ServiceZones {

    // ───────────── State ─────────────

    /** A delivery zone and its bounding box (the smallest rectangle around it — a cheap first check). */
    private class Zone(val id: String, val points: List<Pair<Int, Int>>, val priority: Int) {
        val minX = points.minOf { it.first }
        val maxX = points.maxOf { it.first }
        val minY = points.minOf { it.second }
        val maxY = points.maxOf { it.second }
    }

    /** zone id → zone. */
    private val zones = LinkedHashMap<String, Zone>()

    /** Number of full point-in-polygon tests run by the most recent zoneFor() call. */
    var lastFullChecks: Int = 0
        private set

    // ───────────── Geometry helpers ─────────────

    /**
     * Step 2 — is point P exactly on the segment a→b?
     * Yes if the cross product `(b - a) × (P - a)` is 0 (on the line) AND P is within the segment's x and y ranges. Use Long.
     */
    private fun isOnEdge(px: Int, py: Int, a: Pair<Int, Int>, b: Pair<Int, Int>): Boolean {
        TODO("Step 2")
    }

    /**
     * Step 2 — is (x, y) inside the polygon? Points ON the border count as inside (check every edge with [isOnEdge] first).
     * Then ray casting: walk the edges (i, j = previous point); an edge counts if it crosses the horizontal line at y
     * (`(yi > y) != (yj > y)`) and the crossing's x is to the RIGHT of the point. Odd number of crossings = inside.
     * (The `>` vs `<=` rule is what makes a ray through a corner count correctly.)
     */
    private fun isInside(zone: Zone, x: Int, y: Int): Boolean {
        TODO("Step 2")
    }

    // ───────────── API ─────────────

    /** Step 1 — fewer than 3 points or the id already exists → `IllegalArgumentException`. Store a copy of the points. */
    fun addZone(zoneId: String, polygon: List<Pair<Int, Int>>, priority: Int = 0) {
        TODO("Step 1")
    }

    /**
     * Step 3 — which zone serves (x, y)?
     * - Skip zones whose bounding box doesn't contain the point (cheap). For the rest run [isInside] and count each in `lastFullChecks`.
     * - Several zones match → highest priority; tie → smaller id. None → `null`.
     */
    fun zoneFor(x: Int, y: Int): String? {
        TODO("Step 3")
    }
}
