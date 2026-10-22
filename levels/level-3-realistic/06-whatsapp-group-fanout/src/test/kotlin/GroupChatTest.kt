import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GroupChatTest {

    private fun group(vararg members: String): GroupChat {
        val g = GroupChat()
        members.forEach { g.addMember(it) }
        return g
    }

    @Test
    fun everyoneButTheSenderReceivesTheMessage() {
        val g = group("a", "b", "c")
        g.send("a", "m1", "hello")
        assertEquals(listOf("hello"), g.inbox("b"))
        assertEquals(listOf("hello"), g.inbox("c"))
        assertEquals(emptyList(), g.inbox("a"))
    }

    @Test
    fun inboxKeepsSendOrder() {
        val g = group("a", "b")
        g.send("a", "m1", "one"); g.send("a", "m2", "two"); g.send("a", "m3", "three")
        assertEquals(listOf("one", "two", "three"), g.inbox("b"))
    }

    @Test
    fun lateJoinersDoNotSeeOldMessages() {
        val g = group("a", "b")
        g.send("a", "m1", "old")
        g.addMember("d")
        g.send("a", "m2", "new")
        assertEquals(listOf("new"), g.inbox("d"))
        assertEquals(listOf("old", "new"), g.inbox("b"))
    }

    @Test
    fun leavingKeepsAlreadyReceivedMessages() {
        val g = group("a", "b")
        g.send("a", "m1", "hello")
        g.removeMember("b")
        assertEquals(listOf("hello"), g.inbox("b"))
        g.send("a", "m2", "after")
        assertEquals(listOf("hello"), g.inbox("b"))
    }

    @Test
    fun readReceiptsOnlyCountForRecipients() {
        val g = group("a", "b", "c")
        g.send("a", "m1", "x")
        g.markRead("b", "m1")
        g.markRead("a", "m1")                    // the sender is not a recipient
        g.markRead("outsider", "m1")
        g.markRead("b", "ghost")
        assertEquals(setOf("b"), g.readBy("m1"))
        assertEquals(emptySet(), g.readBy("ghost"))
    }

    @Test
    fun readByAllNeedsEveryCurrentRecipient() {
        val g = group("a", "b", "c")
        g.send("a", "m1", "x")
        assertFalse(g.isReadByAll("m1"))
        g.markRead("b", "m1")
        assertFalse(g.isReadByAll("m1"))
        g.markRead("c", "m1")
        assertTrue(g.isReadByAll("m1"))
    }

    @Test
    fun membersWhoLeftNoLongerBlockReadByAll() {
        val g = group("a", "b", "c")
        g.send("a", "m1", "x")
        g.markRead("b", "m1")
        assertFalse(g.isReadByAll("m1"))
        g.removeMember("c")
        assertTrue(g.isReadByAll("m1"))
    }

    @Test
    fun lateJoinersAreNotRequiredToReadOldMessages() {
        val g = group("a", "b")
        g.send("a", "m1", "x")
        g.addMember("late")
        g.markRead("b", "m1")
        assertTrue(g.isReadByAll("m1"))
    }

    @Test
    fun soloSenderAndUnknownMessages() {
        val g = group("a")
        g.send("a", "m1", "echo")
        assertTrue(g.isReadByAll("m1"))           // nobody else: vacuously true
        assertFalse(g.isReadByAll("ghost"))
    }

    @Test
    fun nonMembersCannotSendAndDuplicatesAreIgnored() {
        val g = group("a", "b")
        assertFailsWith<IllegalStateException> { g.send("stranger", "m1", "x") }
        g.send("a", "m1", "first")
        g.send("a", "m1", "second")
        assertEquals(listOf("first"), g.inbox("b"))
        g.addMember("b"); g.removeMember("nobody")
        g.send("a", "m2", "again")
        assertEquals(listOf("first", "again"), g.inbox("b"))
    }
}
