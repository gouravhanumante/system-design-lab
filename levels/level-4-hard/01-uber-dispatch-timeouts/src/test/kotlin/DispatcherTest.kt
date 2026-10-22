import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DispatcherTest {

    private fun dispatcherWith(vararg drivers: Triple<String, Int, Int>): Dispatcher {
        val d = Dispatcher(10)
        drivers.forEach { d.setDriverAvailable(it.first, it.second, it.third) }
        return d
    }

    @Test
    fun offersTheNearestDriverAndAcceptMatches() {
        val d = dispatcherWith(Triple("far", 50, 0), Triple("near", 1, 0))
        assertEquals(Offer("r1", "near"), d.requestRide("r1", 0, 0, 0))
        assertEquals("OFFERED:near", d.rideStatus("r1"))
        assertTrue(d.accept("r1", "near", 5))
        assertEquals("MATCHED:near", d.rideStatus("r1"))
    }

    @Test
    fun noDriversFailsImmediately() {
        val d = Dispatcher(10)
        assertNull(d.requestRide("r1", 0, 0, 0))
        assertEquals("FAILED", d.rideStatus("r1"))
        assertEquals("UNKNOWN", d.rideStatus("nope"))
    }

    @Test
    fun declineMovesToNextNearestAndNeverReoffersToSameDriver() {
        val d = dispatcherWith(Triple("a", 1, 0), Triple("b", 2, 0), Triple("c", 3, 0))
        assertEquals(Offer("r", "a"), d.requestRide("r", 0, 0, 0))
        assertEquals(Offer("r", "b"), d.decline("r", "a", 1))
        assertEquals(Offer("r", "c"), d.decline("r", "b", 2))
        assertNull(d.decline("r", "c", 3))          // a, b, c all tried
        assertEquals("FAILED", d.rideStatus("r"))
    }

    @Test
    fun offerExpiresExactlyAtTimeout() {
        val d = dispatcherWith(Triple("a", 1, 0))
        d.requestRide("r", 0, 0, 100)
        assertEquals(emptyList(), d.tick(109))          // 109 < 110: still valid
        assertEquals("OFFERED:a", d.rideStatus("r"))
        assertFalse(d.accept("r", "a", 110))             // too late
        assertEquals(emptyList(), d.tick(110))          // expired, nobody left -> FAILED, no new offer
        assertEquals("FAILED", d.rideStatus("r"))
    }

    @Test
    fun timeoutPassesOfferToNextDriverWithFreshTimer() {
        val d = dispatcherWith(Triple("a", 1, 0), Triple("b", 5, 0))
        d.requestRide("r", 0, 0, 0)
        assertEquals(listOf(Offer("r", "b")), d.tick(10))
        assertTrue(d.accept("r", "b", 19))               // new offer made at 10, valid until 20
        assertEquals("MATCHED:b", d.rideStatus("r"))
    }

    @Test
    fun reservedDriversAreNotOfferedToOtherRides() {
        val d = dispatcherWith(Triple("a", 1, 0), Triple("b", 100, 0))
        assertEquals(Offer("r1", "a"), d.requestRide("r1", 0, 0, 0))
        assertEquals(Offer("r2", "b"), d.requestRide("r2", 0, 0, 1))   // a is reserved by r1
        assertNull(d.requestRide("r3", 0, 0, 2))                        // nobody left
    }

    @Test
    fun acceptRemovesDriverFromPoolUntilTheyAreAvailableAgain() {
        val d = dispatcherWith(Triple("a", 1, 0))
        d.requestRide("r1", 0, 0, 0)
        assertTrue(d.accept("r1", "a", 1))
        assertNull(d.requestRide("r2", 0, 0, 2))
        d.setDriverAvailable("a", 2, 0)
        assertEquals(Offer("r3", "a"), d.requestRide("r3", 0, 0, 3))
    }

    @Test
    fun wrongOrStaleCallsAreIgnored() {
        val d = dispatcherWith(Triple("a", 1, 0), Triple("b", 2, 0))
        d.requestRide("r", 0, 0, 0)
        assertFalse(d.accept("r", "b", 1))               // b holds no offer
        assertNull(d.decline("r", "b", 1))               // ignored, ride still offered to a
        assertEquals("OFFERED:a", d.rideStatus("r"))
        assertFalse(d.accept("ghost", "a", 1))
    }

    @Test
    fun declinedDriverBecomesAvailableForOtherRides() {
        val d = dispatcherWith(Triple("a", 1, 0))
        d.requestRide("r1", 0, 0, 0)
        assertNull(d.decline("r1", "a", 1))               // FAILED for r1, a is free again
        assertEquals(Offer("r2", "a"), d.requestRide("r2", 0, 0, 2))
    }

    @Test
    fun tickHandlesSeveralRidesInRideIdOrder() {
        val d = dispatcherWith(Triple("a", 1, 0), Triple("b", 2, 0), Triple("c", 3, 0), Triple("e", 4, 0))
        assertEquals(Offer("r1", "a"), d.requestRide("r1", 0, 0, 0))
        assertEquals(Offer("r2", "b"), d.requestRide("r2", 0, 0, 0))
        // at t=10 both expire. r1 is processed first: a is released, c is nearest untried free driver -> c
        val offers = d.tick(10)
        assertEquals(listOf(Offer("r1", "c"), Offer("r2", "a")), offers)
    }
}
