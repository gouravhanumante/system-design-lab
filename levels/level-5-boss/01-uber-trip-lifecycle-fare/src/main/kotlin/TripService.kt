enum class RideState { REQUESTED, MATCHED, ARRIVED, STARTED, COMPLETED, CANCELLED }
enum class Party { RIDER, DRIVER }

class TripService {

    // ───────────── State ─────────────

    private class Ride(val surge: Double) {
        var state = RideState.REQUESTED
        var driver: String? = null
        /** When a driver was matched (the rider's cancellation fee depends on this). */
        var matchedAt = 0L
        /** When the trip started (the time part of the fare depends on this). */
        var startedAt = 0L
        /** Set when the trip completes. */
        var fare: Long? = null
    }

    private val rides = HashMap<String, Ride>()

    // ───────────── Helpers ─────────────

    /** Step 1 — the ride, or `IllegalArgumentException` if this id was never requested. */
    private fun rideOrThrow(rideId: String): Ride {
        TODO("Step 1")
    }

    /** Step 1 — throw `IllegalStateException` unless the ride's state is one of [allowed]. */
    private fun requireState(ride: Ride, vararg allowed: RideState) {
        TODO("Step 1")
    }

    /**
     * Step 3 — fare in rupees:
     * - minutes = trip duration in minutes, rounded UP, and at least 1 (a 0-second trip costs 1 minute).
     * - raw = (50 + 12 × km + 2 × minutes) × surge, where km = distanceMeters / 1000.0.
     * - fare = raw rounded half-up (`Math.round`), but never less than 80 (minimum applied AFTER surge).
     */
    private fun calculateFare(ride: Ride, distanceMeters: Int, now: Long): Long {
        TODO("Step 3")
    }

    // ───────────── Lifecycle ─────────────

    /** Step 1 — new ride in state REQUESTED. Id already used → `IllegalStateException`. */
    fun request(rideId: String, now: Long, surge: Double = 1.0) {
        TODO("Step 1")
    }

    /** Step 2 — REQUESTED → MATCHED. Remember the driver and `matchedAt = now`. */
    fun match(rideId: String, driverId: String, now: Long) {
        TODO("Step 2")
    }

    /** Step 2 — MATCHED → ARRIVED. */
    fun arrive(rideId: String, now: Long) {
        TODO("Step 2")
    }

    /** Step 2 — ARRIVED → STARTED. Remember `startedAt = now`. */
    fun start(rideId: String, now: Long) {
        TODO("Step 2")
    }

    /** Step 3 — STARTED → COMPLETED. Calculate, store and return the fare. */
    fun complete(rideId: String, distanceMeters: Int, now: Long): Long {
        TODO("Step 3")
    }

    /**
     * Step 4 — cancel; returns the fee charged to the rider.
     * - RIDER: allowed in REQUESTED, MATCHED, ARRIVED → state CANCELLED.
     *   Fee 30 if a driver was matched and more than 120 seconds passed since `matchedAt`; otherwise 0.
     * - DRIVER: allowed in MATCHED, ARRIVED → the ride goes back to REQUESTED with no driver (it needs a new match). Fee 0.
     * - Any other state → `IllegalStateException`.
     */
    fun cancel(rideId: String, by: Party, now: Long): Long {
        TODO("Step 4")
    }

    // ───────────── Queries ─────────────

    /** Step 1 — current state (unknown → `IllegalArgumentException`). */
    fun state(rideId: String): RideState {
        TODO("Step 1")
    }

    /** Step 1 — the fare once completed, `null` before (unknown → `IllegalArgumentException`). */
    fun fare(rideId: String): Long? {
        TODO("Step 1")
    }
}
