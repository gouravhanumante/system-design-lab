import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TwoPhaseCommitTest {
    private val calls = ArrayList<String>()

    private inner class Bank(val name: String, var vote: Boolean = true, var commitFailures: Int = 0, var throwOnPrepare: Boolean = false) : Participant {
        override fun prepare(txId: String): Boolean {
            calls.add("$name.prepare($txId)")
            if (throwOnPrepare) throw RuntimeException("timeout")
            return vote
        }
        override fun commit(txId: String) {
            calls.add("$name.commit($txId)")
            if (commitFailures > 0) { commitFailures--; throw RuntimeException("network") }
        }
        override fun abort(txId: String) {
            calls.add("$name.abort($txId)")
        }
    }

    private fun setup(vararg banks: Bank): Pair<Coordinator, MutableList<String>> {
        val log = ArrayList<String>()
        return Coordinator(banks.associateBy { it.name }, log) to log
    }

    @Test
    fun everyoneVotesYesSoItCommits() {
        val (c, log) = setup(Bank("hdfc"), Bank("sbi"))
        assertEquals(Outcome.COMMITTED, c.execute("t1", listOf("hdfc", "sbi")))
        assertEquals(listOf("begin t1 hdfc,sbi", "decision t1 COMMIT", "done t1"), log)
        assertEquals(listOf("hdfc.prepare(t1)", "sbi.prepare(t1)", "hdfc.commit(t1)", "sbi.commit(t1)"), calls)
    }

    @Test
    fun oneNoVoteAbortsEveryoneAndStopsPreparing() {
        val (c, log) = setup(Bank("hdfc"), Bank("sbi", vote = false), Bank("icici"))
        assertEquals(Outcome.ABORTED, c.execute("t1", listOf("hdfc", "sbi", "icici")))
        assertEquals(listOf("begin t1 hdfc,sbi,icici", "decision t1 ABORT", "done t1"), log)
        assertEquals(
            listOf("hdfc.prepare(t1)", "sbi.prepare(t1)", "hdfc.abort(t1)", "sbi.abort(t1)", "icici.abort(t1)"),
            calls,
        )
    }

    @Test
    fun anExceptionDuringPrepareCountsAsNo() {
        val (c, _) = setup(Bank("hdfc", throwOnPrepare = true), Bank("sbi"))
        assertEquals(Outcome.ABORTED, c.execute("t1", listOf("hdfc", "sbi")))
    }

    @Test
    fun commitIsRetried() {
        val (c, log) = setup(Bank("hdfc", commitFailures = 2), Bank("sbi"))
        assertEquals(Outcome.COMMITTED, c.execute("t1", listOf("hdfc", "sbi")))
        assertEquals(3, calls.count { it == "hdfc.commit(t1)" })
        assertEquals("done t1", log.last())
    }

    @Test
    fun commitThatKeepsFailingIsInDoubtAndRecoverFinishesIt() {
        val sbi = Bank("sbi", commitFailures = 10)
        val (c, log) = setup(Bank("hdfc"), sbi)
        assertEquals(Outcome.IN_DOUBT, c.execute("t1", listOf("hdfc", "sbi")))
        assertEquals(listOf("begin t1 hdfc,sbi", "decision t1 COMMIT"), log)
        assertEquals(4, calls.count { it == "sbi.commit(t1)" })            // 1 + 3 retries
        sbi.commitFailures = 0
        calls.clear()
        assertEquals(mapOf("t1" to Outcome.COMMITTED), c.recover())
        assertEquals("done t1", log.last())
        assertEquals(listOf("hdfc.commit(t1)", "sbi.commit(t1)"), calls)  // commit is resent to everyone
    }

    @Test
    fun recoveryAfterACrashUsesOnlyTheLog() {
        val hdfc = Bank("hdfc"); val sbi = Bank("sbi")
        val log = mutableListOf(
            "begin t1 hdfc,sbi", "decision t1 COMMIT", "done t1",       // finished: untouched
            "begin t2 hdfc,sbi", "decision t2 COMMIT",                  // crashed after deciding
            "begin t3 sbi",                                             // crashed before deciding
            "begin t4 hdfc", "decision t4 ABORT",                       // crashed while aborting
        )
        val restarted = Coordinator(mapOf("hdfc" to hdfc, "sbi" to sbi), log)
        val result = restarted.recover()
        assertEquals(mapOf("t2" to Outcome.COMMITTED, "t3" to Outcome.ABORTED, "t4" to Outcome.ABORTED), result)
        assertEquals(listOf("hdfc.commit(t2)", "sbi.commit(t2)", "sbi.abort(t3)", "hdfc.abort(t4)"), calls)
        assertEquals(
            listOf("done t2", "decision t3 ABORT", "done t3", "done t4"),
            log.drop(8),
        )
        assertEquals(emptyMap(), restarted.recover())
    }

    @Test
    fun invalidCalls() {
        val (c, _) = setup(Bank("hdfc"))
        assertFailsWith<IllegalArgumentException> { c.execute("t1", emptyList()) }
        assertFailsWith<IllegalArgumentException> { c.execute("t1", listOf("hdfc", "ghost")) }
        c.execute("t1", listOf("hdfc"))
        assertFailsWith<IllegalArgumentException> { c.execute("t1", listOf("hdfc")) }
    }
}
