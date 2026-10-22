import kotlin.test.Test
import kotlin.test.assertEquals

class BatchMatchingTest {

    @Test
    fun globalGreedyBeatsFirstComeFirstServed() {
        val riders = listOf(Point("R1", 0, 0), Point("R2", 4, 0))
        val captains = listOf(Point("C1", 3, 0), Point("C2", -5, 0))
        assertEquals(mapOf("R2" to "C1", "R1" to "C2"), matchBatch(riders, captains))
    }

    @Test
    fun moreRidersThanCaptainsLeavesTheFarthestUnmatched() {
        val riders = listOf(Point("R1", 0, 0), Point("R2", 100, 0), Point("R3", 1, 0))
        val captains = listOf(Point("C1", 0, 1))
        // R1 is distance 1 from C1, R3 is distance 2 -> R1 wins
        assertEquals(mapOf("R1" to "C1"), matchBatch(riders, captains))
    }

    @Test
    fun moreCaptainsThanRiders() {
        val riders = listOf(Point("R1", 0, 0))
        val captains = listOf(Point("C1", 5, 0), Point("C2", 1, 0), Point("C3", 2, 0))
        assertEquals(mapOf("R1" to "C2"), matchBatch(riders, captains))
    }

    @Test
    fun tiesPreferSmallerRiderIdThenSmallerCaptainId() {
        val riders = listOf(Point("R2", 0, 0), Point("R1", 0, 0))
        val captains = listOf(Point("C2", 1, 0), Point("C1", 1, 0))
        assertEquals(mapOf("R1" to "C1", "R2" to "C2"), matchBatch(riders, captains))
    }

    @Test
    fun emptyInputs() {
        assertEquals(emptyMap(), matchBatch(emptyList(), listOf(Point("C", 0, 0))))
        assertEquals(emptyMap(), matchBatch(listOf(Point("R", 0, 0)), emptyList()))
        assertEquals(emptyMap(), matchBatch(emptyList(), emptyList()))
    }

    @Test
    fun everyoneGetsMatchedWhenCountsAreEqualAndNoOneIsReusedTwice() {
        val riders = (0 until 60).map { Point("R%02d".format(it), (it * 37) % 101, (it * 53) % 97) }
        val captains = (0 until 60).map { Point("C%02d".format(it), (it * 29) % 103, (it * 41) % 89) }
        val result = matchBatch(riders, captains)
        assertEquals(60, result.size)
        assertEquals(60, result.values.toSet().size)
    }

    @Test
    fun doesNotModifyInputs() {
        val riders = mutableListOf(Point("R1", 0, 0), Point("R2", 9, 9))
        val captains = mutableListOf(Point("C1", 1, 1), Point("C2", 8, 8))
        val r0 = riders.toList(); val c0 = captains.toList()
        matchBatch(riders, captains)
        assertEquals(r0, riders)
        assertEquals(c0, captains)
    }
}
