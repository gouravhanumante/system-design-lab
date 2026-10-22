import kotlin.test.Test
import kotlin.test.assertEquals

class SurgePricingTest {

    private fun priced(drivers: Int, requests: Int): Double {
        val s = SurgePricing(60)
        s.setAvailableDrivers("Z", drivers)
        repeat(requests) { s.recordRequest("Z", 10) }
        return s.multiplier("Z", 20)
    }

    @Test
    fun noRequestsMeansNoSurge() {
        assertEquals(1.0, SurgePricing().multiplier("anywhere", 100), 1e-9)
    }

    @Test
    fun ratioBandsMapToMultipliers() {
        assertEquals(1.0, priced(3, 3), 1e-9)   // ratio 1.0
        assertEquals(1.2, priced(3, 4), 1e-9)   // 1.33
        assertEquals(1.2, priced(2, 3), 1e-9)   // exactly 1.5
        assertEquals(1.5, priced(3, 5), 1e-9)   // 1.67
        assertEquals(1.5, priced(2, 4), 1e-9)   // exactly 2.0
        assertEquals(2.0, priced(3, 7), 1e-9)   // 2.33
        assertEquals(2.0, priced(2, 6), 1e-9)   // exactly 3.0
        assertEquals(3.0, priced(3, 10), 1e-9)  // 3.33
    }

    @Test
    fun zoneWithoutDriversCountsAsOneDriver() {
        val s = SurgePricing(60)
        repeat(2) { s.recordRequest("Z", 5) }
        assertEquals(1.5, s.multiplier("Z", 6), 1e-9)   // 2 / 1
        s.recordRequest("Z", 6)
        assertEquals(2.0, s.multiplier("Z", 7), 1e-9)   // 3 / 1
        s.recordRequest("Z", 7)
        assertEquals(3.0, s.multiplier("Z", 8), 1e-9)   // 4 / 1
    }

    @Test
    fun oldRequestsExpireExactlyAtTheWindowEdge() {
        val s = SurgePricing(60)
        s.setAvailableDrivers("Z", 1)
        s.recordRequest("Z", 0)
        s.recordRequest("Z", 0)
        assertEquals(1.5, s.multiplier("Z", 59), 1e-9)  // both still count: 2 / 1
        assertEquals(1.0, s.multiplier("Z", 60), 1e-9)  // both expired
    }

    @Test
    fun onlyTheNewestRequestsCountAfterTimePasses() {
        val s = SurgePricing(60)
        s.setAvailableDrivers("Z", 1)
        s.recordRequest("Z", 0)
        s.recordRequest("Z", 30)
        s.recordRequest("Z", 70)
        assertEquals(1.5, s.multiplier("Z", 80), 1e-9)  // 30 and 70 count -> 2 / 1
        assertEquals(1.0, s.multiplier("Z", 95), 1e-9)  // only 70 counts -> 1 / 1
    }

    @Test
    fun zonesAreIndependent() {
        val s = SurgePricing(60)
        s.setAvailableDrivers("A", 1)
        s.setAvailableDrivers("B", 1)
        repeat(4) { s.recordRequest("A", 1) }
        assertEquals(3.0, s.multiplier("A", 2), 1e-9)
        assertEquals(1.0, s.multiplier("B", 2), 1e-9)
    }

    @Test
    fun driversCanChange() {
        val s = SurgePricing(60)
        s.setAvailableDrivers("Z", 1)
        repeat(3) { s.recordRequest("Z", 1) }
        assertEquals(2.0, s.multiplier("Z", 2), 1e-9)
        s.setAvailableDrivers("Z", 10)
        assertEquals(1.0, s.multiplier("Z", 2), 1e-9)
    }
}
