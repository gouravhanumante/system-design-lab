import kotlin.test.Test
import kotlin.test.assertEquals

class FeedServiceTest {

    @Test
    fun ordinaryAuthorsArePushedToEachFollower() {
        val f = FeedService(3)
        f.follow("bob", "ann", 0); f.follow("cat", "ann", 0)
        f.post("ann", "p1", 10)
        assertEquals(2, f.pushWrites)
        assertEquals(listOf("p1"), f.feed("bob", 10))
        assertEquals(listOf("p1"), f.feed("cat", 10))
    }

    @Test
    fun celebrityPostsAreNotPushedButStillAppearInFeeds() {
        val f = FeedService(3)
        listOf("x", "y", "z").forEach { f.follow(it, "star", 0) }   // star reaches the threshold
        f.post("star", "s1", 10)
        assertEquals(0, f.pushWrites)
        assertEquals(listOf("s1"), f.feed("x", 10))
        assertEquals(listOf("s1"), f.feed("z", 10))
    }

    @Test
    fun feedMergesPushedAndPulledNewestFirst() {
        val f = FeedService(3)
        f.follow("me", "ann", 0)                                  // ann: ordinary
        listOf("me", "x", "y").forEach { f.follow(it, "star", 0) } // star: celebrity
        f.post("ann", "a1", 10)
        f.post("star", "s1", 20)
        f.post("ann", "a2", 30)
        f.post("star", "s2", 40)
        assertEquals(listOf("s2", "a2", "s1", "a1"), f.feed("me", 10))
        assertEquals(listOf("s2", "a2"), f.feed("me", 2))
    }

    @Test
    fun becomingACelebrityMidwayDoesNotDuplicateOrLosePosts() {
        val f = FeedService(3)
        f.follow("f1", "eve", 0); f.follow("f2", "eve", 0)
        f.post("eve", "e1", 10)                                   // pushed to f1, f2
        assertEquals(2, f.pushWrites)
        f.follow("f3", "eve", 15)                                 // eve is now a celebrity
        f.post("eve", "e2", 20)                                   // pulled, no pushes
        assertEquals(2, f.pushWrites)
        assertEquals(listOf("e2", "e1"), f.feed("f1", 10))        // e1 pushed AND found by pull: once only
        assertEquals(listOf("e2"), f.feed("f3", 10))              // f3 followed after e1
    }

    @Test
    fun youNeverSeePostsFromBeforeYouFollowed() {
        val f = FeedService(2)
        f.follow("a", "star", 0); f.follow("b", "star", 0)        // star is a celebrity
        f.post("star", "old", 10)
        f.follow("late", "star", 50)
        f.post("star", "new", 60)
        assertEquals(listOf("new"), f.feed("late", 10))
        assertEquals(listOf("new", "old"), f.feed("a", 10))
    }

    @Test
    fun ordinaryAuthorsAlsoOnlyReachCurrentFollowers() {
        val f = FeedService(10)
        f.follow("early", "ann", 0)
        f.post("ann", "p1", 10)
        f.follow("late", "ann", 20)
        f.post("ann", "p2", 30)
        assertEquals(listOf("p2", "p1"), f.feed("early", 10))
        assertEquals(listOf("p2"), f.feed("late", 10))
    }

    @Test
    fun equalTimestampsAreOrderedByPostId() {
        val f = FeedService(10)
        f.follow("me", "ann", 0)
        f.post("ann", "b", 10); f.post("ann", "a", 10); f.post("ann", "c", 10)
        assertEquals(listOf("a", "b", "c"), f.feed("me", 10))
    }

    @Test
    fun selfFollowAndDuplicateFollowAreIgnored() {
        val f = FeedService(2)
        f.follow("ann", "ann", 0)
        f.follow("bob", "ann", 0); f.follow("bob", "ann", 1)
        f.post("ann", "p1", 5)
        assertEquals(1, f.pushWrites)                              // only bob, and only once; ann is NOT a celebrity yet
        assertEquals(emptyList(), f.feed("ann", 10))
    }

    @Test
    fun pushWritesStayProportionalToFollowersOfOrdinaryAccountsOnly() {
        val f = FeedService(100)
        for (i in 0 until 50) f.follow("u$i", "ann", 0)
        repeat(4) { f.post("ann", "p$it", it.toLong()) }
        assertEquals(200, f.pushWrites)
    }

    @Test
    fun feedOfUnknownUserIsEmpty() {
        assertEquals(emptyList(), FeedService().feed("nobody", 10))
    }
}
