// ---- Provided: a partitioned topic (do not change) ----
data class Record(val partition: Int, val offset: Long, val key: String?, val value: String)

class Topic(val partitionCount: Int) {
    private val logs = Array(partitionCount) { ArrayList<Record>() }
    private var rr = 0

    fun produce(key: String?, value: String): Record {
        val p = if (key != null) Math.floorMod(key.hashCode(), partitionCount) else (rr++ % partitionCount)
        val r = Record(p, logs[p].size.toLong(), key, value)
        logs[p].add(r)
        return r
    }

    fun read(partition: Int, fromOffset: Long, maxRecords: Int): List<Record> {
        val log = logs[partition]
        if (maxRecords <= 0 || fromOffset >= log.size) return emptyList()
        return log.subList(fromOffset.toInt(), minOf(log.size.toLong(), fromOffset + maxRecords).toInt()).toList()
    }

    fun endOffset(partition: Int): Long = logs[partition].size.toLong()
}

// ---- Your job ----
class ConsumerGroups(private val topic: Topic) {

    // ───────────── State ─────────────
    // Both maps are keyed by (group, partition). A missing entry means 0.

    /** Where the group will read next — lives in the consumer's memory, LOST on a crash. */
    private val position = HashMap<Pair<String, Int>, Long>()

    /** The last offset the group saved ("I've processed everything before this") — survives a crash. */
    private val committedOffset = HashMap<Pair<String, Int>, Long>()

    // ───────────── API ─────────────

    /** Step 1 — read up to [maxRecords] from the group's position in that partition, and move the position past what was returned. */
    fun poll(group: String, partition: Int, maxRecords: Int): List<Record> {
        TODO("Step 1")
    }

    /** Step 2 — save the current position as the committed offset. */
    fun commit(group: String, partition: Int) {
        TODO("Step 2")
    }

    /**
     * Step 3 — simulate the consumer crashing and starting again: for EVERY partition of the topic,
     * the group's position goes back to its committed offset (0 if never committed). Records after it get delivered again.
     */
    fun restart(group: String) {
        TODO("Step 3")
    }

    /**
     * Step 5 — move the position (replay old records or skip ahead). Valid offsets: `0..topic.endOffset(partition)`
     * (the end itself is allowed); anything else → `IllegalArgumentException`. Does NOT commit.
     */
    fun seek(group: String, partition: Int, offset: Long) {
        TODO("Step 5")
    }

    /** Step 2 — committed offset (0 if never). */
    fun committed(group: String, partition: Int): Long {
        TODO("Step 2")
    }

    /** Step 4 — how far behind the group is: end of the partition's log minus the COMMITTED offset. */
    fun lag(group: String, partition: Int): Long {
        TODO("Step 4")
    }
}
