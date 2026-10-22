class DedupStore(private val chunkSize: Int) {

    fun put(fileName: String, data: ByteArray) {
        TODO()
    }

    fun get(fileName: String): ByteArray {
        TODO()
    }

    fun delete(fileName: String) {
        TODO()
    }

    /** Total size of the distinct chunks currently stored. */
    fun storedBytes(): Long {
        TODO()
    }

    /** Total size of all files as users see them. */
    fun logicalBytes(): Long {
        TODO()
    }
}
