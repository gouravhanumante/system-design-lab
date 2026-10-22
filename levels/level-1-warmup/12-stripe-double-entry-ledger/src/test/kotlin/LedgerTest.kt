import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LedgerTest {

    private fun ledger(): Ledger {
        val l = Ledger()
        l.openAccount("world", allowNegative = true)
        l.openAccount("alice"); l.openAccount("bob")
        return l
    }

    @Test
    fun moneyMovesWithTwoEntries() {
        val l = ledger()
        assertTrue(l.transfer("t1", "world", "alice", 500))
        assertEquals(500L, l.balance("alice"))
        assertEquals(-500L, l.balance("world"))
        assertEquals(listOf("+500 t1"), l.entries("alice"))
        assertEquals(listOf("-500 t1"), l.entries("world"))
        assertEquals(0L, l.sumOfBalances())
    }

    @Test
    fun insufficientFundsIsRefusedWithoutEntries() {
        val l = ledger()
        l.transfer("t1", "world", "alice", 500)
        assertFalse(l.transfer("t2", "alice", "bob", 800))
        assertEquals(500L, l.balance("alice"))
        assertEquals(emptyList(), l.entries("bob"))
        assertTrue(l.transfer("t3", "alice", "bob", 500))      // exactly the balance is fine
        assertEquals(0L, l.balance("alice"))
    }

    @Test
    fun retriesReturnTheOriginalResult() {
        val l = ledger()
        assertTrue(l.transfer("t1", "world", "alice", 500))
        assertTrue(l.transfer("t1", "world", "alice", 500))
        assertEquals(500L, l.balance("alice"))
        assertFalse(l.transfer("t2", "bob", "alice", 100))
        l.transfer("t3", "world", "bob", 1_000)
        assertFalse(l.transfer("t2", "bob", "alice", 100))    // still the first answer
        assertEquals(1, l.entries("alice").size)
    }

    @Test
    fun invalidTransfersThrowAndRecordNothing() {
        val l = ledger()
        assertFailsWith<IllegalArgumentException> { l.transfer("x1", "world", "alice", 0) }
        assertFailsWith<IllegalArgumentException> { l.transfer("x2", "world", "ghost", 10) }
        assertFailsWith<IllegalArgumentException> { l.transfer("x3", "alice", "alice", 10) }
        assertTrue(l.transfer("x1", "world", "alice", 10))     // the id was not burned by the exception
        assertFailsWith<IllegalArgumentException> { l.openAccount("alice") }
        assertFailsWith<IllegalArgumentException> { l.balance("ghost") }
    }

    @Test
    fun sumIsAlwaysZero() {
        val l = ledger()
        val names = listOf("alice", "bob", "carol", "dan")
        l.openAccount("carol"); l.openAccount("dan")
        val rnd = Random(2)
        repeat(2_000) { i ->
            val from = if (rnd.nextInt(5) == 0) "world" else names[rnd.nextInt(4)]
            val to = names.filter { it != from }[rnd.nextInt(if (from == "world") 4 else 3)]
            l.transfer("t$i", from, to, rnd.nextLong(1, 500))
            assertEquals(0L, l.sumOfBalances())
        }
        for (n in names) assertTrue(l.balance(n) >= 0)
    }
}
