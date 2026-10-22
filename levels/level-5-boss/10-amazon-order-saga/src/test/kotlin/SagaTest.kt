import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SagaTest {
    private val calls = ArrayList<String>()

    /** A step whose action fails the first [actionFailures] times and whose compensation fails [undoFailures] times. */
    private fun step(name: String, actionFailures: Int = 0, undoFailures: Int = 0): Step {
        var a = 0; var u = 0
        return Step(
            name,
            action = { calls.add("$name.action"); if (a++ < actionFailures) throw RuntimeException("$name failed") },
            compensate = { calls.add("$name.undo"); if (u++ < undoFailures) throw RuntimeException("$name undo failed") },
        )
    }

    @Test
    fun allStepsSucceed() {
        val r = SagaOrchestrator().run("o1", listOf(step("reserve"), step("charge"), step("ship")))
        assertEquals(SagaStatus.COMPLETED, r.status)
        assertEquals(listOf("do:reserve", "ok:reserve", "do:charge", "ok:charge", "do:ship", "ok:ship"), r.log)
        assertEquals(listOf("reserve.action", "charge.action", "ship.action"), calls)
    }

    @Test
    fun transientFailureIsRetried() {
        val r = SagaOrchestrator(maxRetries = 2).run("o1", listOf(step("reserve"), step("charge", actionFailures = 2)))
        assertEquals(SagaStatus.COMPLETED, r.status)
        assertEquals(listOf("do:reserve", "ok:reserve", "do:charge", "retry:charge#1", "retry:charge#2", "ok:charge"), r.log)
    }

    @Test
    fun permanentFailureCompensatesCompletedStepsInReverse() {
        val r = SagaOrchestrator(maxRetries = 1).run("o1", listOf(step("reserve"), step("charge"), step("ship", actionFailures = 99)))
        assertEquals(SagaStatus.COMPENSATED, r.status)
        assertEquals(
            listOf("do:reserve", "ok:reserve", "do:charge", "ok:charge", "do:ship", "retry:ship#1", "fail:ship",
                "undo:charge", "undone:charge", "undo:reserve", "undone:reserve"),
            r.log,
        )
        assertEquals(listOf("reserve.action", "charge.action", "ship.action", "ship.action", "charge.undo", "reserve.undo"), calls)
    }

    @Test
    fun firstStepFailingNeedsNoCompensation() {
        val r = SagaOrchestrator(maxRetries = 0).run("o1", listOf(step("reserve", actionFailures = 1), step("charge")))
        assertEquals(SagaStatus.COMPENSATED, r.status)
        assertEquals(listOf("do:reserve", "fail:reserve"), r.log)
    }

    @Test
    fun compensationIsRetriedToo() {
        val r = SagaOrchestrator(maxRetries = 2).run("o1", listOf(step("reserve", undoFailures = 2), step("charge", actionFailures = 9)))
        assertEquals(SagaStatus.COMPENSATED, r.status)
        assertEquals(
            listOf("do:reserve", "ok:reserve", "do:charge", "retry:charge#1", "retry:charge#2", "fail:charge",
                "undo:reserve", "retry-undo:reserve#1", "retry-undo:reserve#2", "undone:reserve"),
            r.log,
        )
    }

    @Test
    fun stuckCompensationStopsAndFails() {
        val r = SagaOrchestrator(maxRetries = 0).run(
            "o1", listOf(step("reserve"), step("charge", undoFailures = 5), step("ship", actionFailures = 5)),
        )
        assertEquals(SagaStatus.FAILED, r.status)
        assertEquals(listOf("do:reserve", "ok:reserve", "do:charge", "ok:charge", "do:ship", "fail:ship", "undo:charge", "stuck:charge"), r.log)
        assertEquals(false, calls.contains("reserve.undo"))      // left for a human
    }

    @Test
    fun sameSagaIdIsNotExecutedTwice() {
        val o = SagaOrchestrator()
        val first = o.run("o1", listOf(step("reserve"), step("charge")))
        calls.clear()
        val again = o.run("o1", listOf(step("reserve"), step("charge")))
        assertEquals(first, again)
        assertEquals(emptyList(), calls)
        val other = o.run("o2", listOf(step("x")))
        assertEquals(SagaStatus.COMPLETED, other.status)
    }

    @Test
    fun emptySagaCompletes() {
        assertEquals(SagaResult(SagaStatus.COMPLETED, emptyList()), SagaOrchestrator().run("empty", emptyList()))
    }

    @Test
    fun negativeRetriesRejected() {
        assertFailsWith<IllegalArgumentException> { SagaOrchestrator(-1) }
    }
}
