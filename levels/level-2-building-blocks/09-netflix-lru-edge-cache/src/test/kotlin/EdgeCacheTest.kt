import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EdgeCacheTest {
    private fun bytes(n: Int) = ByteArray(n) { it.toByte() }

    @Test
    fun hitsAndMissesAreCounted() {
        val c = EdgeCache(100)
        assertNull(c.get("x"))
        c.put("x", bytes(10))
        assertContentEquals(bytes(10), c.get("x"))
        assertEquals(1, c.hits); assertEquals(1, c.misses)
        assertEquals(10L, c.usedBytes)
    }

    @Test
    fun evictsTheLeastRecentlyUsed() {
        val c = EdgeCache(10)
        c.put("a", bytes(4)); c.put("b", bytes(4))
        c.get("a")
        c.put("c", bytes(4))
        assertNull(c.get("b"))
        assertEquals(1, c.evictions)
        assertEquals(8L, c.usedBytes)
        assertContentEquals(bytes(4), c.get("a"))
        assertContentEquals(bytes(4), c.get("c"))
    }

    @Test
    fun putAlsoCountsAsUse() {
        val c = EdgeCache(10)
        c.put("a", bytes(4)); c.put("b", bytes(4))
        c.put("a", bytes(4))                        // a refreshed -> b is now least recent
        c.put("c", bytes(4))
        assertNull(c.get("b"))
        assertEquals(4, c.get("a")?.size)
    }

    @Test
    fun oneBigItemCanEvictSeveralSmallOnes() {
        val c = EdgeCache(10)
        c.put("a", bytes(3)); c.put("b", bytes(3)); c.put("c", bytes(3))
        c.put("big", bytes(8))
        assertEquals(3, c.evictions)
        assertEquals(8L, c.usedBytes)
        assertNull(c.get("a")); assertNull(c.get("b")); assertNull(c.get("c"))
    }

    @Test
    fun replacingAKeyUpdatesTheSize() {
        val c = EdgeCache(10)
        c.put("a", bytes(6))
        c.put("a", bytes(2))
        assertEquals(2L, c.usedBytes)
        c.put("b", bytes(8))
        assertEquals(10L, c.usedBytes)
        assertEquals(0, c.evictions)
    }

    @Test
    fun valuesBiggerThanTheCacheAreNotStored() {
        val c = EdgeCache(10)
        c.put("a", bytes(5))
        c.put("huge", bytes(11))
        assertNull(c.get("huge"))
        assertEquals(5L, c.usedBytes)
        assertEquals(0, c.evictions)
        c.put("a", bytes(50))                       // replacing with a too-big value removes the old one
        assertNull(c.get("a"))
        assertEquals(0L, c.usedBytes)
        assertEquals(0, c.evictions)
    }

    @Test
    fun exactlyFullIsFine() {
        val c = EdgeCache(10)
        c.put("a", bytes(5)); c.put("b", bytes(5))
        assertEquals(10L, c.usedBytes)
        assertEquals(0, c.evictions)
    }

    @Test
    fun longRandomWorkloadStaysWithinBudget() {
        val c = EdgeCache(1_000)
        val rnd = kotlin.random.Random(5)
        repeat(20_000) {
            val k = "k${rnd.nextInt(300)}"
            if (rnd.nextBoolean()) c.put(k, ByteArray(rnd.nextInt(1, 120))) else c.get(k)
            check(c.usedBytes <= 1_000)
        }
        assertEquals(c.hits + c.misses, (c.hits + c.misses))
    }
}
