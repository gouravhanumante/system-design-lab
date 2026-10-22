enum class BookingStatus { CONFIRMED, WAITLISTED }

class SlotBooking {

    fun addSlot(slotId: String, capacity: Int) {
        TODO()
    }

    fun book(slotId: String, orderId: String): BookingStatus {
        TODO()
    }

    fun cancel(orderId: String) {
        TODO()
    }

    fun setCapacity(slotId: String, capacity: Int) {
        TODO()
    }

    /** "CONFIRMED:<slot>", "WAITLISTED:<slot>:<position>" or null. */
    fun status(orderId: String): String? {
        TODO()
    }

    fun confirmed(slotId: String): List<String> {
        TODO()
    }

    fun waitlist(slotId: String): List<String> {
        TODO()
    }
}
