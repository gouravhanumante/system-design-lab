data class Record(val offset: Long, val value: String)

class AppendLog {

    fun append(value: String): Long {
        TODO()
    }

    fun read(fromOffset: Long, maxRecords: Int): List<Record> {
        TODO()
    }

    fun endOffset(): Long {
        TODO()
    }
}
