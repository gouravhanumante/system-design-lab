import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GridIndexTest {

    @Test
    fun findsDriversWithinRadiusNearestFirst() {
        val g = GridIndex(10)
        g.update("A", 5, 5)
        g.update("B", 8, 5)
        g.update("C", 30, 30)
        assertEquals(listOf("A", "B"), g.withinRadius(5, 5, 5))
        assertEquals(3, g.withinRadius(5, 5, 100).size)
    }

    @Test
    fun boundaryIsInclusive() {
        val g = GridIndex(10)
        g.update("A", 3, 4)             // distance exactly 5 from the origin
        assertEquals(listOf("A"), g.withinRadius(0, 0, 5))
        assertEquals(emptyList(), g.withinRadius(0, 0, 4))
    }

    @Test
    fun movingADriverRemovesItFromTheOldCell() {
        val g = GridIndex(10)
        g.update("A", 5, 5)
        g.update("A", 95, 95)
        assertEquals(emptyList(), g.withinRadius(5, 5, 8))
        assertEquals(listOf("A"), g.withinRadius(95, 95, 1))
    }

    @Test
    fun removeForgetsTheDriver() {
        val g = GridIndex(10)
        g.update("A", 1, 1)
        g.remove("A")
        g.remove("never-existed")
        assertEquals(emptyList(), g.withinRadius(1, 1, 50))
    }

    @Test
    fun negativeCoordinatesWork() {
        val g = GridIndex(10)
        g.update("A", -1, -1)
        g.update("B", 1, 1)
        g.update("C", -25, -25)
        assertEquals(listOf("A", "B"), g.withinRadius(0, 0, 3))
        assertEquals(listOf("C"), g.withinRadius(-25, -25, 2))
    }

    @Test
    fun tiesAreOrderedById() {
        val g = GridIndex(10)
        g.update("Z", 1, 0)
        g.update("M", 0, 1)
        g.update("B", -1, 0)
        assertEquals(listOf("B", "M", "Z"), g.withinRadius(0, 0, 2))
    }

    @Test
    fun matchesBruteForceOnRandomData() {
        val rnd = Random(42)
        val g = GridIndex(7)
        val pos = HashMap<String, Pair<Int, Int>>()
        repeat(500) { i ->
            val id = "d$i"
            val p = rnd.nextInt(-100, 100) to rnd.nextInt(-100, 100)
            pos[id] = p; g.update(id, p.first, p.second)
        }
        repeat(100) { i ->                       // move some, remove some
            val id = "d$i"
            if (i % 3 == 0) { g.remove(id); pos.remove(id) }
            else { val p = rnd.nextInt(-100, 100) to rnd.nextInt(-100, 100); pos[id] = p; g.update(id, p.first, p.second) }
        }
        repeat(60) {
            val qx = rnd.nextInt(-120, 120); val qy = rnd.nextInt(-120, 120); val r = rnd.nextInt(0, 60)
            val expected = pos.entries
                .map { (id, p) -> id to (p.first - qx) * (p.first - qx) + (p.second - qy) * (p.second - qy) }
                .filter { it.second <= r * r }
                .sortedWith(compareBy({ it.second }, { it.first }))
                .map { it.first }
            assertEquals(expected, g.withinRadius(qx, qy, r))
        }
    }

    @Test
    fun smallQueriesDoNotScanEveryDriver() {
        val g = GridIndex(50)
        for (i in 0 until 100) for (j in 0 until 100) g.update("d$i-$j", i * 10, j * 10)   // 10,000 drivers
        val found = g.withinRadius(500, 500, 30)
        assertTrue(found.isNotEmpty())
        assertTrue(g.lastScanCount < 300, "scanned ${g.lastScanCount} drivers; a grid should only look at nearby cells")
        assertTrue(g.lastScanCount >= found.size)
    }
}
