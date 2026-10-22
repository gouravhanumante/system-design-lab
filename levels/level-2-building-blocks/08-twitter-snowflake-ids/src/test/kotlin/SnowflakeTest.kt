import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SnowflakeTest {

    @Test
    fun bitLayoutIsExact() {
        val g = SnowflakeGenerator(5, 0) { 1000L }
        val id = g.nextId()
        assertEquals((1000L shl 22) or (5L shl 12), id)
        assertEquals(SnowflakeParts(1000, 5, 0), decode(id))
    }

    @Test
    fun customEpochIsSubtracted() {
        val g = SnowflakeGenerator(1, epochMillis = 1_600_000_000_000L) { 1_600_000_000_123L }
        assertEquals(SnowflakeParts(123, 1, 0), decode(g.nextId()))
    }

    @Test
    fun sequenceIncrementsWithinAMillisecondAndResets() {
        var now = 50L
        val g = SnowflakeGenerator(7, 0) { now }
        assertEquals(0, decode(g.nextId()).sequence)
        assertEquals(1, decode(g.nextId()).sequence)
        assertEquals(2, decode(g.nextId()).sequence)
        now = 51
        assertEquals(SnowflakeParts(51, 7, 0), decode(g.nextId()))
    }

    @Test
    fun idsAreStrictlyIncreasingForOneWorker() {
        var calls = 0
        val g = SnowflakeGenerator(3, 0) { 10L + (calls++ / 7) }    // clock ticks every 7 reads
        var prev = -1L
        repeat(5_000) {
            val id = g.nextId()
            assertTrue(id > prev)
            prev = id
        }
    }

    @Test
    fun sequenceOverflowWaitsForTheNextMillisecond() {
        var calls = 0
        val g = SnowflakeGenerator(1, 0) { if (calls++ < 6_000) 1000L else 1001L }
        val ids = List(4_097) { g.nextId() }
        assertEquals(SnowflakeParts(1000, 1, 4095), decode(ids[4095]))
        assertEquals(SnowflakeParts(1001, 1, 0), decode(ids[4096]))
        assertEquals(4_097, ids.toSet().size)
    }

    @Test
    fun differentWorkersNeverCollide() {
        val a = SnowflakeGenerator(1, 0) { 777L }
        val b = SnowflakeGenerator(2, 0) { 777L }
        val all = List(100) { a.nextId() } + List(100) { b.nextId() }
        assertEquals(200, all.toSet().size)
    }

    @Test
    fun clockGoingBackwardsIsRefused() {
        var now = 100L
        val g = SnowflakeGenerator(1, 0) { now }
        g.nextId()
        now = 99
        assertFailsWith<IllegalStateException> { g.nextId() }
    }

    @Test
    fun invalidConfiguration() {
        assertFailsWith<IllegalArgumentException> { SnowflakeGenerator(1024, 0) { 1L } }
        assertFailsWith<IllegalArgumentException> { SnowflakeGenerator(-1, 0) { 1L } }
        assertFailsWith<IllegalStateException> { SnowflakeGenerator(1, 500) { 100L }.nextId() }
    }

    @Test
    fun largeTimestampsDecodeCorrectly() {
        val ts = (1L shl 41) - 1
        val g = SnowflakeGenerator(1023, 0) { ts }
        repeat(3) { g.nextId() }
        assertEquals(SnowflakeParts(ts, 1023, 3), decode(g.nextId()))
    }
}
