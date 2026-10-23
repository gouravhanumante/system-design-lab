data class Record(val partition: Int, val offset: Long, val key: String?, val value: String)

class Topic(private val partitions: Int) {

    // ───────────── State ─────────────

    /** One append-only log per partition. Offsets are per partition: each starts at 0. */
    private val logs = Array(partitions) { ArrayList<Record>() }

    /** Counter for messages WITHOUT a key (round robin). Keyed messages don't change it. */
    private var roundRobin = 0

    // ───────────── Helpers ─────────────

    /**
     * Step 1 — which partition a message goes to.
     * - With a key: `Math.floorMod(key.hashCode(), partitions)` (floorMod stays ≥ 0 even for negative hash codes;
     *   `%` would give a negative number). Same key → always the same partition → order per key is kept.
     * - Without a key: `roundRobin % partitions`, then `roundRobin++`.
     */
    private fun choosePartition(key: String?): Int {
        TODO("Step 1")
    }

    /** Step 3 — partition outside `0 until partitions` → `IllegalArgumentException`. */
    private fun requireValidPartition(partition: Int) {
        TODO("Step 3")
    }

    // ───────────── API ─────────────

    /** Step 2 — append to the chosen partition's log; the record's offset = that log's size before adding. Return the record. */
    fun produce(key: String?, value: String): Record {
        TODO("Step 2")
    }

    /**
     * Step 3 — like a single log, but for one partition: up to [maxRecords] records from [fromOffset].
     * Bad partition or negative offset → `IllegalArgumentException`; `maxRecords ≤ 0` or offset past the end → empty.
     */
    fun read(partition: Int, fromOffset: Long, maxRecords: Int): List<Record> {
        TODO("Step 3")
    }

    /** Step 3 — next offset in that partition (bad partition → `IllegalArgumentException`). */
    fun endOffset(partition: Int): Long {
        TODO("Step 3")
    }
}
