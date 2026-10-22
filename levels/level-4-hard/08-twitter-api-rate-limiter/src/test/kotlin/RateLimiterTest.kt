import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RateLimiterTest {

    private fun perClientOnly(cap: Int, rate: Double) = RateLimiter(cap, rate, 1_000_000, 1_000_000.0)

    @Test
    fun allowsABurstUpToCapacity() {
        val r = perClientOnly(3, 1.0)
        assertTrue(r.tryAcquire("a", 0)); assertTrue(r.tryAcquire("a", 0)); assertTrue(r.tryAcquire("a", 0))
        assertFalse(r.tryAcquire("a", 0))
    }

    @Test
    fun refillsOverTime() {
        val r = perClientOnly(2, 1.0)
        r.tryAcquire("a", 0); r.tryAcquire("a", 0)
        assertFalse(r.tryAcquire("a", 999))
        assertTrue(r.tryAcquire("a", 1000))
        assertEquals(0.0, r.tokens("a", 1000), 1e-9)
        assertEquals(2.0, r.tokens("a", 60_000), 1e-9)                  // never above capacity
    }

    @Test
    fun fractionalRefillAccumulates() {
        val r = perClientOnly(1, 0.5)                                   // one token every 2 s
        assertTrue(r.tryAcquire("a", 0))
        assertEquals(0.25, r.tokens("a", 500), 1e-9)
        assertFalse(r.tryAcquire("a", 1500))
        assertTrue(r.tryAcquire("a", 2000))
    }

    @Test
    fun clientsHaveSeparateBuckets() {
        val r = perClientOnly(1, 1.0)
        assertTrue(r.tryAcquire("a", 0))
        assertFalse(r.tryAcquire("a", 0))
        assertTrue(r.tryAcquire("b", 0))
        assertEquals(1.0, r.tokens("new-client", 0), 1e-9)
    }

    @Test
    fun globalBucketLimitsEveryoneTogether() {
        val r = RateLimiter(10, 10.0, 3, 1.0)
        assertTrue(r.tryAcquire("a", 0)); assertTrue(r.tryAcquire("b", 0)); assertTrue(r.tryAcquire("c", 0))
        assertFalse(r.tryAcquire("d", 0))
        assertTrue(r.tryAcquire("d", 1000))
    }

    @Test
    fun aRefusedRequestSpendsNothing() {
        val r = RateLimiter(5, 1.0, 1, 1.0)
        assertTrue(r.tryAcquire("a", 0))                                // global now empty
        assertFalse(r.tryAcquire("b", 0))                               // refused by global...
        assertEquals(5.0, r.tokens("b", 0), 1e-9)                       // ...so b's own bucket is untouched
        assertFalse(r.tryAcquire("a", 500))
        assertEquals(4.5, r.tokens("a", 500), 1e-9)                     // a only paid once
    }

    @Test
    fun retryAfterRoundsUpAndUsesTheSlowerBucket() {
        val r = perClientOnly(2, 1.0)
        assertEquals(0L, r.retryAfterMillis("a", 0))
        r.tryAcquire("a", 0); r.tryAcquire("a", 0)
        assertEquals(1000L, r.retryAfterMillis("a", 0))
        assertEquals(500L, r.retryAfterMillis("a", 500))
        assertEquals(1L, r.retryAfterMillis("a", 999))
        val slow = RateLimiter(10, 10.0, 1, 0.25)                       // global refills one token per 4 s
        slow.tryAcquire("x", 0)
        assertEquals(4000L, slow.retryAfterMillis("x", 0))
        val third = RateLimiter(1, 3.0, 100, 100.0)                      // 1/3 s per token: 333.33 ms -> 334
        third.tryAcquire("x", 0)
        assertEquals(334L, third.retryAfterMillis("x", 0))
    }

    @Test
    fun steadyRateIsRespectedOverALongRun() {
        val r = perClientOnly(5, 2.0)
        var allowed = 0
        for (t in 0L until 60_000L step 50) if (r.tryAcquire("a", t)) allowed++   // 20 requests per second offered
        assertTrue(allowed in 120..125, "allowed $allowed")              // 5 burst + ~2/s for 60 s
    }
}
