import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CircuitBreakerTest {
    private var now = 0L
    private var actionCalls = 0

    private fun breaker(threshold: Int = 3, open: Long = 1000, successes: Int = 2) =
        CircuitBreaker(threshold, open, successes) { now }

    private fun CircuitBreaker.ok(): String = call({ actionCalls++; "live" }, { "fallback" })
    private fun CircuitBreaker.fail(): String = call<String>({ actionCalls++; throw RuntimeException("down") }, { "fallback" })

    @Test
    fun closedPassesResultsThrough() {
        val b = breaker()
        assertEquals("live", b.ok())
        assertEquals(State.CLOSED, b.state())
    }

    @Test
    fun failuresReturnTheFallbackAndOpenAtTheThreshold() {
        val b = breaker()
        assertEquals("fallback", b.fail()); assertEquals(State.CLOSED, b.state())
        b.fail()
        b.fail()
        assertEquals(State.OPEN, b.state())
        assertEquals(3, actionCalls)
    }

    @Test
    fun successResetsTheConsecutiveCount() {
        val b = breaker()
        b.fail(); b.fail(); b.ok(); b.fail(); b.fail()
        assertEquals(State.CLOSED, b.state())
        b.fail()
        assertEquals(State.OPEN, b.state())
    }

    @Test
    fun openRejectsWithoutCallingTheAction() {
        val b = breaker()
        repeat(3) { b.fail() }
        actionCalls = 0
        now = 999
        assertEquals("fallback", b.ok())
        assertEquals("fallback", b.ok())
        assertEquals(0, actionCalls)
        assertEquals(2, b.rejected)
        assertEquals(State.OPEN, b.state())
    }

    @Test
    fun afterTheCooldownItProbesAndClosesAfterEnoughSuccesses() {
        val b = breaker()
        repeat(3) { b.fail() }
        now = 1000
        assertEquals(State.OPEN, b.state())                // state() does not look at the clock
        assertEquals("live", b.ok())
        assertEquals(State.HALF_OPEN, b.state())
        assertEquals("live", b.ok())
        assertEquals(State.CLOSED, b.state())
    }

    @Test
    fun aFailureWhileHalfOpenReopensWithAFreshTimer() {
        val b = breaker()
        repeat(3) { b.fail() }
        now = 1000
        b.ok()                                             // HALF_OPEN, 1 success
        now = 1200
        assertEquals("fallback", b.fail())
        assertEquals(State.OPEN, b.state())
        actionCalls = 0
        now = 2199
        b.ok()
        assertEquals(0, actionCalls)                       // still cooling down (opened again at 1200)
        now = 2200
        b.ok(); b.ok()                                     // needs 2 fresh successes
        assertEquals(State.CLOSED, b.state())
    }

    @Test
    fun closingResetsFailureCount() {
        val b = breaker()
        repeat(3) { b.fail() }
        now = 1000
        b.ok(); b.ok()
        b.fail(); b.fail()
        assertEquals(State.CLOSED, b.state())             // only 2 failures since closing
    }

    @Test
    fun thresholdOfOne() {
        val b = breaker(threshold = 1, open = 0, successes = 1)
        b.fail()
        assertEquals(State.OPEN, b.state())
        assertEquals("live", b.ok())                       // open for 0 ms: next call probes immediately
        assertEquals(State.CLOSED, b.state())
    }

    @Test
    fun invalidConfiguration() {
        assertFailsWith<IllegalArgumentException> { CircuitBreaker(0, 10, 1) { 0L } }
        assertFailsWith<IllegalArgumentException> { CircuitBreaker(1, -1, 1) { 0L } }
        assertFailsWith<IllegalArgumentException> { CircuitBreaker(1, 10, 0) { 0L } }
    }
}
