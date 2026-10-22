import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ViewTrackerTest {

    @Test
    fun firstWatchCounts() {
        val t = ViewTracker()
        assertTrue(t.watch("u", "v", 0))
        assertEquals(1L, t.views("v"))
    }

    @Test
    fun repeatWithinWindowDoesNotCount() {
        val t = ViewTracker(1800)
        assertTrue(t.watch("u", "v", 0))
        assertFalse(t.watch("u", "v", 100))
        assertFalse(t.watch("u", "v", 1799))
        assertEquals(1L, t.views("v"))
    }

    @Test
    fun countsAgainExactlyAtTheWindowEdgeAndWindowRestarts() {
        val t = ViewTracker(1800)
        t.watch("u", "v", 0)
        assertTrue(t.watch("u", "v", 1800))
        assertFalse(t.watch("u", "v", 2000))
        assertTrue(t.watch("u", "v", 3600))
        assertEquals(3L, t.views("v"))
    }

    @Test
    fun uncountedWatchesDoNotExtendTheWindow() {
        val t = ViewTracker(1000)
        t.watch("u", "v", 0)
        repeat(9) { assertFalse(t.watch("u", "v", 100L * (it + 1))) }   // refresh spam at 100..900
        assertTrue(t.watch("u", "v", 1000))                               // still counted at 0 + window
    }

    @Test
    fun usersAndVideosAreIndependent() {
        val t = ViewTracker(1800)
        assertTrue(t.watch("u1", "v", 0))
        assertTrue(t.watch("u2", "v", 1))
        assertTrue(t.watch("u1", "w", 2))
        assertFalse(t.watch("u1", "v", 3))
        assertEquals(2L, t.views("v"))
        assertEquals(1L, t.views("w"))
        assertEquals(0L, t.views("unknown"))
    }

    @Test
    fun separatorCharactersInIdsDoNotCollide() {
        val t = ViewTracker(1800)
        assertTrue(t.watch("a|b", "c", 0))
        assertTrue(t.watch("a", "b|c", 1))      // different pair, must count
        assertEquals(1L, t.views("c"))
        assertEquals(1L, t.views("b|c"))
    }
}
