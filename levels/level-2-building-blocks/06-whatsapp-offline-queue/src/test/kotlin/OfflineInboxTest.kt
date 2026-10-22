import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OfflineInboxTest {

    @Test
    fun onlineUsersGetMessagesImmediately() {
        val i = OfflineInbox()
        i.connect("bob", 0)
        i.send("bob", "m1", "hi", 1)
        assertEquals(listOf("hi"), i.delivered("bob"))
    }

    @Test
    fun offlineMessagesWaitAndArriveInOrderOnConnect() {
        val i = OfflineInbox()
        i.send("bob", "m1", "one", 1)
        i.send("bob", "m2", "two", 2)
        i.send("bob", "m3", "three", 3)
        assertEquals(emptyList(), i.delivered("bob"))
        assertEquals(listOf("one", "two", "three"), i.connect("bob", 10))
        assertEquals(listOf("one", "two", "three"), i.delivered("bob"))
        assertTrue(i.isOnline("bob"))
    }

    @Test
    fun expiredMessagesAreDroppedExactlyAtTheTtl() {
        val i = OfflineInbox(100)
        i.send("bob", "m1", "old", 0)
        i.send("bob", "m2", "fresh", 50)
        assertEquals(listOf("fresh"), i.connect("bob", 100))      // m1: age 100 -> expired; m2: age 50
    }

    @Test
    fun everythingExpiredMeansNothingDelivered() {
        val i = OfflineInbox(10)
        i.send("bob", "m1", "a", 0)
        assertEquals(emptyList(), i.connect("bob", 1000))
        assertEquals(emptyList(), i.delivered("bob"))
    }

    @Test
    fun connectingTwiceDoesNotRedeliver() {
        val i = OfflineInbox()
        i.send("bob", "m1", "a", 0)
        assertEquals(listOf("a"), i.connect("bob", 1))
        assertEquals(emptyList(), i.connect("bob", 2))
        assertEquals(listOf("a"), i.delivered("bob"))
    }

    @Test
    fun disconnectThenMessagesQueueAgain() {
        val i = OfflineInbox()
        i.connect("bob", 0)
        i.send("bob", "m1", "live", 1)
        i.disconnect("bob")
        assertFalse(i.isOnline("bob"))
        i.send("bob", "m2", "later", 2)
        assertEquals(listOf("live"), i.delivered("bob"))
        assertEquals(listOf("later"), i.connect("bob", 5))
        assertEquals(listOf("live", "later"), i.delivered("bob"))
    }

    @Test
    fun retriesOfTheSameMessageAreIgnored() {
        val i = OfflineInbox()
        i.send("bob", "m1", "x", 0)
        i.send("bob", "m1", "x", 1)                 // retry while queued
        assertEquals(listOf("x"), i.connect("bob", 2))
        i.send("bob", "m1", "x", 3)                 // retry after delivery
        assertEquals(listOf("x"), i.delivered("bob"))
    }

    @Test
    fun sameMessageIdForDifferentUsersAreDifferentMessages() {
        val i = OfflineInbox()
        i.send("bob", "m1", "for bob", 0)
        i.send("amy", "m1", "for amy", 0)
        assertEquals(listOf("for bob"), i.connect("bob", 1))
        assertEquals(listOf("for amy"), i.connect("amy", 1))
    }
}
