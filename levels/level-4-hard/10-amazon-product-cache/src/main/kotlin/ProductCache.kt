data class Product(val id: String, val name: String, val pricePaise: Long)

/** Provided: the database. */
interface ProductDb {
    fun load(id: String): Product?
    fun save(p: Product)
}

class ProductCache(
    private val db: ProductDb,
    private val ttlSeconds: Long = 60,
    private val staleSeconds: Long = 30,
    private val negativeTtlSeconds: Long = 10,
) {

    // ───────────── State ─────────────

    /** A cached DB result. [product] is `null` when the DB said "no such product" (negative caching). */
    private class CacheEntry(val product: Product?, val loadedAt: Long)

    /** product id → cached entry. */
    private val cache = HashMap<String, CacheEntry>()

    /** Ids waiting for a background refresh, in the order they were queued (no duplicates). */
    private val refreshQueue = LinkedHashSet<String>()

    var hits: Int = 0
        private set
    var misses: Int = 0
        private set

    // ───────────── API ─────────────

    /**
     * Steps 1–3 — read a product. age = `now - loadedAt`.
     * - Cached "not found" (step 3) with `age < negativeTtlSeconds` → hit, return `null`.
     * - Cached product with `age < ttlSeconds` → fresh hit.
     * - Cached product with `age < ttlSeconds + staleSeconds` (step 2) → still a hit, return the old value,
     *   AND add the id to the refresh queue.
     * - Anything else (not cached, or too old) → miss: `db.load(id)`, cache the result (even `null`) with loadedAt = now, return it.
     */
    fun get(id: String, now: Long): Product? {
        TODO("Step 1")
    }

    /** Step 4 — write to the DB FIRST, then delete the cache entry and cancel any pending refresh for that id. */
    fun update(p: Product, now: Long) {
        TODO("Step 4")
    }

    /** Step 2 — reload every queued id from the DB (in queue order), cache the result with loadedAt = now, clear the queue, return how many. */
    fun runRefreshes(now: Long): Int {
        TODO("Step 2")
    }
}
