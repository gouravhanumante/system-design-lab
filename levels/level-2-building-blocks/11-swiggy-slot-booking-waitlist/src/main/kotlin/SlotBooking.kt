enum class BookingStatus { CONFIRMED, WAITLISTED }

class SlotBooking {

    // ───────────── State ─────────────

    private class Slot(var capacity: Int) {
        /** Confirmed orders, in the order they got confirmed. */
        val confirmed = ArrayList<String>()
        /** Waiting orders; index 0 = first in line. */
        val waiting = ArrayList<String>()
    }

    /** slot id → slot. */
    private val slots = HashMap<String, Slot>()

    /** order id → the slot it's booked in (an order can be in only one slot). */
    private val slotOfOrder = HashMap<String, String>()

    // ───────────── Helpers ─────────────

    /** Step 1 — the slot, or `IllegalArgumentException`. */
    private fun slotOrThrow(slotId: String): Slot {
        TODO("Step 1")
    }

    /**
     * Step 2 — make the lists match the capacity:
     * - too many confirmed → move the MOST RECENT confirmations to the FRONT of the waitlist (last confirmed ends up first in line);
     * - room left → move people from the front of the waitlist into confirmed, in order.
     */
    private fun rebalance(slot: Slot) {
        TODO("Step 2")
    }

    // ───────────── API ─────────────

    /** Step 1 — new slot. Capacity < 0 or the id already exists → `IllegalArgumentException`. */
    fun addSlot(slotId: String, capacity: Int) {
        TODO("Step 1")
    }

    /**
     * Step 2 — book [orderId] into [slotId] (unknown slot → `IllegalArgumentException`).
     * - Order already booked in THIS slot → return its current status (retry-safe).
     * - Already booked in ANOTHER slot → `IllegalArgumentException`.
     * - Otherwise add it to the end of the waitlist, [rebalance], and return CONFIRMED or WAITLISTED.
     */
    fun book(slotId: String, orderId: String): BookingStatus {
        TODO("Step 2")
    }

    /** Step 3 — remove the order wherever it is, then [rebalance] (the first waiting order gets the seat). Unknown: do nothing. */
    fun cancel(orderId: String) {
        TODO("Step 3")
    }

    /** Step 4 — change capacity (unknown slot or capacity < 0 → `IllegalArgumentException`), then [rebalance]. */
    fun setCapacity(slotId: String, capacity: Int) {
        TODO("Step 4")
    }

    /** Step 2 — `"CONFIRMED:<slot>"`, `"WAITLISTED:<slot>:<position>"` (position 1 = first in line), or `null` if unknown. */
    fun status(orderId: String): String? {
        TODO("Step 2")
    }

    /** Step 1 — copy of the confirmed list (unknown slot → `IllegalArgumentException`). */
    fun confirmed(slotId: String): List<String> {
        TODO("Step 1")
    }

    /** Step 1 — copy of the waitlist (unknown slot → `IllegalArgumentException`). */
    fun waitlist(slotId: String): List<String> {
        TODO("Step 1")
    }
}
