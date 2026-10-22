import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LikeServiceTest {

    @Test
    fun likingTwiceCountsOnce() {
        val s = LikeService()
        assertTrue(s.like("ann", "p1"))
        assertFalse(s.like("ann", "p1"))
        assertEquals(1, s.likeCount("p1"))
    }

    @Test
    fun unlikeRemovesAndIsIdempotent() {
        val s = LikeService()
        s.like("ann", "p1")
        assertTrue(s.unlike("ann", "p1"))
        assertFalse(s.unlike("ann", "p1"))
        assertFalse(s.unlike("bob", "p1"))
        assertEquals(0, s.likeCount("p1"))
        assertFalse(s.hasLiked("ann", "p1"))
    }

    @Test
    fun likedByIsMostRecentFirst() {
        val s = LikeService()
        s.like("ann", "p1"); s.like("bob", "p1"); s.like("cat", "p1")
        assertEquals(listOf("cat", "bob", "ann"), s.likedBy("p1"))
    }

    @Test
    fun relikingMakesYouTheMostRecent() {
        val s = LikeService()
        s.like("ann", "p1"); s.like("bob", "p1")
        s.unlike("ann", "p1")
        s.like("ann", "p1")
        assertEquals(listOf("ann", "bob"), s.likedBy("p1"))
        assertEquals(2, s.likeCount("p1"))
    }

    @Test
    fun repeatedLikeKeepsTheOriginalPosition() {
        val s = LikeService()
        s.like("ann", "p1"); s.like("bob", "p1")
        s.like("ann", "p1")
        assertEquals(listOf("bob", "ann"), s.likedBy("p1"))
    }

    @Test
    fun postsAreIndependentAndUnknownPostsAreEmpty() {
        val s = LikeService()
        s.like("ann", "p1")
        assertEquals(0, s.likeCount("p2"))
        assertEquals(emptyList(), s.likedBy("p2"))
        assertFalse(s.hasLiked("ann", "p2"))
        assertTrue(s.hasLiked("ann", "p1"))
    }
}
