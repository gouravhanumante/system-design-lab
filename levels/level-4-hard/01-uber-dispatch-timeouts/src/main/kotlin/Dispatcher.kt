data class Offer(val rideId: String, val driverId: String)

class Dispatcher(private val offerTimeoutSeconds: Long = 10) {

    fun setDriverAvailable(driverId: String, x: Int, y: Int) {
        TODO("Add the driver to the pool or update its position")
    }

    /** Offer the ride to the nearest available driver, or mark it FAILED and return null. */
    fun requestRide(rideId: String, x: Int, y: Int, now: Long): Offer? {
        TODO()
    }

    fun accept(rideId: String, driverId: String, now: Long): Boolean {
        TODO()
    }

    fun decline(rideId: String, driverId: String, now: Long): Offer? {
        TODO()
    }

    /** Expire timed-out offers (ascending ride id) and return the new offers made. */
    fun tick(now: Long): List<Offer> {
        TODO()
    }

    /** OFFERED:<driver>, MATCHED:<driver>, FAILED or UNKNOWN. */
    fun rideStatus(rideId: String): String {
        TODO()
    }
}
