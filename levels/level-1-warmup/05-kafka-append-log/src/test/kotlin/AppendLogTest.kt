import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AppendLogTest {

    @Test
    fun offsetsStartAtZeroAndIncrease() {
        val log = AppendLog()
        assertEquals(0L, log.endOffset())
        assertEquals(0L, log.append("a"))
        assertEquals(1L, log.append("b"))
        assertEquals(2L, log.append("c"))
        assertEquals(3L, log.endOffset())
    }

    @Test
    fun readReturnsRecordsFromTheGivenOffset() {
        val log = AppendLog()
        listOf("a", "b", "c").forEach { log.append(it) }
        assertEquals(listOf(Record(1, "b"), Record(2, "c")), log.read(1, 5))
        assertEquals(listOf(Record(0, "a"), Record(1, "b")), log.read(0, 2))
    }

    @Test
    fun readingAtOrPastTheEndIsEmpty() {
        val log = AppendLog()
        log.append("a")
        assertEquals(emptyList(), log.read(1, 10))
        assertEquals(emptyList(), log.read(100, 10))
        assertEquals(emptyList(), AppendLog().read(0, 10))
    }

    @Test
    fun maxRecordsOfZeroOrLessReturnsNothing() {
        val log = AppendLog()
        log.append("a")
        assertEquals(emptyList(), log.read(0, 0))
        assertEquals(emptyList(), log.read(0, -3))
    }

    @Test
    fun negativeOffsetIsRejected() {
        assertFailsWith<IllegalArgumentException> { AppendLog().read(-1, 5) }
    }

    @Test
    fun laterAppendsAreVisibleToLaterReads() {
        val log = AppendLog()
        log.append("a")
        assertEquals(emptyList(), log.read(1, 5))
        log.append("b")
        assertEquals(listOf(Record(1, "b")), log.read(1, 5))
    }

    @Test
    fun largeLog() {
        val log = AppendLog()
        repeat(50_000) { log.append("m$it") }
        assertEquals(Record(49_999, "m49999"), log.read(49_999, 10).single())
        assertEquals(1000, log.read(100, 1000).size)
    }
}
