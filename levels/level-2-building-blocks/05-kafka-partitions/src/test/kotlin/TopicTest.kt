import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TopicTest {

    @Test
    fun keyedMessagesGoToHashPartition() {
        val t = Topic(3)
        val r = t.produce("user-1", "hello")
        assertEquals(Math.floorMod("user-1".hashCode(), 3), r.partition)
        assertEquals(0L, r.offset)
        assertEquals("user-1", r.key)
    }

    @Test
    fun sameKeyAlwaysSamePartitionAndOrderIsKept() {
        val t = Topic(4)
        val records = (1..10).map { t.produce("user-42", "event-$it") }
        assertEquals(1, records.map { it.partition }.toSet().size)
        assertEquals((0L..9L).toList(), records.map { it.offset })
        val p = records[0].partition
        assertEquals((1..10).map { "event-$it" }, t.read(p, 0, 100).map { it.value })
    }

    @Test
    fun negativeHashCodesStillMapToAValidPartition() {
        val key = generateSequence(0) { it + 1 }.map { "k$it" }.first { it.hashCode() < 0 }
        val t = Topic(5)
        val r = t.produce(key, "v")
        assertTrue(r.partition in 0 until 5)
        assertEquals(Math.floorMod(key.hashCode(), 5), r.partition)
    }

    @Test
    fun unkeyedMessagesAreRoundRobin() {
        val t = Topic(3)
        val parts = (1..7).map { t.produce(null, "m$it").partition }
        assertEquals(listOf(0, 1, 2, 0, 1, 2, 0), parts)
    }

    @Test
    fun keyedMessagesDoNotAdvanceTheRoundRobinCounter() {
        val t = Topic(3)
        assertEquals(0, t.produce(null, "a").partition)
        t.produce("k", "x"); t.produce("k2", "y")
        assertEquals(1, t.produce(null, "b").partition)
    }

    @Test
    fun partitionsHaveIndependentOffsets() {
        val t = Topic(2)
        assertEquals(0L, t.produce(null, "a").offset)   // partition 0
        assertEquals(0L, t.produce(null, "b").offset)   // partition 1
        assertEquals(1L, t.produce(null, "c").offset)   // partition 0 again
        assertEquals(2L, t.endOffset(0))
        assertEquals(1L, t.endOffset(1))
    }

    @Test
    fun readBehavesLikeALogPerPartition() {
        val t = Topic(2)
        listOf("a", "b", "c", "d").forEach { t.produce(null, it) }     // p0: a, c   p1: b, d
        assertEquals(listOf("a", "c"), t.read(0, 0, 10).map { it.value })
        assertEquals(listOf("d"), t.read(1, 1, 10).map { it.value })
        assertEquals(emptyList(), t.read(1, 2, 10))
        assertFailsWith<IllegalArgumentException> { t.read(0, -1, 5) }
    }

    @Test
    fun invalidPartitionsAreRejected() {
        val t = Topic(2)
        assertFailsWith<IllegalArgumentException> { t.read(2, 0, 1) }
        assertFailsWith<IllegalArgumentException> { t.read(-1, 0, 1) }
        assertFailsWith<IllegalArgumentException> { t.endOffset(5) }
    }
}
