data class Record(val partition: Int, val offset: Long, val key: String?, val value: String)

class Topic(private val partitions: Int) {

    fun produce(key: String?, value: String): Record {
        TODO()
    }

    fun read(partition: Int, fromOffset: Long, maxRecords: Int): List<Record> {
        TODO()
    }

    fun endOffset(partition: Int): Long {
        TODO()
    }
}
