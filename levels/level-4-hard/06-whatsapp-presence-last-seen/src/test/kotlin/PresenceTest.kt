import kotlin.test.Test
import kotlin.test.assertEquals

class PresenceTest {

    @Test
    fun onlineWhileHeartbeatsAreFresh() {
        val p = Presence(30)
        p.heartbeat("bob", 100)
        assertEquals("online", p.view("alice", "bob", 100))
        assertEquals("online", p.view("alice", "bob", 129))
    }

    @Test
    fun becomesLastSeenExactlyAtTheTimeout() {
        val p = Presence(30)
        p.heartbeat("bob", 100)
        assertEquals("last seen 100", p.view("alice", "bob", 130))
        assertEquals("last seen 100", p.view("alice", "bob", 5000))
    }

    @Test
    fun newerHeartbeatReplacesTheOldOne() {
        val p = Presence(30)
        p.heartbeat("bob", 100)
        p.heartbeat("bob", 200)
        assertEquals("last seen 200", p.view("alice", "bob", 300))
    }

    @Test
    fun neverSeenUserIsUnknown() {
        assertEquals("unknown", Presence().view("alice", "ghost", 10))
    }

    @Test
    fun nobodyHidesPresenceFromEveryoneElse() {
        val p = Presence(30)
        p.heartbeat("bob", 100)
        p.setVisibility("bob", Visibility.NOBODY)
        assertEquals("hidden", p.view("alice", "bob", 100))
        assertEquals("online", p.view("bob", "bob", 100))        // you always see yourself
    }

    @Test
    fun contactsOnlyUsesTheTargetsContactList() {
        val p = Presence(30)
        p.heartbeat("bob", 100)
        p.setVisibility("bob", Visibility.CONTACTS)
        p.addContact("bob", "alice")
        p.addContact("carol", "bob")                              // carol has bob, but bob does not have carol
        assertEquals("online", p.view("alice", "bob", 100))
        assertEquals("hidden", p.view("carol", "bob", 100))
        assertEquals("hidden", p.view("stranger", "bob", 100))
    }

    @Test
    fun reciprocityHidingYoursHidesEveryonesFromYou() {
        val p = Presence(30)
        p.heartbeat("bob", 100)
        p.setVisibility("alice", Visibility.NOBODY)
        assertEquals("hidden", p.view("alice", "bob", 100))
        p.setVisibility("alice", Visibility.EVERYONE)
        assertEquals("online", p.view("alice", "bob", 100))
    }

    @Test
    fun contactsViewersAreNotBlockedByReciprocity() {
        val p = Presence(30)
        p.heartbeat("bob", 100)
        p.setVisibility("alice", Visibility.CONTACTS)             // alice shows to contacts only: she can still look
        assertEquals("online", p.view("alice", "bob", 100))
    }

    @Test
    fun hiddenBeatsUnknown() {
        val p = Presence(30)
        p.setVisibility("ghost", Visibility.NOBODY)
        assertEquals("hidden", p.view("alice", "ghost", 5))
        assertEquals("unknown", p.view("ghost", "ghost", 5))
    }
}
