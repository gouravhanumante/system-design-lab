data class Record(val offset: Long, val value: String)

class AppendLog {

    /** The log: index in this list = the record's offset. Records are only ever added at the end, never changed. */
    private val values = ArrayList<String>()

    /** Step 1 — add [value] at the end and return its offset (0, 1, 2, …). */
    fun append(value: String): Long {
        TODO("Step 1")
    }

    /**
     * Step 2 — up to [maxRecords] records starting at [fromOffset], in order.
     * - [fromOffset] < 0 → `IllegalArgumentException`.
     * - [maxRecords] ≤ 0, or [fromOffset] at/after the end → empty list.
     * - Fewer records left than [maxRecords] → return what's there.
     */
    fun read(fromOffset: Long, maxRecords: Int): List<Record> {
        TODO("Step 2")
    }

    /** Step 1 — the offset the NEXT append will get (= number of records). */
    fun endOffset(): Long {
        TODO("Step 1")
    }
}
