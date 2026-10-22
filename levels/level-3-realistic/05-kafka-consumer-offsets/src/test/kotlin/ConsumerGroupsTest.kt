import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConsumerGroupsTest {

    private fun topicWith(n: Int, partitions: Int = 1): Topic {
        val t = Topic(partitions)
        repeat(n) { t.produce(null, "m$it") }
        return t
    }

    private fun values(r: List<Record>) = r.map { it.value }

    @Test
    fun pollAdvancesThePosition() {
        val c = ConsumerGroups(topicWith(5))
        assertEquals(listOf("m0", "m1"), values(c.poll("g", 0, 2)))
        assertEquals(listOf("m2", "m3", "m4"), values(c.poll("g", 0, 10)))
        assertEquals(emptyList(), c.poll("g", 0, 10))
    }

    @Test
    fun commitIsSeparateFromPosition() {
        val c = ConsumerGroups(topicWith(5))
        c.poll("g", 0, 3)
        assertEquals(0L, c.committed("g", 0))
        c.commit("g", 0)
        assertEquals(3L, c.committed("g", 0))
    }

    @Test
    fun crashBeforeCommitCausesRedelivery() {
        val c = ConsumerGroups(topicWith(5))
        c.poll("g", 0, 3); c.commit("g", 0)
        assertEquals(listOf("m3", "m4"), values(c.poll("g", 0, 2)))
        c.restart("g")                                         // crashed before committing 3..4
        assertEquals(listOf("m3", "m4"), values(c.poll("g", 0, 10)))
    }

    @Test
    fun restartWithoutAnyCommitStartsFromTheBeginning() {
        val c = ConsumerGroups(topicWith(3))
        c.poll("g", 0, 3)
        c.restart("g")
        assertEquals(listOf("m0", "m1", "m2"), values(c.poll("g", 0, 10)))
    }

    @Test
    fun groupsAreIndependent() {
        val c = ConsumerGroups(topicWith(3))
        c.poll("fast", 0, 3); c.commit("fast", 0)
        assertEquals(listOf("m0", "m1", "m2"), values(c.poll("slow", 0, 10)))
        assertEquals(0L, c.committed("slow", 0))
        c.restart("fast")
        assertEquals(emptyList(), c.poll("fast", 0, 10))      // fast stays at its commit
    }

    @Test
    fun partitionsAreTrackedSeparately() {
        val t = topicWith(6, partitions = 2)                   // p0: m0 m2 m4   p1: m1 m3 m5
        val c = ConsumerGroups(t)
        assertEquals(listOf("m0", "m2"), values(c.poll("g", 0, 2)))
        assertEquals(listOf("m1", "m3", "m5"), values(c.poll("g", 1, 10)))
        c.commit("g", 1)
        c.restart("g")
        assertEquals(listOf("m0", "m2", "m4"), values(c.poll("g", 0, 10)))   // p0 uncommitted -> replay
        assertEquals(emptyList(), c.poll("g", 1, 10))                        // p1 committed at end
    }

    @Test
    fun lagIsDistanceFromTheEndOfTheLogToTheCommit() {
        val t = topicWith(10)
        val c = ConsumerGroups(t)
        assertEquals(10L, c.lag("g", 0))
        c.poll("g", 0, 4)
        assertEquals(10L, c.lag("g", 0))                        // reading is not committing
        c.commit("g", 0)
        assertEquals(6L, c.lag("g", 0))
        t.produce(null, "new")
        assertEquals(7L, c.lag("g", 0))
    }

    @Test
    fun seekAllowsReplayAndSkipping() {
        val c = ConsumerGroups(topicWith(5))
        c.seek("g", 0, 3)
        assertEquals(listOf("m3", "m4"), values(c.poll("g", 0, 10)))
        c.seek("g", 0, 0)
        assertEquals("m0", c.poll("g", 0, 1).single().value)
        c.seek("g", 0, 5)                                        // exactly the end is allowed
        assertEquals(emptyList(), c.poll("g", 0, 1))
        assertFailsWith<IllegalArgumentException> { c.seek("g", 0, 6) }
        assertFailsWith<IllegalArgumentException> { c.seek("g", 0, -1) }
    }
}
