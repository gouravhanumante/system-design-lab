import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GroupCoordinatorTest {

    @Test
    fun rangeAssignmentSplitsEvenly() {
        val g = GroupCoordinator(7)
        g.join("c", 0); g.join("a", 0); g.join("b", 0)
        assertEquals(mapOf("a" to listOf(0, 1, 2), "b" to listOf(3, 4), "c" to listOf(5, 6)), g.assignment())
    }

    @Test
    fun extraPartitionsGoToTheFirstMembers() {
        val g = GroupCoordinator(6)
        listOf("a", "b", "c", "d").forEach { g.join(it, 0) }
        assertEquals(mapOf("a" to listOf(0, 1), "b" to listOf(2, 3), "c" to listOf(4), "d" to listOf(5)), g.assignment())
    }

    @Test
    fun moreMembersThanPartitionsLeavesSomeIdle() {
        val g = GroupCoordinator(2)
        listOf("a", "b", "c").forEach { g.join(it, 0) }
        assertEquals(mapOf("a" to listOf(0), "b" to listOf(1), "c" to emptyList()), g.assignment())
    }

    @Test
    fun emptyGroupHasNoAssignment() {
        val g = GroupCoordinator(4)
        assertEquals(emptyMap(), g.assignment())
        assertEquals(0, g.generation())
    }

    @Test
    fun joiningAndLeavingRebalances() {
        val g = GroupCoordinator(4)
        g.join("a", 0)
        assertEquals(mapOf("a" to listOf(0, 1, 2, 3)), g.assignment())
        assertEquals(1, g.generation())
        g.join("b", 1)
        assertEquals(mapOf("a" to listOf(0, 1), "b" to listOf(2, 3)), g.assignment())
        assertEquals(2, g.generation())
        g.leave("a")
        assertEquals(mapOf("b" to listOf(0, 1, 2, 3)), g.assignment())
        assertEquals(3, g.generation())
    }

    @Test
    fun rejoiningOrLeavingUnknownMembersDoesNotRebalance() {
        val g = GroupCoordinator(4)
        g.join("a", 0)
        g.join("a", 5)
        g.leave("ghost")
        assertEquals(1, g.generation())
    }

    @Test
    fun heartbeatKeepsAMemberAlive() {
        val g = GroupCoordinator(4, 10)
        g.join("a", 0); g.join("b", 0)
        assertTrue(g.heartbeat("a", 8))
        assertEquals(listOf("b"), g.checkTimeouts(10))          // b silent for exactly 10 s
        assertEquals(mapOf("a" to listOf(0, 1, 2, 3)), g.assignment())
        assertEquals(3, g.generation())                          // join a, join b, timeout
    }

    @Test
    fun timeoutBoundaryAndRepeatedChecks() {
        val g = GroupCoordinator(4, 10)
        g.join("a", 0)
        assertEquals(emptyList(), g.checkTimeouts(9))
        assertEquals(1, g.generation())
        assertEquals(listOf("a"), g.checkTimeouts(10))
        assertEquals(emptyList(), g.checkTimeouts(11))
        assertEquals(2, g.generation())
        assertEquals(emptyMap(), g.assignment())
    }

    @Test
    fun severalTimeoutsAtOnceCauseOneRebalance() {
        val g = GroupCoordinator(6, 10)
        listOf("x", "y", "z").forEach { g.join(it, 0) }
        g.heartbeat("z", 9)
        assertEquals(listOf("x", "y"), g.checkTimeouts(10))
        assertEquals(4, g.generation())                          // 3 joins + 1 rebalance
        assertEquals(mapOf("z" to listOf(0, 1, 2, 3, 4, 5)), g.assignment())
    }

    @Test
    fun kickedMembersLearnItFromTheirNextHeartbeat() {
        val g = GroupCoordinator(2, 10)
        g.join("a", 0)
        g.checkTimeouts(100)
        assertFalse(g.heartbeat("a", 101))
        g.join("a", 102)
        assertTrue(g.heartbeat("a", 103))
    }

    @Test
    fun everyPartitionIsOwnedByExactlyOneMember() {
        for (p in 1..12) for (m in 0..15) {
            val g = GroupCoordinator(p)
            repeat(m) { g.join("member-%02d".format(it), 0) }
            val all = g.assignment().values.flatten().sorted()
            if (m == 0) assertEquals(emptyList(), all) else assertEquals((0 until p).toList(), all)
            val sizes = g.assignment().values.map { it.size }
            if (sizes.isNotEmpty()) assertTrue(sizes.max() - sizes.min() <= 1, "unbalanced for p=$p m=$m")
        }
    }
}
