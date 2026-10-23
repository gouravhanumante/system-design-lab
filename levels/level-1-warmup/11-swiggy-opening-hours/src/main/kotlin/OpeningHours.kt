class OpeningHours {

    // ───────────── State ─────────────
    // Trick: measure everything in "minutes since Monday 00:00". A week has 7 × 1440 = 10080 minutes.

    private val minutesPerWeek = 7 * 1440

    /** Opening intervals as (start, end) in minutes since Monday 00:00; open for start ≤ t < end. `end` may go past the week end (Sunday night → Monday). */
    private val intervals = ArrayList<Pair<Int, Int>>()

    // ───────────── Helpers ─────────────

    /**
     * Step 1 — `"09:30"` → 570. Must look exactly like `HH:MM` with hour 00–23 and minute 00–59,
     * otherwise `IllegalArgumentException`.
     */
    private fun toMinutes(time: String): Int {
        TODO("Step 1")
    }

    /** Step 1 — day (0 = Monday … 6 = Sunday; anything else → `IllegalArgumentException`) + time → `day × 1440 + toMinutes(time)`. */
    private fun minuteOfWeek(day: Int, time: String): Int {
        TODO("Step 1")
    }

    /**
     * Step 2 — is minute-of-week [m] inside any interval (start ≤ m < end)?
     * Also check `m + minutesPerWeek`, so a Sunday-night interval that runs into "day 7" covers Monday morning.
     */
    private fun isOpenAt(m: Int): Boolean {
        TODO("Step 2")
    }

    // ───────────── API ─────────────

    /**
     * Step 2 — open on [day] from [open] until [close].
     * - [close] later than [open] → same day. [close] earlier → overnight, it closes the NEXT day at [close].
     * - open == close → `IllegalArgumentException`.
     */
    fun addInterval(day: Int, open: String, close: String) {
        TODO("Step 2")
    }

    /** Step 2 — open at that moment? (Exactly at closing time = closed.) */
    fun isOpen(day: Int, time: String): Boolean {
        TODO("Step 2")
    }

    /**
     * Step 3 — open now → return (day, time) unchanged. Otherwise the next interval START going forward in time
     * (wrapping from Sunday to Monday), as (day, "HH:MM"). No intervals → `null`.
     * Tip: distance forward from now to a start = `Math.floorMod(start - now, minutesPerWeek)`; take the smallest.
     */
    fun nextOpening(day: Int, time: String): Pair<Int, String>? {
        TODO("Step 3")
    }
}
