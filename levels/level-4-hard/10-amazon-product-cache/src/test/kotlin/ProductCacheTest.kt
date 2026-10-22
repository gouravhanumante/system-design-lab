import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProductCacheTest {

    private class FakeDb : ProductDb {
        val rows = HashMap<String, Product>()
        val log = ArrayList<String>()
        override fun load(id: String): Product? { log.add("load:$id"); return rows[id] }
        override fun save(p: Product) { log.add("save:${p.id}"); rows[p.id] = p }
    }

    private val phone = Product("p1", "Phone", 1_999_900)

    private fun setup(): Pair<FakeDb, ProductCache> {
        val db = FakeDb()
        db.rows["p1"] = phone
        return db to ProductCache(db, ttlSeconds = 60, staleSeconds = 30, negativeTtlSeconds = 10)
    }

    @Test
    fun firstReadMissesThenHits() {
        val (db, c) = setup()
        assertEquals(phone, c.get("p1", 0))
        assertEquals(phone, c.get("p1", 59))
        assertEquals(listOf("load:p1"), db.log)
        assertEquals(1, c.hits); assertEquals(1, c.misses)
    }

    @Test
    fun staleValueIsServedAndRefreshedInTheBackground() {
        val (db, c) = setup()
        c.get("p1", 0)
        db.rows["p1"] = phone.copy(pricePaise = 1_799_900)    // changed behind the cache's back
        assertEquals(phone, c.get("p1", 60))                   // stale but instant
        assertEquals(phone, c.get("p1", 70))
        assertEquals(1, db.log.size)                           // no synchronous load
        assertEquals(1, c.runRefreshes(71))                    // queued once only
        assertEquals(1_799_900, c.get("p1", 72)?.pricePaise)
        assertEquals(0, c.runRefreshes(73))
    }

    @Test
    fun tooOldIsAMiss() {
        val (db, c) = setup()
        c.get("p1", 0)
        c.get("p1", 90)                                        // 60 + 30: dead
        assertEquals(listOf("load:p1", "load:p1"), db.log)
        assertEquals(2, c.misses)
    }

    @Test
    fun updateWritesTheDatabaseFirstThenInvalidates() {
        val (db, c) = setup()
        c.get("p1", 0)
        val cheaper = phone.copy(pricePaise = 1_499_900)
        c.update(cheaper, 5)
        assertEquals(listOf("load:p1", "save:p1"), db.log)
        assertEquals(cheaper, c.get("p1", 6))
        assertEquals(listOf("load:p1", "save:p1", "load:p1"), db.log)
    }

    @Test
    fun updateCancelsAPendingRefresh() {
        val (db, c) = setup()
        c.get("p1", 0)
        c.get("p1", 65)                                        // queues a refresh
        c.update(phone.copy(name = "Phone 2"), 66)
        assertEquals(0, c.runRefreshes(67))
    }

    @Test
    fun missingProductsAreNegativelyCached() {
        val (db, c) = setup()
        assertNull(c.get("ghost", 0))
        assertNull(c.get("ghost", 9))
        assertEquals(1, db.log.count { it == "load:ghost" })
        assertNull(c.get("ghost", 10))                         // negative TTL over, no stale period
        assertEquals(2, db.log.count { it == "load:ghost" })
    }

    @Test
    fun aProductCreatedLaterAppearsAfterTheNegativeTtl() {
        val (db, c) = setup()
        assertNull(c.get("new", 0))
        db.rows["new"] = Product("new", "Gadget", 100)
        assertNull(c.get("new", 5))
        assertEquals("Gadget", c.get("new", 10)?.name)
    }

    @Test
    fun refreshesRunInQueueOrderAndCanStoreDeletions() {
        val (db, c) = setup()
        db.rows["p2"] = Product("p2", "Case", 50_000)
        c.get("p2", 0); c.get("p1", 1)
        c.get("p2", 61); c.get("p1", 62)
        db.rows.remove("p2")
        db.log.clear()
        assertEquals(2, c.runRefreshes(63))
        assertEquals(listOf("load:p2", "load:p1"), db.log)
        assertNull(c.get("p2", 64))                            // refreshed as "does not exist"
        assertEquals(phone, c.get("p1", 64))
    }
}
