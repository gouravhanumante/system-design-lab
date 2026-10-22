import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StoriesTest {

    @Test
    fun storiesExpireAfterTheTtl() {
        val s = Stories(100)
        s.post("ann", "s1", Audience.EVERYONE, 0)
        assertTrue(s.canView("bob", "s1", 99))
        assertFalse(s.canView("bob", "s1", 100))
        assertFalse(s.canView("ann", "s1", 100))          // not even the owner
    }

    @Test
    fun everyoneStoriesAreOpenToAll() {
        val s = Stories()
        s.post("ann", "s1", Audience.EVERYONE, 0)
        assertTrue(s.canView("stranger", "s1", 10))
        assertFalse(s.canView("stranger", "unknown", 10))
    }

    @Test
    fun closeFriendsStoriesAreRestrictedAndCheckedAtViewTime() {
        val s = Stories(100)
        s.post("ann", "s1", Audience.CLOSE_FRIENDS, 0)
        s.addCloseFriend("ann", "bob")
        assertTrue(s.canView("bob", "s1", 50))
        assertFalse(s.canView("carol", "s1", 50))
        assertTrue(s.canView("ann", "s1", 50))             // owner
        s.removeCloseFriend("ann", "bob")
        assertFalse(s.canView("bob", "s1", 51))
    }

    @Test
    fun friendAddedLaterCanSeeExistingStories() {
        val s = Stories(100)
        s.post("ann", "s1", Audience.CLOSE_FRIENDS, 0)
        assertFalse(s.canView("bob", "s1", 5))
        s.addCloseFriend("ann", "bob")
        assertTrue(s.canView("bob", "s1", 6))
    }

    @Test
    fun viewersAreUniqueOrderedAndExcludeTheOwner() {
        val s = Stories()
        s.post("ann", "s1", Audience.EVERYONE, 0)
        assertTrue(s.view("bob", "s1", 1))
        assertTrue(s.view("cat", "s1", 2))
        assertTrue(s.view("bob", "s1", 3))                 // again: still true, but not duplicated
        assertTrue(s.view("ann", "s1", 4))                 // owner
        assertEquals(listOf("bob", "cat"), s.viewers("s1"))
    }

    @Test
    fun blockedViewsAreNotRecordedAndEarlierViewsSurviveAccessChanges() {
        val s = Stories(100)
        s.post("ann", "s1", Audience.CLOSE_FRIENDS, 0)
        s.addCloseFriend("ann", "bob")
        assertFalse(s.view("carol", "s1", 1))
        assertTrue(s.view("bob", "s1", 2))
        s.removeCloseFriend("ann", "bob")
        assertFalse(s.view("bob", "s1", 3))
        assertEquals(listOf("bob"), s.viewers("s1"))
        assertEquals(emptyList(), s.viewers("unknown"))
    }

    @Test
    fun visibleStoriesAreOldestFirstAndRespectAudienceAndExpiry() {
        val s = Stories(100)
        s.post("ann", "b-late", Audience.EVERYONE, 30)
        s.post("ann", "a-early", Audience.EVERYONE, 10)
        s.post("ann", "secret", Audience.CLOSE_FRIENDS, 20)
        s.post("ann", "ancient", Audience.EVERYONE, -200)
        assertEquals(listOf("a-early", "b-late"), s.visibleStories("ann", "bob", 50))
        s.addCloseFriend("ann", "bob")
        assertEquals(listOf("a-early", "secret", "b-late"), s.visibleStories("ann", "bob", 50))
        assertEquals(listOf("b-late"), s.visibleStories("ann", "bob", 125))   // a-early and secret expired
        assertEquals(emptyList(), s.visibleStories("nobody", "bob", 50))
    }

    @Test
    fun highlightsNeverExpireButKeepTheirAudience() {
        val s = Stories(100)
        s.post("ann", "s1", Audience.CLOSE_FRIENDS, 0)
        s.addCloseFriend("ann", "bob")
        assertTrue(s.highlight("ann", "s1", 50))
        assertTrue(s.canView("bob", "s1", 10_000))
        assertFalse(s.canView("carol", "s1", 10_000))
        assertEquals(listOf("s1"), s.visibleStories("ann", "bob", 10_000))
    }

    @Test
    fun onlyLiveStoriesOfTheOwnerCanBeHighlighted() {
        val s = Stories(100)
        s.post("ann", "s1", Audience.EVERYONE, 0)
        assertFalse(s.highlight("bob", "s1", 10))          // not the owner
        assertFalse(s.highlight("ann", "ghost", 10))
        assertFalse(s.highlight("ann", "s1", 100))          // already expired
        assertFalse(s.canView("ann", "s1", 100))
    }

    @Test
    fun reposingAnExistingIdIsIgnored() {
        val s = Stories(100)
        s.post("ann", "s1", Audience.EVERYONE, 0)
        s.post("ann", "s1", Audience.CLOSE_FRIENDS, 90)    // ignored: would have extended life and restricted it
        assertTrue(s.canView("bob", "s1", 50))
        assertFalse(s.canView("bob", "s1", 100))
    }
}
