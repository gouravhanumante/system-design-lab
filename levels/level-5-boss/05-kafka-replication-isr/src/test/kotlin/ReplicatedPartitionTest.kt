import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ReplicatedPartitionTest {

    private fun cluster(minInSync: Int = 1) = ReplicatedPartition(listOf("a", "b", "c"), minInSync)

    @Test
    fun initialState() {
        val p = cluster()
        assertEquals("a", p.leader())
        assertEquals(listOf("a", "b", "c"), p.isr())
        assertEquals(0L, p.highWatermark())
        assertEquals(emptyList(), p.consume())
    }

    @Test
    fun messageIsCommittedOnlyWhenEveryIsrMemberHasIt() {
        val p = cluster()
        assertEquals(0L, p.produce("x"))
        assertEquals(0L, p.highWatermark())
        assertEquals(emptyList(), p.consume())
        p.replicate("b")
        assertEquals(0L, p.highWatermark())                    // c still lags
        p.replicate("c")
        assertEquals(1L, p.highWatermark())
        assertEquals(listOf("x"), p.consume())
        assertEquals(listOf("x"), p.logOf("c"))
    }

    @Test
    fun replicatingOnTheLeaderOrAnUnknownNodeDoesNothing() {
        val p = cluster()
        p.produce("x")
        p.replicate("a")
        p.replicate("nobody")
        assertEquals(emptyList(), p.logOf("b"))
    }

    @Test
    fun failedFollowerLeavesTheIsrSoTheWatermarkCanAdvance() {
        val p = cluster()
        p.produce("x")
        p.replicate("b")
        p.fail("c")
        assertEquals(listOf("a", "b"), p.isr())
        assertEquals("a", p.leader())
        assertEquals(1L, p.highWatermark())
        assertEquals(listOf("x"), p.consume())
    }

    @Test
    fun leaderFailureElectsFirstIsrMemberAndKeepsCommittedData() {
        val p = cluster()
        p.produce("x"); p.produce("y")
        p.replicate("b"); p.replicate("c")                      // committed: x, y
        p.produce("z")                                          // only on a: never committed
        p.fail("a")
        assertEquals("b", p.leader())
        assertEquals(listOf("b", "c"), p.isr())
        assertEquals(listOf("x", "y"), p.consume())
        assertEquals(2L, p.produce("w"))
        p.replicate("c")
        assertEquals(listOf("x", "y", "w"), p.consume())
    }

    @Test
    fun otherIsrMembersTruncateToTheNewLeadersLog() {
        val p = cluster()
        p.produce("x"); p.replicate("b")                        // b: [x]
        p.produce("y"); p.produce("z"); p.replicate("c")        // c: [x, y, z]
        p.fail("a")                                             // new leader b (shorter than c)
        assertEquals("b", p.leader())
        assertEquals(listOf("x"), p.logOf("c"))
        assertEquals(1L, p.highWatermark())
        assertEquals(listOf("x"), p.consume())
    }

    @Test
    fun produceIsRefusedWhenTooFewReplicasAreInSync() {
        val p = cluster(minInSync = 2)
        p.fail("b"); p.fail("c")
        assertEquals(listOf("a"), p.isr())
        assertFailsWith<IllegalStateException> { p.produce("x") }
        p.recover("b"); p.replicate("b")
        assertEquals(listOf("a", "b"), p.isr())
        assertEquals(0L, p.produce("x"))
    }

    @Test
    fun recoveredReplicaTruncatesAndMustCatchUpBeforeRejoining() {
        val p = cluster()
        p.produce("x"); p.produce("y")
        p.replicate("b"); p.replicate("c")                      // committed x, y
        p.produce("z")                                          // uncommitted, only on a
        p.fail("a")
        p.recover("a")
        assertEquals(listOf("x", "y"), p.logOf("a"))            // z was above the high watermark
        assertEquals(listOf("b", "c"), p.isr())                 // not back in sync yet
        p.produce("w")                                          // goes to leader b
        p.replicate("a")
        assertEquals(listOf("b", "c", "a"), p.isr())
        assertEquals(p.logOf("b"), p.logOf("a"))
        assertEquals(listOf("x", "y", "w"), p.logOf("a"))
    }

    @Test
    fun committedDataSurvivesRepeatedLeaderFailures() {
        val p = cluster()
        repeat(5) { p.produce("m$it") }
        p.replicate("b"); p.replicate("c")
        p.fail("a"); p.fail("b")
        assertEquals("c", p.leader())
        assertEquals(listOf("c"), p.isr())
        assertEquals((0 until 5).map { "m$it" }, p.consume())
    }

    @Test
    fun noLeaderMeansNoProduceAndNothingToConsume() {
        val p = cluster()
        p.produce("x"); p.replicate("b"); p.replicate("c")
        p.fail("c"); p.fail("b"); p.fail("a")
        assertNull(p.leader())
        assertEquals(emptyList(), p.isr())
        assertEquals(0L, p.highWatermark())
        assertEquals(emptyList(), p.consume())
        assertFailsWith<IllegalStateException> { p.produce("y") }
    }

    @Test
    fun uncleanRecoveryWhenThereIsNoLeader() {
        val p = cluster()
        p.produce("x"); p.replicate("b")                         // c never replicated
        p.fail("c"); p.fail("b"); p.fail("a")
        p.recover("c")                                           // c is empty: becomes leader anyway
        assertEquals("c", p.leader())
        assertEquals(listOf("c"), p.isr())
        assertEquals(emptyList(), p.logOf("c"))
        assertEquals(0L, p.produce("fresh"))
        assertEquals(listOf("fresh"), p.consume())
    }

    @Test
    fun failAndRecoverOnDeadOrUnknownReplicas() {
        val p = cluster()
        p.fail("b"); p.fail("b")                                  // second call is a no-op
        p.recover("a")                                            // already alive: no-op
        assertEquals(listOf("a", "c"), p.isr())
        assertFailsWith<IllegalArgumentException> { p.fail("zzz") }
    }
}
