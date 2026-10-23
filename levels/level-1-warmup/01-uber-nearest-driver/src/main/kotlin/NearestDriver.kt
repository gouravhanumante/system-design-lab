data class Driver(val id: String, val x: Int, val y: Int)

class DriverFinder(private val drivers: List<Driver>) {

    /**
     * Step 1 — squared straight-line distance from [driver] to the point (x, y): `dx*dx + dy*dy`.
     * Squared is enough to compare "who is closer", and avoids `sqrt` and decimals.
     */
    private fun distanceSquared(driver: Driver, x: Int, y: Int): Int {
        TODO("Step 1")
    }

    /**
     * Step 2 — ids of the [k] drivers closest to (x, y), nearest first.
     * - Ties (same distance): smaller id first (alphabetical).
     * - [k] bigger than the number of drivers → return all of them. `k = 0` or no drivers → empty list.
     * - Do NOT change [drivers] (sort a copy — `sortedWith` returns a new list).
     */
    fun nearest(x: Int, y: Int, k: Int): List<String> {
        TODO("Step 2")
    }
}
