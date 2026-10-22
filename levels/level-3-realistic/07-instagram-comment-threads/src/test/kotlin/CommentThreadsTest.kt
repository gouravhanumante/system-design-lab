import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CommentThreadsTest {

    private fun sample(): CommentThreads {
        val t = CommentThreads()
        t.add("p", "c1", "ann", "nice")
        t.add("p", "c2", "bob", "thanks", parentId = "c1")
        t.add("p", "c3", "ann", "welcome", parentId = "c2")
        t.add("p", "c4", "cat", "first!")
        return t
    }

    @Test
    fun rendersDepthFirstWithIndentation() {
        assertEquals(
            listOf("ann: nice", "  bob: thanks", "    ann: welcome", "cat: first!"),
            sample().render("p"),
        )
    }

    @Test
    fun siblingsKeepInsertionOrder() {
        val t = CommentThreads()
        t.add("p", "a", "u1", "top")
        t.add("p", "b", "u2", "second reply", parentId = "a")
        t.add("p", "c", "u3", "third", parentId = "a")
        t.add("p", "d", "u4", "grandchild of b", parentId = "b")
        assertEquals(listOf("u1: top", "  u2: second reply", "    u4: grandchild of b", "  u3: third"), t.render("p"))
    }

    @Test
    fun deletedCommentWithLiveRepliesBecomesAPlaceholder() {
        val t = sample()
        t.delete("c2")
        assertEquals(listOf("ann: nice", "  [deleted]", "    ann: welcome", "cat: first!"), t.render("p"))
        assertEquals(3, t.count("p"))
    }

    @Test
    fun deletedLeafDisappears() {
        val t = sample()
        t.delete("c3")
        assertEquals(listOf("ann: nice", "  bob: thanks", "cat: first!"), t.render("p"))
    }

    @Test
    fun deletedBranchWithNothingAliveIsPruned() {
        val t = sample()
        t.delete("c2"); t.delete("c3")
        assertEquals(listOf("ann: nice", "cat: first!"), t.render("p"))
        assertEquals(2, t.count("p"))
    }

    @Test
    fun deletingEverythingRendersNothing() {
        val t = sample()
        listOf("c1", "c2", "c3", "c4").forEach { t.delete(it) }
        assertEquals(emptyList(), t.render("p"))
        assertEquals(0, t.count("p"))
    }

    @Test
    fun repliesCanBeAddedUnderDeletedComments() {
        val t = sample()
        t.delete("c1")
        t.add("p", "c5", "dan", "late reply", parentId = "c1")
        assertEquals(listOf("[deleted]", "  bob: thanks", "    ann: welcome", "  dan: late reply", "cat: first!"), t.render("p"))
    }

    @Test
    fun postsAreSeparate() {
        val t = sample()
        t.add("other", "x1", "zed", "hi")
        assertEquals(listOf("zed: hi"), t.render("other"))
        assertEquals(1, t.count("other"))
        assertEquals(emptyList(), t.render("none"))
        assertEquals(0, t.count("none"))
    }

    @Test
    fun invalidOperationsAreRejected() {
        val t = sample()
        assertFailsWith<IllegalArgumentException> { t.add("p", "c1", "x", "dup") }
        assertFailsWith<IllegalArgumentException> { t.add("p", "c9", "x", "orphan", parentId = "nope") }
        assertFailsWith<IllegalArgumentException> { t.add("other", "c9", "x", "wrong post", parentId = "c1") }
        assertFailsWith<IllegalArgumentException> { t.delete("ghost") }
        t.delete("c4"); t.delete("c4")                // deleting twice is fine
    }

    @Test
    fun deepChainsDoNotBreak() {
        val t = CommentThreads()
        t.add("p", "n0", "u", "root")
        for (i in 1..500) t.add("p", "n$i", "u", "reply $i", parentId = "n${i - 1}")
        assertEquals(501, t.render("p").size)
        assertEquals(1000, t.render("p").last().takeWhile { it == ' ' }.length)
    }
}
