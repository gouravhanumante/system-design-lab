import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TripServiceTest {

    private fun startedTrip(t: TripService, id: String = "r", surge: Double = 1.0, startAt: Long = 100) {
        t.request(id, 0, surge)
        t.match(id, "driver-1", 10)
        t.arrive(id, 50)
        t.start(id, startAt)
    }

    @Test
    fun happyPathAndFareWithSurge() {
        val t = TripService()
        startedTrip(t, surge = 1.5, startAt = 100)
        assertEquals(RideState.STARTED, t.state("r"))
        // 14 min 10 s -> 15 minutes. (50 + 12*5 + 2*15) * 1.5 = 210
        assertEquals(210L, t.complete("r", 5000, 100 + 14 * 60 + 10))
        assertEquals(RideState.COMPLETED, t.state("r"))
        assertEquals(210L, t.fare("r"))
    }

    @Test
    fun minimumFareApplies() {
        val t = TripService()
        startedTrip(t, startAt = 0)
        // 1000 m, 30 s -> 1 minute: 50 + 12 + 2 = 64 -> minimum 80
        assertEquals(80L, t.complete("r", 1000, 30))
    }

    @Test
    fun surgeIsAppliedBeforeMinimumAndRoundingIsHalfUp() {
        val t = TripService()
        startedTrip(t, id = "a", surge = 2.0, startAt = 0)
        assertEquals(128L, t.complete("a", 1000, 60))       // 64 * 2.0
        startedTrip(t, id = "b", surge = 1.0, startAt = 0)
        // 2500 m, 5 min exactly: 50 + 30 + 10 = 90
        assertEquals(90L, t.complete("b", 2500, 300))
        startedTrip(t, id = "c", surge = 1.0, startAt = 0)
        // 1250 m, 61 s -> 2 min: 50 + 15 + 4 = 69 -> minimum 80
        assertEquals(80L, t.complete("c", 1250, 61))
        startedTrip(t, id = "d", surge = 1.0, startAt = 0)
        // 10_000 m, 20 min: 50 + 120 + 40 = 210
        assertEquals(210L, t.complete("d", 10_000, 20 * 60))
        startedTrip(t, id = "e", surge = 1.5, startAt = 0)
        // 1250 m, 60 s: (50 + 15 + 2) * 1.5 = 100.5 -> rounds half up to 101
        assertEquals(101L, t.complete("e", 1250, 60))
    }

    @Test
    fun tripOfZeroSecondsStillCostsOneMinute() {
        val t = TripService()
        startedTrip(t, startAt = 500)
        assertEquals(88L, t.complete("r", 3000, 500))       // 50 + 36 + 2*1
    }

    @Test
    fun riderCancelFeeDependsOnHowLongAfterMatch() {
        val t = TripService()
        t.request("free", 0); t.match("free", "d", 10)
        assertEquals(0L, t.cancel("free", Party.RIDER, 130))     // exactly 120 s: still free
        assertEquals(RideState.CANCELLED, t.state("free"))

        t.request("fee", 0); t.match("fee", "d", 10)
        assertEquals(30L, t.cancel("fee", Party.RIDER, 131))     // 121 s: fee
        t.request("fee2", 0); t.match("fee2", "d", 10); t.arrive("fee2", 20)
        assertEquals(30L, t.cancel("fee2", Party.RIDER, 500))    // fee also after arrival

        t.request("early", 0)
        assertEquals(0L, t.cancel("early", Party.RIDER, 9999))   // never matched -> free
    }

    @Test
    fun driverCancelSendsRideBackToMatchingWithoutFee() {
        val t = TripService()
        t.request("r", 0); t.match("r", "d1", 10); t.arrive("r", 60)
        assertEquals(0L, t.cancel("r", Party.DRIVER, 1000))
        assertEquals(RideState.REQUESTED, t.state("r"))
        t.match("r", "d2", 1010)                                  // can be matched again
        assertEquals(30L, t.cancel("r", Party.RIDER, 1010 + 121)) // fee clock restarts at the new match
    }

    @Test
    fun illegalTransitionsThrow() {
        val t = TripService()
        t.request("r", 0)
        assertFailsWith<IllegalStateException> { t.arrive("r", 1) }
        assertFailsWith<IllegalStateException> { t.start("r", 1) }
        assertFailsWith<IllegalStateException> { t.complete("r", 100, 1) }
        assertFailsWith<IllegalStateException> { t.cancel("r", Party.DRIVER, 1) }
        t.match("r", "d", 1)
        assertFailsWith<IllegalStateException> { t.match("r", "d2", 2) }
        t.arrive("r", 2); t.start("r", 3)
        assertFailsWith<IllegalStateException> { t.cancel("r", Party.RIDER, 4) }
        assertFailsWith<IllegalStateException> { t.cancel("r", Party.DRIVER, 4) }
        t.complete("r", 5000, 600)
        assertFailsWith<IllegalStateException> { t.complete("r", 5000, 601) }
        assertFailsWith<IllegalStateException> { t.cancel("r", Party.RIDER, 700) }
    }

    @Test
    fun unknownAndDuplicateRides() {
        val t = TripService()
        assertFailsWith<IllegalArgumentException> { t.match("ghost", "d", 1) }
        assertFailsWith<IllegalArgumentException> { t.state("ghost") }
        t.request("r", 0)
        assertFailsWith<IllegalStateException> { t.request("r", 5) }
    }

    @Test
    fun fareIsNullBeforeCompletion() {
        val t = TripService()
        t.request("r", 0)
        assertNull(t.fare("r"))
        t.match("r", "d", 1); t.arrive("r", 2); t.start("r", 3)
        assertNull(t.fare("r"))
    }
}
