import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InventoryTest {

    @Test
    fun holdReducesAvailability() {
        val inv = Inventory(600)
        inv.addStock("PS5", 3)
        assertTrue(inv.hold("A", "PS5", 2, 0))
        assertEquals(1, inv.available("PS5", 0))
        assertEquals(0, inv.available("UNKNOWN", 0))
    }

    @Test
    fun cannotOversell() {
        val inv = Inventory(600)
        inv.addStock("PS5", 1)
        assertTrue(inv.hold("A", "PS5", 1, 0))
        assertFalse(inv.hold("B", "PS5", 1, 1))
        assertEquals(0, inv.available("PS5", 1))
    }

    @Test
    fun holdsExpireExactlyOnTime() {
        val inv = Inventory(600)
        inv.addStock("PS5", 1)
        inv.hold("A", "PS5", 1, 0)
        assertEquals(0, inv.available("PS5", 599))
        assertEquals(1, inv.available("PS5", 600))
        assertTrue(inv.hold("B", "PS5", 1, 600))
        assertFalse(inv.confirm("A", 601))
        assertTrue(inv.confirm("B", 601))
        assertEquals(1, inv.sold("PS5"))
        assertEquals(0, inv.available("PS5", 10_000))
    }

    @Test
    fun confirmTurnsHoldIntoSaleAndIsIdempotent() {
        val inv = Inventory(600)
        inv.addStock("X", 5)
        inv.hold("A", "X", 2, 0)
        assertTrue(inv.confirm("A", 10))
        assertTrue(inv.confirm("A", 99_999))           // retry long after: still fine
        assertEquals(2, inv.sold("X"))
        assertEquals(3, inv.available("X", 99_999))      // a sale never expires
        assertFalse(inv.confirm("ghost", 1))
    }

    @Test
    fun releaseReturnsStock() {
        val inv = Inventory(600)
        inv.addStock("X", 1)
        inv.hold("A", "X", 1, 0)
        inv.release("A")
        assertEquals(1, inv.available("X", 1))
        assertFalse(inv.confirm("A", 2))
        inv.release("A"); inv.release("ghost")
    }

    @Test
    fun repeatedHoldIsIdempotentButConflictingHoldIsAnError() {
        val inv = Inventory(600)
        inv.addStock("X", 2)
        assertTrue(inv.hold("A", "X", 1, 0))
        assertTrue(inv.hold("A", "X", 1, 5))            // client retry
        assertEquals(1, inv.available("X", 5))           // still only one unit held
        assertFailsWith<IllegalArgumentException> { inv.hold("A", "X", 2, 6) }
        assertFailsWith<IllegalArgumentException> { inv.hold("A", "Y", 1, 6) }
        inv.confirm("A", 7)
        assertFailsWith<IllegalArgumentException> { inv.hold("A", "X", 1, 8) }
    }

    @Test
    fun anOrderCanHoldAgainAfterItsHoldExpiredOrWasReleased() {
        val inv = Inventory(100)
        inv.addStock("X", 1)
        inv.hold("A", "X", 1, 0)
        assertTrue(inv.hold("A", "X", 1, 100))          // old one expired: new hold
        assertEquals(0, inv.available("X", 150))
        inv.release("A")
        assertTrue(inv.hold("A", "X", 1, 160))
        assertTrue(inv.confirm("A", 259))
    }

    @Test
    fun stockCanBeAddedLaterAndInputsAreValidated() {
        val inv = Inventory(600)
        inv.addStock("X", 1)
        assertFalse(inv.hold("A", "X", 2, 0))
        inv.addStock("X", 1)
        assertTrue(inv.hold("A", "X", 2, 1))
        assertFailsWith<IllegalArgumentException> { inv.hold("B", "X", 0, 1) }
        assertFailsWith<IllegalArgumentException> { inv.addStock("X", 0) }
    }
}
