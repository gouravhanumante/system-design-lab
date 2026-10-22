import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JobSchedulerTest {

    @Test
    fun chainRunsSequentially() {
        val s = JobScheduler(2)
        s.submit(listOf(Job("a", 10), Job("b", 5, listOf("a")), Job("c", 5, listOf("b"))))
        val r = s.run()
        assertEquals(mapOf("a" to 10L, "b" to 15L, "c" to 20L), r.finishTimes)
        assertEquals(20L, r.makespan)
        assertEquals(emptySet(), r.failed)
        assertEquals(emptySet(), r.skipped)
    }

    @Test
    fun workersLimitParallelism() {
        val s = JobScheduler(2)
        s.submit(listOf(Job("x", 10), Job("y", 10), Job("z", 10)))
        val r = s.run()
        assertEquals(mapOf("x" to 10L, "y" to 10L, "z" to 20L), r.finishTimes)
        assertEquals(20L, r.makespan)
    }

    @Test
    fun higherPriorityStartsFirstThenSmallerId() {
        val s = JobScheduler(1)
        s.submit(listOf(Job("low", 5, priority = 0), Job("high", 5, priority = 5), Job("mid", 5, priority = 2), Job("aaa", 5, priority = 2)))
        val r = s.run()
        assertEquals(5L, r.finishTimes["high"])
        assertEquals(10L, r.finishTimes["aaa"])
        assertEquals(15L, r.finishTimes["mid"])
        assertEquals(20L, r.finishTimes["low"])
    }

    @Test
    fun youtubePipeline() {
        val s = JobScheduler(2)
        s.submit(listOf(
            Job("upload", 10),
            Job("t360", 20, listOf("upload")),
            Job("t720", 30, listOf("upload")),
            Job("t1080", 40, listOf("upload")),
            Job("thumb", 5, listOf("t360")),
            Job("publish", 1, listOf("t360", "t720", "t1080", "thumb")),
        ))
        val r = s.run()
        assertEquals(10L, r.finishTimes["upload"])
        assertEquals(30L, r.finishTimes["t360"])
        assertEquals(50L, r.finishTimes["t1080"])
        assertEquals(55L, r.finishTimes["thumb"])
        assertEquals(60L, r.finishTimes["t720"])
        assertEquals(61L, r.finishTimes["publish"])
        assertEquals(61L, r.makespan)
    }

    @Test
    fun retriesUntilSuccess() {
        val s = JobScheduler(1, maxAttempts = 3, failures = { id, attempt -> id == "t" && attempt < 3 })
        s.submit(listOf(Job("t", 10)))
        val r = s.run()
        assertEquals(mapOf("t" to 30L), r.finishTimes)
        assertEquals(30L, r.makespan)
        assertEquals(emptySet(), r.failed)
    }

    @Test
    fun permanentFailureSkipsEverythingThatDependsOnIt() {
        val s = JobScheduler(2, maxAttempts = 2, failures = { id, _ -> id == "t" })
        s.submit(listOf(Job("t", 10), Job("u", 5, listOf("t")), Job("w", 5, listOf("u")), Job("z", 5)))
        val r = s.run()
        assertEquals(setOf("t"), r.failed)
        assertEquals(setOf("u", "w"), r.skipped)
        assertEquals(mapOf("z" to 5L), r.finishTimes)
        assertEquals(20L, r.makespan)
    }

    @Test
    fun aRetryCompetesWithOtherReadyJobsByPriority() {
        // one worker: "flaky" (prio 9) fails once; after it fails, it is ready again and goes before "other"
        val s = JobScheduler(1, maxAttempts = 2, failures = { id, attempt -> id == "flaky" && attempt == 1 })
        s.submit(listOf(Job("flaky", 4, priority = 9), Job("other", 3, priority = 1)))
        val r = s.run()
        assertEquals(8L, r.finishTimes["flaky"])
        assertEquals(11L, r.finishTimes["other"])
    }

    @Test
    fun invalidSubmissionsAreRejected() {
        assertFailsWith<IllegalArgumentException> { JobScheduler(1).submit(listOf(Job("a", 1), Job("a", 2))) }
        assertFailsWith<IllegalArgumentException> { JobScheduler(1).submit(listOf(Job("a", 1, listOf("ghost")))) }
        assertFailsWith<IllegalArgumentException> {
            JobScheduler(1).submit(listOf(Job("a", 1, listOf("b")), Job("b", 1, listOf("c")), Job("c", 1, listOf("a"))))
        }
        assertFailsWith<IllegalArgumentException> { JobScheduler(1).submit(listOf(Job("self", 1, listOf("self")))) }
    }

    @Test
    fun emptyScheduleHasZeroMakespan() {
        val s = JobScheduler(3)
        s.submit(emptyList())
        val r = s.run()
        assertEquals(0L, r.makespan)
        assertEquals(emptyMap(), r.finishTimes)
    }
}
