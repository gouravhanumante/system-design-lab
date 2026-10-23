class EdgeCache(private val capacityBytes: Long) {

    /**
     * key → value, ordered from LEAST recently used to MOST recently used.
     * The `true` (accessOrder) makes LinkedHashMap move a key to the end every time you `get` or `put` it — LRU for free.
     */
    private val entries = LinkedHashMap<String, ByteArray>(16, 0.75f, true)

    var usedBytes: Long = 0
        private set
    var hits: Int = 0
        private set
    var misses: Int = 0
        private set
    var evictions: Int = 0
        private set

    /** Step 1 — the value (counts a hit, and makes it most recently used) or `null` (counts a miss). */
    fun get(key: String): ByteArray? {
        TODO("Step 1")
    }

    /**
     * Step 2 — store [value] under [key] as most recently used.
     * - If the key exists, remove the old value first (and subtract its size).
     * - A value bigger than the whole cache is not stored at all.
     * - Add its size to `usedBytes`, then [evictUntilFits].
     */
    fun put(key: String, value: ByteArray) {
        TODO("Step 2")
    }

    /**
     * Step 2 — while `usedBytes > capacityBytes`, remove the least recently used entry (the FIRST in `entries`),
     * but never [keep] (the item just added). Each removal: `evictions++`. Exactly full is fine.
     * Tip: use `entries.entries.iterator()` and `it.remove()`.
     */
    private fun evictUntilFits(keep: String) {
        TODO("Step 2")
    }
}
