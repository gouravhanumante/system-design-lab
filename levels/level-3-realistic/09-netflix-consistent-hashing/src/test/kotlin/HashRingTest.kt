import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HashRingTest {
    private val keys = (0 until 10_000).map { "title-$it" }

    private fun ring(vararg nodes: String, v: Int = 100): HashRing {
        val r = HashRing(v)
        nodes.forEach { r.addNode(it) }
        return r
    }

    @Test
    fun emptyRingHasNoOwner() {
        assertNull(HashRing(10).nodeFor("anything"))
    }

    @Test
    fun singleNodeOwnsEverything() {
        val r = ring("A")
        assertTrue(keys.all { r.nodeFor(it) == "A" })
    }

    @Test
    fun ownerIsTheFirstPointClockwise() {
        val r = ring("A", "B", "C", v = 3)
        val points = listOf("A", "B", "C").flatMap { n -> (0 until 3).map { ringPosition("$n#$it") to n } }.sortedBy { it.first }
        for (k in keys.take(500)) {
            val h = ringPosition(k)
            val expected = (points.firstOrNull { it.first >= h } ?: points.first()).second
            assertEquals(expected, r.nodeFor(k), "key $k")
        }
    }

    @Test
    fun removingANodeOnlyMovesItsOwnKeys() {
        val r = ring("A", "B", "C", "D")
        val before = keys.associateWith { r.nodeFor(it)!! }
        r.removeNode("C")
        for (k in keys) {
            if (before[k] != "C") assertEquals(before[k], r.nodeFor(k))
            else assertTrue(r.nodeFor(k) != "C")
        }
        assertEquals(setOf("A", "B", "D"), r.nodes())
    }

    @Test
    fun addingANodeOnlyTakesKeysForItself() {
        val r = ring("A", "B", "C", "D")
        val before = keys.associateWith { r.nodeFor(it)!! }
        r.addNode("E")
        var moved = 0
        for (k in keys) {
            val now = r.nodeFor(k)
            if (now != before[k]) { assertEquals("E", now); moved++ }
        }
        assertTrue(moved in 1_000..3_000, "moved $moved of 10000 (ideal is about 2000)")
        val modMoved = keys.count { Math.floorMod(it.hashCode(), 4) != Math.floorMod(it.hashCode(), 5) }
        assertTrue(modMoved > 7_000, "hash % N moves most keys: $modMoved")
    }

    @Test
    fun virtualNodesSpreadTheLoad() {
        val r = ring("A", "B", "C", v = 200)
        val counts = keys.groupingBy { r.nodeFor(it)!! }.eachCount()
        assertEquals(setOf("A", "B", "C"), counts.keys)
        assertTrue(counts.values.all { it in 2_300..4_400 }, "load $counts")
    }

    @Test
    fun addingTwiceAndRemovingUnknownAreHarmless() {
        val r = ring("A", "B")
        val before = keys.take(200).map { r.nodeFor(it) }
        r.addNode("A"); r.removeNode("Z")
        assertEquals(before, keys.take(200).map { r.nodeFor(it) })
        r.removeNode("A"); r.removeNode("B")
        assertNull(r.nodeFor("x"))
        assertEquals(emptySet(), r.nodes())
    }

    @Test
    fun invalidVirtualNodeCount() {
        assertFailsWith<IllegalArgumentException> { HashRing(0) }
    }
}
