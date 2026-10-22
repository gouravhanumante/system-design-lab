import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class UserTimelineTest {

    private fun timeline(n: Int, user: String = "u"): UserTimeline {
        val t = UserTimeline()
        for (i in 1..n) t.post(user, i.toLong(), "tweet $i")
        return t
    }

    private fun ids(p: Page) = p.tweets.map { it.id }

    @Test
    fun walksAllPagesNewestFirst() {
        val t = timeline(5)
        val p1 = t.page("u", null, 2)
        assertEquals(listOf(5L, 4L), ids(p1)); assertEquals(4L, p1.nextCursor)
        val p2 = t.page("u", p1.nextCursor, 2)
        assertEquals(listOf(3L, 2L), ids(p2)); assertEquals(2L, p2.nextCursor)
        val p3 = t.page("u", p2.nextCursor, 2)
        assertEquals(listOf(1L), ids(p3)); assertNull(p3.nextCursor)
    }

    @Test
    fun exactFitHasNoNextCursor() {
        val t = timeline(4)
        val p1 = t.page("u", null, 2)
        val p2 = t.page("u", p1.nextCursor, 2)
        assertEquals(listOf(2L, 1L), ids(p2))
        assertNull(p2.nextCursor)
    }

    @Test
    fun deletionsBetweenPagesNeverCauseSkipsOrDuplicates() {
        val t = timeline(10)
        val p1 = t.page("u", null, 3)                 // 10, 9, 8
        t.delete(9); t.delete(8)                       // including the cursor tweet itself
        t.delete(7)
        val p2 = t.page("u", p1.nextCursor, 3)
        assertEquals(listOf(6L, 5L, 4L), ids(p2))
    }

    @Test
    fun newTweetsDoNotShiftLaterPages() {
        val t = timeline(6)
        val p1 = t.page("u", null, 3)                 // 6, 5, 4
        t.post("u", 100, "brand new")
        assertEquals(listOf(3L, 2L, 1L), ids(t.page("u", p1.nextCursor, 3)))
    }

    @Test
    fun usersAreSeparate() {
        val t = UserTimeline()
        t.post("a", 1, "a1"); t.post("b", 2, "b1"); t.post("a", 3, "a2")
        assertEquals(listOf(3L, 1L), ids(t.page("a", null, 10)))
        assertEquals(listOf(2L), ids(t.page("b", null, 10)))
        assertEquals(Page(emptyList(), null), t.page("nobody", null, 10))
    }

    @Test
    fun edgeCases() {
        val t = timeline(3)
        assertEquals(Page(emptyList(), null), t.page("u", null, 0))
        assertEquals(Page(emptyList(), null), t.page("u", 1, 5))
        assertFailsWith<IllegalArgumentException> { t.post("other", 2, "dup id") }
        t.delete(12345)
        assertEquals("tweet 3", t.page("u", null, 1).tweets.single().text)
    }
}
