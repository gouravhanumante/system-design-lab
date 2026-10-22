enum class ReceiveResult { ACCEPTED, DUPLICATE, CORRUPT, OUT_OF_RANGE }

/** Simple checksum used by clients and the server: the sum of all unsigned byte values. (Provided.) */
fun checksumOf(data: ByteArray): Long = data.sumOf { (it.toInt() and 0xFF).toLong() }

class ChunkedUpload(private val totalChunks: Int) {

    fun receive(index: Int, data: ByteArray, checksum: Long): ReceiveResult {
        TODO()
    }

    fun missingChunks(): List<Int> {
        TODO()
    }

    fun isComplete(): Boolean {
        TODO()
    }

    /** All chunks in index order. Throws IllegalStateException if something is missing. */
    fun assemble(): ByteArray {
        TODO()
    }
}
