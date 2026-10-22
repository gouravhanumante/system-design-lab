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

    var hits: Int = 0
        private set
    var misses: Int = 0
        private set

    fun get(id: String, now: Long): Product? {
        TODO()
    }

    fun update(p: Product, now: Long) {
        TODO()
    }

    /** Reloads every queued id (in queue order) and returns how many were refreshed. */
    fun runRefreshes(now: Long): Int {
        TODO()
    }
}
