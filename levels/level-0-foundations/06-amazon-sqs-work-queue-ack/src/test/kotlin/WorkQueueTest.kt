import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WorkQueueTest {

    @Test
    fun sendReceiveAck() {
        val q = WorkQueue()
        assertEquals("m1", q.send("email:42"))
        val m = q.receive(0)!!
        assertEquals(Message("m1", "email:42", "m1#1", 1), m)
        assertTrue(q.ack(m.receipt, 5))
        assertNull(q.receive(100))
    }

    @Test
    fun receivedMessagesAreHiddenNotDeleted() {
        val q = WorkQueue(30)
        q.send("a"); q.send("b")
        assertEquals("m1", q.receive(0)!!.id)
        assertEquals("m2", q.receive(1)!!.id)
        assertNull(q.receive(2))
        assertEquals(2, q.inFlightCount(2))
        assertEquals(0, q.visibleCount(2))
    }

    @Test
    fun unackedMessageReappearsAfterTheTimeout() {
        val q = WorkQueue(30)
        q.send("a")
        val first = q.receive(0)!!
        assertNull(q.receive(29))
        val second = q.receive(30)!!
        assertEquals("m1#2", second.receipt)
        assertEquals(2, second.receiveCount)
        assertFalse(q.ack(first.receipt, 31))            // stale receipt from the crashed worker
        assertTrue(q.ack(second.receipt, 31))
    }

    @Test
    fun ackAfterTheTimeoutFails() {
        val q = WorkQueue(30)
        q.send("a")
        val m = q.receive(0)!!
        assertFalse(q.ack(m.receipt, 30))
        assertEquals(1, q.visibleCount(30))
    }

    @Test
    fun extendingVisibilityKeepsTheMessageHidden() {
        val q = WorkQueue(30)
        q.send("long job")
        val m = q.receive(0)!!
        assertTrue(q.extendVisibility(m.receipt, 60, 25))  // now hidden until 85
        assertNull(q.receive(80))
        assertTrue(q.ack(m.receipt, 84))
        assertFalse(q.extendVisibility("m1#1", 10, 90))
    }

    @Test
    fun poisonMessagesGoToTheDeadLetterQueue() {
        val q = WorkQueue(10, maxReceives = 2)
        q.send("poison"); q.send("fine")
        assertEquals("m1", q.receive(0)!!.id)
        assertEquals("m2", q.receive(0)!!.id)
        assertEquals("m1#2", q.receive(10)!!.receipt)
        val next = q.receive(20)                           // m1 has been received twice already -> DLQ
        assertEquals("m2", next!!.id)
        assertEquals(listOf("m1"), q.deadLetters())
        assertEquals(0, q.visibleCount(25))
    }

    @Test
    fun fifoOrderAmongVisibleMessages() {
        val q = WorkQueue(10)
        q.send("a"); q.send("b"); q.send("c")
        val a = q.receive(0)!!
        q.receive(0)
        q.ack(a.receipt, 1)
        assertEquals("m3", q.receive(2)!!.id)
        assertEquals("m2", q.receive(10)!!.id)           // b timed out and is first in line again
    }

    @Test
    fun unknownReceipts() {
        val q = WorkQueue()
        assertFalse(q.ack("m9#1", 0))
        assertFalse(q.ack("garbage", 0))
    }
}
