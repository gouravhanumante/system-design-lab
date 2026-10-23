class DedupStore(private val chunkSize: Int) {

    // ───────────── State ─────────────

    /** One distinct chunk, stored once, plus how many places (across all files) use it. */
    private class StoredChunk(val bytes: ByteArray) {
        var refCount = 0
    }

    /** file name → the keys of its chunks, in order. */
    private val files = HashMap<String, List<String>>()

    /** file name → its size in bytes (what the user sees). */
    private val fileSizes = HashMap<String, Long>()

    /** chunk key → the stored chunk. Identical chunks share one entry — that's the deduplication. */
    private val chunks = HashMap<String, StoredChunk>()

    // ───────────── Helper ─────────────

    /**
     * Step 1 — a key that is equal for two chunks exactly when their bytes are equal.
     * Real systems use a hash like SHA-256; here you may simply use `String(bytes, Charsets.ISO_8859_1)` (1 char per byte, lossless).
     * (ByteArray itself can't be a map key — two arrays with the same bytes are not `equals`.)
     */
    private fun chunkKey(bytes: ByteArray): String {
        TODO("Step 1")
    }

    // ───────────── API ─────────────

    /**
     * Steps 1 + 4 — store [data] under [fileName].
     * - Step 4: if the file already exists, `delete` it first (releases its old chunks).
     * - Cut [data] into pieces of [chunkSize] bytes (the last one may be shorter). For each piece: reuse the stored
     *   chunk with the same key or store a new one, and add 1 to its `refCount` (a piece repeated inside one file counts each time).
     * - Remember the file's chunk keys and size.
     */
    fun put(fileName: String, data: ByteArray) {
        TODO("Step 1")
    }

    /** Step 1 — the file's bytes, rebuilt by joining its chunks in order. Unknown file → `IllegalArgumentException`. */
    fun get(fileName: String): ByteArray {
        TODO("Step 1")
    }

    /** Step 3 — remove the file (unknown: do nothing). For each of its chunks `refCount - 1`; a chunk at 0 is removed. */
    fun delete(fileName: String) {
        TODO("Step 3")
    }

    /** Step 2 — total size of the distinct chunks currently stored (what the disk really holds). */
    fun storedBytes(): Long {
        TODO("Step 2")
    }

    /** Step 2 — total size of all files as users see them. */
    fun logicalBytes(): Long {
        TODO("Step 2")
    }
}
