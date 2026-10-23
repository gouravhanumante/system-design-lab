enum class ReceiveResult { ACCEPTED, DUPLICATE, CORRUPT, OUT_OF_RANGE }

/** Simple checksum used by clients and the server: the sum of all unsigned byte values. (Provided.) */
fun checksumOf(data: ByteArray): Long = data.sumOf { (it.toInt() and 0xFF).toLong() }

class ChunkedUpload(private val totalChunks: Int) {

    /** One slot per chunk index; `null` = not received yet. */
    private val slots = arrayOfNulls<ByteArray>(totalChunks)

    /**
     * Step 1 — store one chunk. Check in THIS order:
     * 1. index outside `0 until totalChunks` → `OUT_OF_RANGE`
     * 2. `checksum != checksumOf(data)` → `CORRUPT` (nothing stored; the client can retry)
     * 3. slot already filled → `DUPLICATE` (keep the first copy)
     * 4. otherwise store a COPY of [data] (`data.copyOf()`, so later changes to the caller's array don't affect you) → `ACCEPTED`
     */
    fun receive(index: Int, data: ByteArray, checksum: Long): ReceiveResult {
        TODO("Step 1")
    }

    /** Step 2 — indexes not received yet, ascending. (This is what a client asks for when it resumes.) */
    fun missingChunks(): List<Int> {
        TODO("Step 2")
    }

    /** Step 2 — every slot filled? (0 chunks → `true`.) */
    fun isComplete(): Boolean {
        TODO("Step 2")
    }

    /** Step 3 — all chunks joined in index order. Not complete → `IllegalStateException` (`check(...)` does this). */
    fun assemble(): ByteArray {
        TODO("Step 3")
    }
}
