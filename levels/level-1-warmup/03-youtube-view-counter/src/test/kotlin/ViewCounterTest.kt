import kotlin.test.Test
import kotlin.test.assertEquals

class ViewCounterTest {

    @Test
    fun countsViewsPerVideo() {
        val c = ViewCounter()
        listOf("a", "b", "a", "c", "b", "a").forEach { c.view(it) }
        assertEquals(3L, c.views("a"))
        assertEquals(2L, c.views("b"))
        assertEquals(1L, c.views("c"))
        assertEquals(0L, c.views("nobody-watched-this"))
    }

    @Test
    fun topReturnsMostViewedFirst() {
        val c = ViewCounter()
        listOf("a", "b", "a", "c", "b", "a").forEach { c.view(it) }
        assertEquals(listOf("a" to 3L, "b" to 2L), c.top(2))
        assertEquals(listOf("a" to 3L, "b" to 2L, "c" to 1L), c.top(10))
    }

    @Test
    fun tiesAreBrokenByVideoId() {
        val c = ViewCounter()
        listOf("zed", "amy", "mid").forEach { c.view(it) }
        assertEquals(listOf("amy" to 1L, "mid" to 1L, "zed" to 1L), c.top(3))
    }

    @Test
    fun zeroAndEmptyCases() {
        val c = ViewCounter()
        assertEquals(emptyList(), c.top(5))
        c.view("a")
        assertEquals(emptyList(), c.top(0))
    }

    @Test
    fun handlesManyViews() {
        val c = ViewCounter()
        repeat(100_000) { c.view("v${it % 10}") }
        assertEquals(10_000L, c.views("v3"))
        assertEquals(10, c.top(100).size)
    }
}
