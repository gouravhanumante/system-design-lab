import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class BitrateSelectorTest {
    private val ladder = listOf(500, 1000, 3000, 6000)

    /** A download that measures exactly [kbps]. */
    private fun BitrateSelector.sample(kbps: Long) = onSegmentDownloaded(kbps * 1000 / 8, 1000)

    @Test
    fun startsLowWithoutData() {
        val b = BitrateSelector(ladder)
        assertNull(b.estimateKbps())
        assertEquals(500, b.choose(30.0))
    }

    @Test
    fun throughputAndHarmonicMean() {
        val b = BitrateSelector(ladder, window = 3)
        b.onSegmentDownloaded(1_250_000, 1000)                // 10,000 kbps
        assertEquals(10_000.0, b.estimateKbps()!!, 1e-6)
        b.sample(1_000)
        assertEquals(2 / (1 / 10_000.0 + 1 / 1_000.0), b.estimateKbps()!!, 1e-6)
    }

    @Test
    fun onlyTheLastWindowSamplesCount() {
        val b = BitrateSelector(ladder, window = 2)
        b.sample(100); b.sample(4_000); b.sample(4_000)
        assertEquals(4_000.0, b.estimateKbps()!!, 1e-6)
    }

    @Test
    fun climbsOneRungAtATimeWithAHealthyBuffer() {
        val b = BitrateSelector(ladder)
        b.sample(10_000)
        assertEquals(1000, b.choose(20.0))
        assertEquals(3000, b.choose(20.0))
        assertEquals(6000, b.choose(20.0))
        assertEquals(6000, b.choose(20.0))
    }

    @Test
    fun doesNotClimbWithAThinBuffer() {
        val b = BitrateSelector(ladder)
        b.sample(10_000)
        assertEquals(500, b.choose(14.9))
        assertEquals(1000, b.choose(15.0))
    }

    @Test
    fun dropsImmediatelyAndAllTheWayToTheTarget() {
        val b = BitrateSelector(ladder, window = 1)
        b.sample(10_000)
        repeat(3) { b.choose(20.0) }                       // at 6000
        b.sample(1_300)                                    // 0.8 * 1300 = 1040 -> target 1000
        assertEquals(1000, b.choose(20.0))
    }

    @Test
    fun panicModeWhenTheBufferIsNearlyEmpty() {
        val b = BitrateSelector(ladder)
        b.sample(50_000)
        repeat(3) { b.choose(20.0) }
        assertEquals(500, b.choose(4.9))
        assertEquals(1000, b.choose(15.0))                 // climbs again from the bottom
    }

    @Test
    fun safetyFactorKeepsHeadroom() {
        val b = BitrateSelector(ladder)
        b.sample(3_000)                                    // 0.8 * 3000 = 2400 -> 3000 does not fit
        repeat(5) { b.choose(20.0) }
        assertEquals(1000, b.choose(20.0))
        val tight = BitrateSelector(ladder, safetyFactor = 1.0)
        tight.sample(3_000)
        repeat(5) { tight.choose(20.0) }
        assertEquals(3000, tight.choose(20.0))
    }

    @Test
    fun veryLowBandwidthStillPlaysTheLowestRung() {
        val b = BitrateSelector(ladder)
        b.sample(100)
        assertEquals(500, b.choose(30.0))
    }

    @Test
    fun invalidLadder() {
        assertFailsWith<IllegalArgumentException> { BitrateSelector(emptyList()) }
        assertFailsWith<IllegalArgumentException> { BitrateSelector(listOf(1000, 500)) }
    }
}
