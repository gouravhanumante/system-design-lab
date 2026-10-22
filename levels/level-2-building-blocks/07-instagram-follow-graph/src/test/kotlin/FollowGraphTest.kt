import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FollowGraphTest {

    private fun graph(vararg edges: Pair<String, String>): FollowGraph {
        val g = FollowGraph()
        edges.forEach { g.follow(it.first, it.second) }
        return g
    }

    @Test
    fun followAndUnfollowReturnWhetherTheGraphChanged() {
        val g = FollowGraph()
        assertTrue(g.follow("a", "b"))
        assertFalse(g.follow("a", "b"))
        assertFalse(g.follow("a", "a"))
        assertTrue(g.unfollow("a", "b"))
        assertFalse(g.unfollow("a", "b"))
    }

    @Test
    fun followersAndFollowingAreMirrorImages() {
        val g = graph("a" to "b", "c" to "b", "a" to "c")
        assertEquals(setOf("b", "c"), g.following("a"))
        assertEquals(setOf("a", "c"), g.followers("b"))
        assertEquals(setOf("a"), g.followers("c"))
        assertEquals(emptySet(), g.followers("a"))
        assertEquals(emptySet(), g.following("nobody"))
    }

    @Test
    fun unfollowRemovesBothDirections() {
        val g = graph("a" to "b")
        g.unfollow("a", "b")
        assertEquals(emptySet(), g.following("a"))
        assertEquals(emptySet(), g.followers("b"))
    }

    @Test
    fun commonFollowingIsTheIntersection() {
        val g = graph("a" to "x", "a" to "y", "a" to "z", "b" to "y", "b" to "z", "b" to "w")
        assertEquals(setOf("y", "z"), g.commonFollowing("a", "b"))
        assertEquals(emptySet(), g.commonFollowing("a", "nobody"))
    }

    @Test
    fun suggestionsRankByNumberOfFolloweesWhoFollowTheCandidate() {
        val g = graph("me" to "a", "me" to "b", "a" to "x", "a" to "y", "b" to "x")
        assertEquals(listOf("x", "y"), g.suggestions("me", 10))
    }

    @Test
    fun suggestionsExcludeSelfAndAlreadyFollowed() {
        val g = graph("me" to "a", "me" to "b", "a" to "me", "a" to "b", "b" to "a", "a" to "x")
        assertEquals(listOf("x"), g.suggestions("me", 10))
    }

    @Test
    fun suggestionTiesAreBrokenByIdAndLimitApplies() {
        val g = graph("me" to "a", "a" to "zed", "a" to "amy", "a" to "mia")
        assertEquals(listOf("amy", "mia", "zed"), g.suggestions("me", 10))
        assertEquals(listOf("amy", "mia"), g.suggestions("me", 2))
        assertEquals(emptyList(), g.suggestions("me", 0))
    }

    @Test
    fun noFolloweesMeansNoSuggestions() {
        val g = graph("a" to "b")
        assertEquals(emptyList(), g.suggestions("loner", 5))
    }

    @Test
    fun largerGraphScoresCorrectly() {
        val g = FollowGraph()
        for (i in 0 until 20) g.follow("me", "f$i")
        for (i in 0 until 20) for (j in 0..(i % 5)) g.follow("f$i", "c$j")   // c0 followed by 20, c1 by 16, c2 by 12, c3 by 8, c4 by 4
        assertEquals(listOf("c0", "c1", "c2", "c3", "c4"), g.suggestions("me", 5))
    }
}
