class RideOtp(private val generator: () -> String) {

    fun issue(rideId: String): String {
        TODO("Create (once) and return the code for this ride")
    }

    fun verify(rideId: String, code: String): Boolean {
        TODO("True only for the right code, and only the first time")
    }
}
