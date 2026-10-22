import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class KvStoreTest {

    @Test
    fun plainSetGetDelete() {
        val kv = KvStore()
        kv.set("a", "1")
        assertEquals("1", kv.get("a"))
        kv.delete("a"); kv.delete("missing")
        assertNull(kv.get("a"))
    }

    @Test
    fun rollbackDiscardsChanges() {
        val kv = KvStore()
        kv.set("a", "10")
        kv.begin()
        kv.set("a", "20"); kv.set("b", "5")
        assertEquals("20", kv.get("a"))
        assertTrue(kv.rollback())
        assertEquals("10", kv.get("a"))
        assertNull(kv.get("b"))
    }

    @Test
    fun commitKeepsChanges() {
        val kv = KvStore()
        kv.begin()
        kv.set("a", "1"); kv.delete("a"); kv.set("b", "2")
        assertTrue(kv.commit())
        assertNull(kv.get("a"))
        assertEquals("2", kv.get("b"))
        assertFalse(kv.rollback())
    }

    @Test
    fun nestedTransactions() {
        val kv = KvStore()
        kv.set("a", "10")
        kv.begin()
        kv.set("a", "20")
        kv.begin()
        kv.delete("a")
        assertNull(kv.get("a"))
        assertTrue(kv.rollback())
        assertEquals("20", kv.get("a"))
        assertTrue(kv.commit())
        assertEquals("20", kv.get("a"))
        assertFalse(kv.commit())
    }

    @Test
    fun innerCommitStillRollsBackWithTheOuter() {
        val kv = KvStore()
        kv.set("a", "1")
        kv.begin()
        kv.begin()
        kv.set("a", "2")
        kv.commit()                                // into the outer transaction only
        assertEquals("2", kv.get("a"))
        kv.rollback()
        assertEquals("1", kv.get("a"))
    }

    @Test
    fun deleteInsideATransactionHidesCommittedValueUntilRollback() {
        val kv = KvStore()
        kv.set("a", "1")
        kv.begin()
        kv.delete("a")
        assertNull(kv.get("a"))
        assertEquals(0, kv.count("1"))
        kv.rollback()
        assertEquals("1", kv.get("a"))
    }

    @Test
    fun countSeesTheCurrentView() {
        val kv = KvStore()
        kv.set("a", "x"); kv.set("b", "x"); kv.set("c", "y")
        assertEquals(2, kv.count("x"))
        kv.begin()
        kv.set("c", "x"); kv.delete("a")
        assertEquals(2, kv.count("x"))
        kv.begin()
        kv.set("d", "x")
        assertEquals(3, kv.count("x"))
        kv.rollback(); kv.rollback()
        assertEquals(2, kv.count("x"))
        assertEquals(1, kv.count("y"))
        assertEquals(0, kv.count("nothing"))
    }

    @Test
    fun noTransactionToCloseReturnsFalse() {
        val kv = KvStore()
        assertFalse(kv.commit())
        assertFalse(kv.rollback())
    }
}
