import kotlin.test.Test
import kotlin.test.assertEquals

class ContinueWatchingTest {

    @Test
    fun resumesWhereYouStopped() {
        val c = ContinueWatching()
        c.progress("me", "dark-s1e1", 1200, 3000, 1)
        assertEquals(1200, c.resumePosition("me", "dark-s1e1"))
        assertEquals(0, c.resumePosition("me", "unknown"))
    }

    @Test
    fun rowIsMostRecentFirst() {
        val c = ContinueWatching()
        c.progress("kid", "bluey", 300, 420, 10)
        c.progress("kid", "pingu", 60, 300, 20)
        c.progress("kid", "peppa", 10, 300, 15)
        assertEquals(listOf("pingu", "peppa", "bluey"), c.row("kid", 10))
        assertEquals(listOf("pingu", "peppa"), c.row("kid", 2))
    }

    @Test
    fun finishedTitlesLeaveTheRow() {
        val c = ContinueWatching()
        c.progress("kid", "bluey", 300, 420, 10)
        c.progress("kid", "pingu", 60, 300, 20)
        c.progress("kid", "bluey", 399, 420, 30)       // exactly 95 %
        assertEquals(listOf("pingu"), c.row("kid", 10))
        assertEquals(0, c.resumePosition("kid", "bluey"))
    }

    @Test
    fun rewatchingAFinishedTitleBringsItBack() {
        val c = ContinueWatching()
        c.progress("me", "film", 5000, 5000, 1)
        c.progress("me", "film", 100, 5000, 2)
        assertEquals(listOf("film"), c.row("me", 10))
        assertEquals(100, c.resumePosition("me", "film"))
    }

    @Test
    fun rewindingOverwritesAndUpdatesRecency() {
        val c = ContinueWatching()
        c.progress("me", "a", 900, 3000, 1)
        c.progress("me", "b", 100, 3000, 2)
        c.progress("me", "a", 300, 3000, 3)
        assertEquals(300, c.resumePosition("me", "a"))
        assertEquals(listOf("a", "b"), c.row("me", 10))
    }

    @Test
    fun tiesAreOrderedByTitleId() {
        val c = ContinueWatching()
        c.progress("me", "zz", 1, 100, 5); c.progress("me", "aa", 1, 100, 5)
        assertEquals(listOf("aa", "zz"), c.row("me", 10))
    }

    @Test
    fun profilesAreSeparateAndRemoveWorks() {
        val c = ContinueWatching()
        c.progress("mom", "crown", 50, 3000, 1)
        c.progress("dad", "f1", 70, 3000, 1)
        assertEquals(listOf("crown"), c.row("mom", 10))
        assertEquals(0, c.resumePosition("dad", "crown"))
        c.remove("mom", "crown")
        c.remove("mom", "never-watched")
        assertEquals(emptyList(), c.row("mom", 10))
        assertEquals(0, c.resumePosition("mom", "crown"))
    }

    @Test
    fun customThreshold() {
        val c = ContinueWatching(0.8)
        c.progress("me", "x", 79, 100, 1)
        assertEquals(listOf("x"), c.row("me", 5))
        c.progress("me", "x", 80, 100, 2)
        assertEquals(emptyList(), c.row("me", 5))
    }
}
