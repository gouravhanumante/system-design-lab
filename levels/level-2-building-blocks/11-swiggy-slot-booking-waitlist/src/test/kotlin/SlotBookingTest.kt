import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class SlotBookingTest {

    private fun slotWith(capacity: Int, vararg orders: String): SlotBooking {
        val s = SlotBooking()
        s.addSlot("7am", capacity)
        orders.forEach { s.book("7am", it) }
        return s
    }

    @Test
    fun confirmsUntilFullThenWaitlists() {
        val s = SlotBooking()
        s.addSlot("7am", 2)
        assertEquals(BookingStatus.CONFIRMED, s.book("7am", "a"))
        assertEquals(BookingStatus.CONFIRMED, s.book("7am", "b"))
        assertEquals(BookingStatus.WAITLISTED, s.book("7am", "c"))
        assertEquals(BookingStatus.WAITLISTED, s.book("7am", "d"))
        assertEquals("WAITLISTED:7am:2", s.status("d"))
        assertEquals("CONFIRMED:7am", s.status("a"))
    }

    @Test
    fun cancellationPromotesTheFirstInLine() {
        val s = slotWith(2, "a", "b", "c", "d")
        s.cancel("a")
        assertEquals(listOf("b", "c"), s.confirmed("7am"))
        assertEquals(listOf("d"), s.waitlist("7am"))
        assertEquals("WAITLISTED:7am:1", s.status("d"))
        assertNull(s.status("a"))
    }

    @Test
    fun cancellingAWaitlistedOrderJustRemovesIt() {
        val s = slotWith(1, "a", "b", "c")
        s.cancel("b")
        assertEquals(listOf("a"), s.confirmed("7am"))
        assertEquals(listOf("c"), s.waitlist("7am"))
        s.cancel("ghost")
    }

    @Test
    fun bookingIsIdempotentAndOneSlotPerOrder() {
        val s = slotWith(1, "a", "b")
        s.addSlot("9am", 5)
        assertEquals(BookingStatus.CONFIRMED, s.book("7am", "a"))
        assertEquals(BookingStatus.WAITLISTED, s.book("7am", "b"))
        assertEquals(listOf("b"), s.waitlist("7am"))
        assertFailsWith<IllegalArgumentException> { s.book("9am", "a") }
        s.cancel("a")
        assertEquals(BookingStatus.CONFIRMED, s.book("9am", "a"))
    }

    @Test
    fun raisingCapacityPromotesInOrder() {
        val s = slotWith(1, "a", "b", "c", "d")
        s.setCapacity("7am", 3)
        assertEquals(listOf("a", "b", "c"), s.confirmed("7am"))
        assertEquals(listOf("d"), s.waitlist("7am"))
    }

    @Test
    fun loweringCapacityBumpsMostRecentConfirmationsToTheFront() {
        val s = slotWith(4, "a", "b", "c", "d", "e", "f")
        s.setCapacity("7am", 2)
        assertEquals(listOf("a", "b"), s.confirmed("7am"))
        assertEquals(listOf("c", "d", "e", "f"), s.waitlist("7am"))
        assertEquals("WAITLISTED:7am:1", s.status("c"))
    }

    @Test
    fun promotedOrderCountsAsConfirmedAtPromotionTime() {
        val s = slotWith(2, "a", "b", "c")
        s.cancel("a")                                     // c promoted after b
        s.setCapacity("7am", 1)                           // c is the most recent confirmation
        assertEquals(listOf("b"), s.confirmed("7am"))
        assertEquals(listOf("c"), s.waitlist("7am"))
    }

    @Test
    fun zeroCapacitySlot() {
        val s = slotWith(0, "a")
        assertEquals(emptyList(), s.confirmed("7am"))
        assertEquals("WAITLISTED:7am:1", s.status("a"))
        s.setCapacity("7am", 1)
        assertEquals("CONFIRMED:7am", s.status("a"))
    }

    @Test
    fun invalidCalls() {
        val s = slotWith(1)
        assertFailsWith<IllegalArgumentException> { s.addSlot("7am", 3) }
        assertFailsWith<IllegalArgumentException> { s.addSlot("x", -1) }
        assertFailsWith<IllegalArgumentException> { s.book("nope", "a") }
        assertFailsWith<IllegalArgumentException> { s.setCapacity("nope", 1) }
        assertFailsWith<IllegalArgumentException> { s.setCapacity("7am", -1) }
    }
}
