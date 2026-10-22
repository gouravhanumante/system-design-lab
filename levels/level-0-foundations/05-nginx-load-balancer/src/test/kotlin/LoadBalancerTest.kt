import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoadBalancerTest {

    private fun lb(strategy: Strategy, vararg names: String): LoadBalancer {
        val l = LoadBalancer(strategy)
        names.forEach { l.addServer(it) }
        return l
    }

    @Test
    fun roundRobinTakesTurns() {
        val l = lb(Strategy.ROUND_ROBIN, "a", "b", "c")
        assertEquals(listOf("a", "b", "c", "a", "b"), List(5) { l.acquire() })
        assertEquals(2, l.activeConnections("a"))
    }

    @Test
    fun serverGoesDownOnlyAfterFallFailuresInARow() {
        val l = lb(Strategy.ROUND_ROBIN, "a", "b", "c")
        l.healthCheck("b", false); l.healthCheck("b", false)
        assertTrue(l.isHealthy("b"))
        l.healthCheck("b", true)                         // streak broken
        l.healthCheck("b", false); l.healthCheck("b", false)
        assertTrue(l.isHealthy("b"))
        l.healthCheck("b", false)
        assertFalse(l.isHealthy("b"))
    }

    @Test
    fun unhealthyServersAreSkippedAndComeBackAfterRisePasses() {
        val l = lb(Strategy.ROUND_ROBIN, "a", "b", "c")
        repeat(3) { l.healthCheck("b", false) }
        assertEquals(listOf("a", "c", "a", "c"), List(4) { l.acquire() })
        l.healthCheck("b", true)
        assertFalse(l.isHealthy("b"))
        l.healthCheck("b", true)
        assertTrue(l.isHealthy("b"))
        assertEquals(listOf("a", "b", "c"), List(3) { l.acquire() })
    }

    @Test
    fun noHealthyServers() {
        val l = lb(Strategy.ROUND_ROBIN, "a")
        repeat(3) { l.healthCheck("a", false) }
        assertNull(l.acquire())
        assertNull(lb(Strategy.LEAST_CONNECTIONS).acquire())
    }

    @Test
    fun leastConnectionsPicksTheLeastBusy() {
        val l = lb(Strategy.LEAST_CONNECTIONS, "a", "b", "c")
        assertEquals(listOf("a", "b", "c"), List(3) { l.acquire() })
        l.release("b")
        assertEquals("b", l.acquire())
        l.release("c"); l.release("a")
        assertEquals("a", l.acquire())                  // a and c both at 0: a was added first
        assertEquals("c", l.acquire())
    }

    @Test
    fun releaseNeverGoesBelowZero() {
        val l = lb(Strategy.LEAST_CONNECTIONS, "a")
        l.release("a"); l.release("ghost")
        assertEquals(0, l.activeConnections("a"))
    }

    @Test
    fun removedServersAreNeverChosen() {
        val l = lb(Strategy.ROUND_ROBIN, "a", "b", "c")
        l.acquire()
        l.removeServer("b"); l.removeServer("ghost")
        val picks = List(6) { l.acquire() }
        assertFalse("b" in picks)
        assertEquals(listOf("c", "a", "c", "a", "c", "a"), picks)
        assertFailsWith<IllegalArgumentException> { l.isHealthy("b") }
    }

    @Test
    fun invalidCalls() {
        val l = lb(Strategy.ROUND_ROBIN, "a")
        assertFailsWith<IllegalArgumentException> { l.addServer("a") }
        assertFailsWith<IllegalArgumentException> { l.healthCheck("ghost", true) }
        assertFailsWith<IllegalArgumentException> { l.activeConnections("ghost") }
    }
}
