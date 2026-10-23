data class Offer(val rideId: String, val driverId: String)

class Dispatcher(private val offerTimeoutSeconds: Long = 10) {

    // ───────────── State ─────────────

    /** One ride request and where it is in the matching process. */
    private class Ride(val x: Int, val y: Int) {
        /** `"OFFERED"`, `"MATCHED"` or `"FAILED"`. */
        var status = "FAILED"
        /** The driver it's offered to / matched with. */
        var driver: String? = null
        /** When the current offer was made (the timeout counts from here). */
        var offeredAt = 0L
        /** Drivers that already declined or timed out — never offer this ride to them again. */
        val tried = HashSet<String>()
    }

    /** Free drivers that can receive an offer: id → (x, y). A driver with a pending offer or a match is NOT in here. */
    private val pool = HashMap<String, Pair<Int, Int>>()

    /** Last known position of every driver ever made available (needed to put a driver back into the pool). */
    private val positions = HashMap<String, Pair<Int, Int>>()

    /** ride id → ride. */
    private val rides = HashMap<String, Ride>()

    // ───────────── API ─────────────

    /** Step 1 — add the driver to the free pool (or update its position) and to `positions`. */
    fun setDriverAvailable(driverId: String, x: Int, y: Int) {
        TODO("Step 1")
    }

    /**
     * Step 2 helper — offer [ride] to the nearest free driver it hasn't tried yet.
     * - Nearest = smallest squared distance; tie → smaller driver id.
     * - Found: remove the driver from `pool`, set status `"OFFERED"`, `driver`, `offeredAt = now`, return the [Offer].
     * - None left: status `"FAILED"`, `driver = null`, return `null`.
     */
    private fun offerToNextDriver(rideId: String, ride: Ride, now: Long): Offer? {
        TODO("Step 2")
    }

    /** Step 2 — create the ride and offer it to the nearest driver (or mark it FAILED and return null). */
    fun requestRide(rideId: String, x: Int, y: Int, now: Long): Offer? {
        TODO("Step 2")
    }

    /**
     * Step 3 — the driver accepts. Only works if the ride is `"OFFERED"` to exactly this driver and the offer
     * hasn't expired (`now < offeredAt + offerTimeoutSeconds`). Then status → `"MATCHED"`, return `true`.
     * Anything else (unknown ride, wrong driver, too late, already matched) → `false`, change nothing.
     * The driver stays out of the pool until `setDriverAvailable` is called again.
     */
    fun accept(rideId: String, driverId: String, now: Long): Boolean {
        TODO("Step 3")
    }

    /** Step 4 helper — the driver said no / timed out: add them to `ride.tried` and put them back in `pool` at their position. */
    private fun releaseDriver(ride: Ride, driverId: String) {
        TODO("Step 4")
    }

    /**
     * Step 4 — the driver declines. Only valid if the ride is `"OFFERED"` to this driver; otherwise return `null` and change nothing.
     * Release the driver, then offer to the next nearest driver and return that offer (or `null` if the ride FAILED).
     */
    fun decline(rideId: String, driverId: String, now: Long): Offer? {
        TODO("Step 4")
    }

    /**
     * Step 5 — for every ride, in ascending ride id order: if it's `"OFFERED"` and `now >= offeredAt + offerTimeoutSeconds`,
     * release that driver and offer to the next one. Return all the new offers made, in that order.
     */
    fun tick(now: Long): List<Offer> {
        TODO("Step 5")
    }

    /** Step 2 — `"OFFERED:<driver>"`, `"MATCHED:<driver>"`, `"FAILED"`, or `"UNKNOWN"` for a ride id never requested. */
    fun rideStatus(rideId: String): String {
        TODO("Step 2")
    }
}
