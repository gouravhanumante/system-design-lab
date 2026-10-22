import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MessageStatusTrackerTest {

    @Test
    fun newMessageIsSent() {
        val t = MessageStatusTracker()
        t.send("m")
        assertEquals(Status.SENT, t.status("m"))
    }

    @Test
    fun statusMovesForward() {
        val t = MessageStatusTracker()
        t.send("m")
        t.markDelivered("m")
        assertEquals(Status.DELIVERED, t.status("m"))
        t.markRead("m")
        assertEquals(Status.READ, t.status("m"))
    }

    @Test
    fun readCanSkipDelivered() {
        val t = MessageStatusTracker()
        t.send("m")
        t.markRead("m")
        assertEquals(Status.READ, t.status("m"))
    }

    @Test
    fun staleUpdatesAreIgnored() {
        val t = MessageStatusTracker()
        t.send("m")
        t.markRead("m")
        t.markDelivered("m")
        assertEquals(Status.READ, t.status("m"))
        t.send("m")                                  // duplicate send does not reset
        assertEquals(Status.READ, t.status("m"))
    }

    @Test
    fun unknownMessagesStayUnknown() {
        val t = MessageStatusTracker()
        t.markDelivered("ghost")
        t.markRead("ghost")
        assertNull(t.status("ghost"))
    }

    @Test
    fun messagesAreIndependent() {
        val t = MessageStatusTracker()
        t.send("a"); t.send("b")
        t.markRead("a")
        assertEquals(Status.READ, t.status("a"))
        assertEquals(Status.SENT, t.status("b"))
    }
}
