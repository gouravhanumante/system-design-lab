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

    fun poll(group: String, partition: Int, maxRecords: Int): List<Record> {
        TODO()
    }

    fun commit(group: String, partition: Int) {
        TODO()
    }

    /** Simulates a crash: positions go back to the committed offsets. */
    fun restart(group: String) {
        TODO()
    }

    fun seek(group: String, partition: Int, offset: Long) {
        TODO()
    }

    fun committed(group: String, partition: Int): Long {
        TODO()
    }

    fun lag(group: String, partition: Int): Long {
        TODO()
    }
}
