enum class RideState { REQUESTED, MATCHED, ARRIVED, STARTED, COMPLETED, CANCELLED }
enum class Party { RIDER, DRIVER }

class TripService {

    fun request(rideId: String, now: Long, surge: Double = 1.0) {
        TODO()
    }

    fun match(rideId: String, driverId: String, now: Long) {
        TODO()
    }

    fun arrive(rideId: String, now: Long) {
        TODO()
    }

    fun start(rideId: String, now: Long) {
        TODO()
    }

    /** Returns the final fare. */
    fun complete(rideId: String, distanceMeters: Int, now: Long): Long {
        TODO()
    }

    /** Returns the cancellation fee charged to the rider. */
    fun cancel(rideId: String, by: Party, now: Long): Long {
        TODO()
    }

    fun state(rideId: String): RideState {
        TODO()
    }

    fun fare(rideId: String): Long? {
        TODO()
    }
}
