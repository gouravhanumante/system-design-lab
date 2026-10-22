import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RouteMonitorTest {
    private val straight = listOf(0 to 0, 1000 to 0)

    private fun monitor(route: List<Pair<Int, Int>> = straight, pings: Int = 3): RouteMonitor {
        val m = RouteMonitor(50, pings)
        m.startTrip("t", route)
        return m
    }

    @Test
    fun onRoutePingsNeverAlert() {
        val m = monitor()
        for (i in 0..20) assertFalse(m.ping("t", i * 50, 10, i.toLong()))
        assertEquals(emptyList(), m.alerts("t"))
    }

    @Test
    fun alertsOnTheNthConsecutiveOffRoutePingOnlyOnce() {
        val m = monitor()
        assertFalse(m.ping("t", 500, 60, 1))
        assertFalse(m.ping("t", 500, 80, 2))
        assertTrue(m.ping("t", 500, 90, 3))
        assertFalse(m.ping("t", 500, 100, 4))
        assertFalse(m.ping("t", 500, 120, 5))
        assertEquals(listOf(3L), m.alerts("t"))
    }

    @Test
    fun thresholdIsStrict() {
        val m = monitor(pings = 1)
        assertFalse(m.ping("t", 500, 50, 1))      // exactly 50 away: still on route
        assertTrue(m.ping("t", 500, 51, 2))
    }

    @Test
    fun distanceIsMeasuredToTheSegmentNotJustTheCorners() {
        val m = monitor(pings = 1)
        assertFalse(m.ping("t", 500, 40, 1))      // 500 m from either corner but only 40 m from the road
        assertFalse(m.ping("t", -30, 40, 2))      // beyond the start: nearest point is the corner, exactly 50 away
        assertTrue(m.ping("t", 1200, 0, 3))       // beyond the end: 200 m away
    }

    @Test
    fun anOnRoutePingResetsTheStreak() {
        val m = monitor()
        assertFalse(m.ping("t", 500, 100, 1))
        assertFalse(m.ping("t", 500, 100, 2))
        assertFalse(m.ping("t", 500, 0, 3))       // back on the road
        assertFalse(m.ping("t", 500, 100, 4))
        assertFalse(m.ping("t", 500, 100, 5))
        assertTrue(m.ping("t", 500, 100, 6))
    }

    @Test
    fun canAlertAgainAfterReturningToTheRoute() {
        val m = monitor(pings = 2)
        m.ping("t", 500, 100, 1); assertTrue(m.ping("t", 500, 100, 2))
        assertFalse(m.ping("t", 500, 0, 3))
        m.ping("t", 500, 100, 4); assertTrue(m.ping("t", 500, 100, 5))
        assertEquals(listOf(2L, 5L), m.alerts("t"))
    }

    @Test
    fun polylineWithACorner() {
        val m = monitor(route = listOf(0 to 0, 1000 to 0, 1000 to 1000), pings = 1)
        assertFalse(m.ping("t", 1000, 500, 1))    // on the second leg
        assertFalse(m.ping("t", 1030, 700, 2))    // 30 m from the second leg
        assertTrue(m.ping("t", 500, 500, 3))      // cuts the corner: 500 m from both legs
    }

    @Test
    fun tripsAreIndependentAndEndedTripsAreIgnored() {
        val m = RouteMonitor(50, 1)
        m.startTrip("a", straight)
        m.startTrip("b", listOf(0 to 0, 0 to 1000))
        assertTrue(m.ping("a", 0, 500, 1))        // off route for a (vertical)
        assertFalse(m.ping("b", 0, 500, 1))       // on route for b
        m.endTrip("a")
        assertFalse(m.ping("a", 0, 900, 2))
        assertFalse(m.ping("never-started", 0, 0, 3))
        assertEquals(emptyList(), m.alerts("never-started"))
    }

    @Test
    fun routeNeedsAtLeastTwoPoints() {
        val m = RouteMonitor(50, 1)
        assertFailsWith<IllegalArgumentException> { m.startTrip("x", listOf(0 to 0)) }
        assertFailsWith<IllegalArgumentException> { m.startTrip("x", emptyList()) }
    }
}
