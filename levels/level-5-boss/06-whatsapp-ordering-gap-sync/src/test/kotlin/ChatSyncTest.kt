import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChatSyncTest {

    private fun serverWith(n: Int, chat: String = "c"): Pair<ChatServer, List<Message>> {
        val s = ChatServer()
        val msgs = (1..n).map { s.send(chat, "id$it", "text$it") }
        return s to msgs
    }

    @Test
    fun serverAssignsSequenceNumbersPerChat() {
        val s = ChatServer()
        assertEquals(1L, s.send("a", "m1", "x").seq)
        assertEquals(2L, s.send("a", "m2", "y").seq)
        assertEquals(1L, s.send("b", "m1", "z").seq)
    }

    @Test
    fun sendingTheSameMessageIdAgainReturnsTheOriginal() {
        val s = ChatServer()
        val first = s.send("a", "m1", "x")
        val again = s.send("a", "m1", "x")
        assertEquals(first, again)
        assertEquals(2L, s.send("a", "m2", "y").seq)
    }

    @Test
    fun fetchReturnsMessagesAfterTheGivenSeqInOrderWithLimit() {
        val (s, msgs) = serverWith(7)
        assertEquals(msgs.subList(0, 3), s.fetch("c", 0, 3))
        assertEquals(msgs.subList(4, 7), s.fetch("c", 4, 10))
        assertEquals(emptyList(), s.fetch("c", 7, 10))
        assertEquals(emptyList(), s.fetch("other", 0, 10))
        assertEquals(4, s.fetchCount)
    }

    @Test
    fun outOfOrderPushesDisplayOnlyTheContiguousPrefix() {
        val (s, m) = serverWith(5)
        val d = DeviceClient(s, "c")
        d.receive(m[1]); d.receive(m[0]); d.receive(m[4])
        assertEquals(listOf(m[0], m[1]), d.displayed())
        assertTrue(d.hasGap())
    }

    @Test
    fun noGapWhenEverythingArrivedInOrderOrNothingArrived() {
        val (s, m) = serverWith(3)
        val d = DeviceClient(s, "c")
        assertEquals(emptyList(), d.displayed())
        assertFalse(d.hasGap())
        d.receive(m[0]); d.receive(m[1])
        assertFalse(d.hasGap())
        assertEquals(m.subList(0, 2), d.displayed())
    }

    @Test
    fun duplicatePushesAreHarmless() {
        val (s, m) = serverWith(2)
        val d = DeviceClient(s, "c")
        d.receive(m[0]); d.receive(m[0]); d.receive(m[1]); d.receive(m[1])
        assertEquals(m, d.displayed())
    }

    @Test
    fun missingFirstMessageMeansNothingIsDisplayed() {
        val (s, m) = serverWith(3)
        val d = DeviceClient(s, "c")
        d.receive(m[1]); d.receive(m[2])
        assertEquals(emptyList(), d.displayed())
        assertTrue(d.hasGap())
    }

    @Test
    fun syncFillsGaps() {
        val (s, m) = serverWith(5)
        val d = DeviceClient(s, "c")
        d.receive(m[0]); d.receive(m[1]); d.receive(m[4])
        d.sync()
        assertEquals(m, d.displayed())
        assertFalse(d.hasGap())
    }

    @Test
    fun syncFetchesInBatchesAndStopsOnAShortBatch() {
        val (s, m) = serverWith(12)
        val d = DeviceClient(s, "c", batchSize = 5)
        val before = s.fetchCount
        d.sync()
        assertEquals(m, d.displayed())
        assertEquals(3, s.fetchCount - before)                  // 5 + 5 + 2
    }

    @Test
    fun syncWhenAlreadyUpToDateCostsOneFetch() {
        val (s, m) = serverWith(4)
        val d = DeviceClient(s, "c", batchSize = 10)
        m.forEach { d.receive(it) }
        val before = s.fetchCount
        d.sync()
        assertEquals(1, s.fetchCount - before)
        assertEquals(m, d.displayed())
    }

    @Test
    fun exactMultipleOfBatchSizeNeedsOneExtraEmptyFetch() {
        val (s, m) = serverWith(10)
        val d = DeviceClient(s, "c", batchSize = 5)
        val before = s.fetchCount
        d.sync()
        assertEquals(m, d.displayed())
        assertEquals(3, s.fetchCount - before)                  // 5 + 5 + (empty)
    }

    @Test
    fun twoDevicesWithDifferentPushesConvergeAfterSync() {
        val (s, m) = serverWith(6)
        val phone = DeviceClient(s, "c", 2)
        val laptop = DeviceClient(s, "c", 4)
        phone.receive(m[0]); phone.receive(m[3])
        laptop.receive(m[5]); laptop.receive(m[2])
        s.send("c", "late", "arrived while syncing")
        phone.sync(); laptop.sync()
        assertEquals(7, phone.displayed().size)
        assertEquals(phone.displayed(), laptop.displayed())
        assertEquals(listOf(1L, 2L, 3L, 4L, 5L, 6L, 7L), phone.displayed().map { it.seq })
    }
}
