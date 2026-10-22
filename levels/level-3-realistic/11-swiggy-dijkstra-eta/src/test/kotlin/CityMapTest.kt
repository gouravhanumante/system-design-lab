import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class CityMapTest {

    private fun triangle(): CityMap {
        val m = CityMap()
        m.addRoad("A", "B", 5); m.addRoad("B", "C", 5); m.addRoad("A", "C", 15)
        return m
    }

    @Test
    fun picksTheFastestNotTheShortestHopCount() {
        assertEquals(Route(10, listOf("A", "B", "C")), triangle().fastestRoute("A", "C"))
        assertEquals(Route(10, listOf("C", "B", "A")), triangle().fastestRoute("C", "A"))
    }

    @Test
    fun closuresAndReopening() {
        val m = triangle()
        m.closeRoad("C", "B")
        assertEquals(Route(15, listOf("A", "C")), m.fastestRoute("A", "C"))
        m.reopenRoad("B", "C")
        assertEquals(Route(10, listOf("A", "B", "C")), m.fastestRoute("A", "C"))
        m.closeRoad("X", "Y")
    }

    @Test
    fun oneWayStreets() {
        val m = CityMap()
        m.addRoad("A", "B", 1, oneWay = true)
        m.addRoad("B", "C", 1, oneWay = true)
        m.addRoad("C", "A", 10, oneWay = true)
        assertEquals(Route(2, listOf("A", "B", "C")), m.fastestRoute("A", "C"))
        assertEquals(Route(10, listOf("C", "A")), m.fastestRoute("C", "A"))
        assertEquals(Route(11, listOf("B", "C", "A")), m.fastestRoute("B", "A"))
    }

    @Test
    fun addingARoadAgainReplacesItsTime() {
        val m = triangle()
        m.addRoad("A", "C", 3)
        assertEquals(Route(3, listOf("A", "C")), m.fastestRoute("A", "C"))
    }

    @Test
    fun tiesPickTheLexicographicallySmallestPath() {
        val m = CityMap()
        m.addRoad("S", "Z", 1); m.addRoad("Z", "T", 1)
        m.addRoad("S", "M", 1); m.addRoad("M", "T", 1)
        m.addRoad("S", "T", 2)
        assertEquals(Route(2, listOf("S", "M", "T")), m.fastestRoute("S", "T"))
    }

    @Test
    fun unreachableAndTrivialRoutes() {
        val m = triangle()
        m.addRoad("X", "Y", 1)
        assertNull(m.fastestRoute("A", "X"))
        assertNull(m.fastestRoute("A", "nowhere"))
        assertEquals(Route(0, listOf("A")), m.fastestRoute("A", "A"))
    }

    @Test
    fun invalidRoads() {
        val m = CityMap()
        assertFailsWith<IllegalArgumentException> { m.addRoad("A", "B", 0) }
        assertFailsWith<IllegalArgumentException> { m.addRoad("A", "A", 3) }
    }

    @Test
    fun gridCityMatchesManhattanDistance() {
        val m = CityMap()
        val n = 15
        for (r in 0 until n) for (c in 0 until n) {
            if (c + 1 < n) m.addRoad("$r,$c", "$r,${c + 1}", 2)
            if (r + 1 < n) m.addRoad("$r,$c", "${r + 1},$c", 3)
        }
        val route = m.fastestRoute("0,0", "14,14")!!
        assertEquals(14 * 2 + 14 * 3, route.minutes)
        assertEquals(29, route.path.size)
    }

    @Test
    fun matchesBruteForceTimesOnRandomGraphs() {
        val rnd = Random(4)
        repeat(20) {
            val m = CityMap()
            val nodes = (0 until 8).map { "n$it" }
            val dist = Array(8) { i -> IntArray(8) { j -> if (i == j) 0 else 1_000_000 } }
            repeat(14) {
                val a = rnd.nextInt(8); val b = rnd.nextInt(8)
                if (a != b) {
                    val w = rnd.nextInt(1, 20)
                    m.addRoad(nodes[a], nodes[b], w, oneWay = true)
                    dist[a][b] = w
                }
            }
            for (k in 0 until 8) for (i in 0 until 8) for (j in 0 until 8)
                if (dist[i][k] + dist[k][j] < dist[i][j]) dist[i][j] = dist[i][k] + dist[k][j]
            for (i in 0 until 8) for (j in 0 until 8) {
                val r = m.fastestRoute(nodes[i], nodes[j])
                if (dist[i][j] >= 1_000_000) assertNull(if (i == j) null else r)
                else assertEquals(dist[i][j], r!!.minutes)
            }
        }
    }
}
