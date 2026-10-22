import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ViewCountServiceTest {

    private class FakeStore : ViewStore {
        val data = HashMap<String, Long>()
        val calls = ArrayList<Pair<String, Long>>()
        override fun add(videoId: String, delta: Long) {
            calls.add(videoId to delta)
            data.merge(videoId, delta, Long::plus)
        }
        override fun get(videoId: String): Long = data[videoId] ?: 0L
    }

    @Test
    fun readsAreExactEvenWhileViewsAreBuffered() {
        val store = FakeStore()
        val s = ViewCountService(store, 100)
        repeat(37) { s.recordView("v") }
        assertEquals(37L, s.views("v"))
        assertEquals(0L, store.get("v"))             // nothing written yet
        assertEquals(0, store.calls.size)
    }

    @Test
    fun flushesExactlyWhenTheThresholdIsReached() {
        val store = FakeStore()
        val s = ViewCountService(store, 100)
        repeat(99) { s.recordView("v") }
        assertEquals(0, store.calls.size)
        s.recordView("v")
        assertEquals(listOf("v" to 100L), store.calls)
        assertEquals(100L, s.views("v"))
    }

    @Test
    fun manyViewsCauseFarFewerWrites() {
        val store = FakeStore()
        val s = ViewCountService(store, 100)
        repeat(1050) { s.recordView("v") }
        assertEquals(10, store.calls.size)
        assertTrue(store.calls.all { it.second == 100L })
        assertEquals(1050L, s.views("v"))
        s.flushAll()
        assertEquals(11, store.calls.size)
        assertEquals("v" to 50L, store.calls.last())
        assertEquals(1050L, s.views("v"))
        assertEquals(1050L, store.get("v"))
    }

    @Test
    fun videosAreBufferedIndependently() {
        val store = FakeStore()
        val s = ViewCountService(store, 3)
        s.recordView("a"); s.recordView("b"); s.recordView("a"); s.recordView("b")
        assertEquals(0, store.calls.size)
        s.recordView("a")
        assertEquals(listOf("a" to 3L), store.calls)
        assertEquals(3L, s.views("a"))
        assertEquals(2L, s.views("b"))
    }

    @Test
    fun flushAllWritesOneCallPerNonEmptyBufferAndNeverZero() {
        val store = FakeStore()
        val s = ViewCountService(store, 100)
        s.flushAll()
        assertEquals(0, store.calls.size)
        s.recordView("a"); s.recordView("b"); s.recordView("b")
        s.flushAll()
        assertEquals(setOf("a" to 1L, "b" to 2L), store.calls.toSet())
        assertEquals(2, store.calls.size)
        s.flushAll()
        assertEquals(2, store.calls.size)            // buffers are empty now: no more calls
    }

    @Test
    fun existingStoreValuesAreIncludedInReads() {
        val store = FakeStore()
        store.data["v"] = 1_000L
        val s = ViewCountService(store, 10)
        repeat(4) { s.recordView("v") }
        assertEquals(1_004L, s.views("v"))
    }

    @Test
    fun countsRemainExactAcrossThresholdsAndFlushes() {
        val store = FakeStore()
        val s = ViewCountService(store, 7)
        var expected = 0L
        for (i in 1..500) {
            s.recordView("v"); expected++
            if (i % 61 == 0) s.flushAll()
            assertEquals(expected, s.views("v"))
        }
    }
}
