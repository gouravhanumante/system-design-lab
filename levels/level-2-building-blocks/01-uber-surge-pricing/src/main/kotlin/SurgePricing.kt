class SurgePricing(private val windowSeconds: Long = 60) {

    fun recordRequest(zone: String, now: Long) {
        TODO("Remember this request for the zone")
    }

    fun setAvailableDrivers(zone: String, count: Int) {
        TODO("Remember how many drivers are free in the zone")
    }

    fun multiplier(zone: String, now: Long): Double {
        TODO("Demand in the last windowSeconds / supply, mapped to a multiplier")
    }
}
