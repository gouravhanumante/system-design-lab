class OpeningHours {

    /** day: 0 = Monday … 6 = Sunday. Times are "HH:MM". */
    fun addInterval(day: Int, open: String, close: String) {
        TODO()
    }

    fun isOpen(day: Int, time: String): Boolean {
        TODO()
    }

    /** (day, "HH:MM") of now if open, else of the next opening; null if there are no intervals. */
    fun nextOpening(day: Int, time: String): Pair<Int, String>? {
        TODO()
    }
}
