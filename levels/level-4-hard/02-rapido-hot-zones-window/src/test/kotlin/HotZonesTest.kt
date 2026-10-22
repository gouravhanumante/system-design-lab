import kotlin.test.Test
import kotlin.test.assertEquals

class HotZonesTest {

    @Test
    fun countsAndSorts() {
        val h = HotZones(30, 10)
        h.record("B", 1); h.record("A", 2); h.record("A", 3); h.record("C", 4); h.record("A", 5); h.record("B", 6)
        assertEquals(listOf("A" to 3, "B" to 2), h.topZones(2, 10))
        assertEquals(listOf("A" to 3, "B" to 2, "C" to 1), h.topZones(10, 10))
    }

    @Test
    fun tiesAreOrderedByZoneName() {
        val h = HotZones(30, 10)
        h.record("zeta", 1); h.record("alpha", 2); h.record("mid", 3)
        assertEquals(listOf("alpha" to 1, "mid" to 1, "zeta" to 1), h.topZones(3, 5))
    }

    @Test
    fun bucketsExpireAsTimeMovesOn() {
        val h = HotZones(30, 10)                           // 3 buckets
        h.record("A", 0); h.record("A", 5); h.record("B", 12); h.record("A", 25)
        assertEquals(listOf("A" to 3, "B" to 1), h.topZones(5, 29))
        assertEquals(listOf("A" to 1, "B" to 1), h.topZones(5, 30))   // bucket 0 (A twice) is gone
        assertEquals(listOf("A" to 1), h.topZones(5, 40))             // bucket 1 (B) is gone
        assertEquals(emptyList(), h.topZones(5, 60))
    }

    @Test
    fun defaultSettingsKeepFiveMinutes() {
        val h = HotZones()
        h.record("X", 5)
        assertEquals(listOf("X" to 1), h.topZones(1, 299))
        assertEquals(emptyList(), h.topZones(1, 300))
    }

    @Test
    fun emptyAndSmallK() {
        val h = HotZones(30, 10)
        assertEquals(emptyList(), h.topZones(3, 0))
        h.record("A", 1); h.record("B", 1); h.record("B", 2)
        assertEquals(listOf("B" to 2), h.topZones(1, 3))
        assertEquals(emptyList(), h.topZones(0, 3))
    }

    @Test
    fun queryingBeforeRecordingKeepsCountsConsistent() {
        val h = HotZones(30, 10)
        h.record("A", 1)
        assertEquals(listOf("A" to 1), h.topZones(3, 2))
        h.record("A", 3)
        assertEquals(listOf("A" to 2), h.topZones(3, 4))
    }

    @Test
    fun handlesLongStreamsOfEvents() {
        val h = HotZones(60, 10)
        for (t in 0 until 100_000L) h.record("zone-${t % 7}", t)
        // last 60 s window at now = 99_999: buckets 9999 .. 9994 -> 6 buckets = 60 events spread over 7 zones
        val top = h.topZones(100, 99_999)
        assertEquals(60, top.sumOf { it.second })
        assertEquals(7, top.size)
    }
}
