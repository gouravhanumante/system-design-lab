import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReconcileTest {

    @Test
    fun exactAndFuzzyMatches() {
        val r = reconcile(
            listOf(LedgerTxn("t1", 10_000, 1), LedgerTxn("t2", 25_000, 2)),
            listOf(BankLine("t1", 10_000, 2), BankLine("", 25_000, 4)),
        )
        assertEquals(listOf("t1" to "t1", "t2" to ""), r.matched)
        assertEquals(emptyList(), r.amountMismatch)
        assertEquals(emptyList(), r.missingInBank)
        assertEquals(emptyList(), r.missingInLedger)
    }

    @Test
    fun referenceMatchWithDifferentAmountIsAMismatch() {
        val r = reconcile(listOf(LedgerTxn("t1", 100_000, 1)), listOf(BankLine("t1", 98_000, 1)))
        assertEquals(listOf("t1" to "t1"), r.amountMismatch)
        assertEquals(emptyList(), r.matched)
        assertEquals(emptyList(), r.missingInLedger)
    }

    @Test
    fun outsideToleranceIsNotMatched() {
        val r = reconcile(listOf(LedgerTxn("t1", 500, 1)), listOf(BankLine("x", 500, 4)))
        assertEquals(listOf("t1"), r.missingInBank)
        assertEquals(listOf(BankLine("x", 500, 4)), r.missingInLedger)
        val loose = reconcile(listOf(LedgerTxn("t1", 500, 1)), listOf(BankLine("x", 500, 4)), dayTolerance = 3)
        assertEquals(listOf("t1" to "x"), loose.matched)
    }

    @Test
    fun fuzzyPicksTheClosestDayThenEarlierDayThenListPosition() {
        val r = reconcile(
            listOf(LedgerTxn("t1", 700, 5)),
            listOf(BankLine("far", 700, 7), BankLine("late", 700, 6), BankLine("early", 700, 4), BankLine("early2", 700, 4)),
        )
        assertEquals(listOf("t1" to "early"), r.matched)
        assertEquals(listOf("far", "late", "early2"), r.missingInLedger.map { it.ref })
    }

    @Test
    fun fuzzyProcessesLedgerByDayThenId() {
        val r = reconcile(
            listOf(LedgerTxn("b", 300, 3), LedgerTxn("a", 300, 3), LedgerTxn("z", 300, 1)),
            listOf(BankLine("x", 300, 2), BankLine("y", 300, 3)),
        )
        // z (day 1) goes first and takes x (day 2, diff 1); then a takes y; b is left over
        assertEquals(listOf("a" to "y", "z" to "x"), r.matched)
        assertEquals(listOf("b"), r.missingInBank)
    }

    @Test
    fun referenceMatchesWinBeforeFuzzyOnes() {
        val r = reconcile(
            listOf(LedgerTxn("t1", 900, 1), LedgerTxn("t2", 900, 1)),
            listOf(BankLine("t2", 900, 1), BankLine("t1", 900, 1)),
        )
        assertEquals(listOf("t1" to "t1", "t2" to "t2"), r.matched)
    }

    @Test
    fun eachBankLineIsUsedOnce() {
        val r = reconcile(
            listOf(LedgerTxn("t1", 100, 1), LedgerTxn("t2", 100, 1)),
            listOf(BankLine("", 100, 1)),
        )
        assertEquals(listOf("t1" to ""), r.matched)
        assertEquals(listOf("t2"), r.missingInBank)
    }

    @Test
    fun duplicateReferencesUseTheFirstUnusedLine() {
        val r = reconcile(
            listOf(LedgerTxn("t1", 100, 1)),
            listOf(BankLine("t1", 50, 1), BankLine("t1", 100, 1)),
        )
        assertEquals(listOf("t1" to "t1"), r.amountMismatch)
        assertEquals(listOf(BankLine("t1", 100, 1)), r.missingInLedger)
    }

    @Test
    fun reportListsAreSorted() {
        val r = reconcile(
            listOf(LedgerTxn("c", 1, 1), LedgerTxn("a", 2, 1), LedgerTxn("b", 3, 1), LedgerTxn("y", 9, 1), LedgerTxn("x", 8, 1)),
            listOf(BankLine("q", 7, 30), BankLine("c", 1, 1), BankLine("b", 3, 1), BankLine("a", 2, 1), BankLine("p", 6, 30)),
        )
        assertEquals(listOf("a" to "a", "b" to "b", "c" to "c"), r.matched)
        assertEquals(listOf("x", "y"), r.missingInBank)
        assertEquals(listOf("q", "p"), r.missingInLedger.map { it.ref })
    }

    @Test
    fun emptyInputsAndDuplicates() {
        assertEquals(Report(emptyList(), emptyList(), emptyList(), emptyList()), reconcile(emptyList(), emptyList()))
        assertFailsWith<IllegalArgumentException> { reconcile(listOf(LedgerTxn("t", 1, 1), LedgerTxn("t", 2, 1)), emptyList()) }
    }
}
