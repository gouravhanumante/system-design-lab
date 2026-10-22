import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ServiceZonesTest {
    private val square = listOf(0 to 0, 10 to 0, 10 to 10, 0 to 10)

    @Test
    fun insideOutsideAndBorder() {
        val z = ServiceZones()
        z.addZone("HSR", square)
        assertEquals("HSR", z.zoneFor(5, 5))
        assertEquals("HSR", z.zoneFor(10, 3))      // on an edge
        assertEquals("HSR", z.zoneFor(0, 0))       // on a corner
        assertEquals("HSR", z.zoneFor(5, 10))
        assertNull(z.zoneFor(11, 5))
        assertNull(z.zoneFor(-1, -1))
    }

    @Test
    fun concavePolygonNotchIsOutside() {
        // An "L": the top-right quarter is missing
        val l = listOf(0 to 0, 10 to 0, 10 to 5, 5 to 5, 5 to 10, 0 to 10)
        val z = ServiceZones()
        z.addZone("L", l)
        assertEquals("L", z.zoneFor(2, 8))
        assertEquals("L", z.zoneFor(8, 2))
        assertNull(z.zoneFor(8, 8))               // in the notch
        assertEquals("L", z.zoneFor(7, 5))         // on the inner edge
        assertEquals("L", z.zoneFor(5, 5))         // inner corner
    }

    @Test
    fun triangleAndDiagonalEdge() {
        val z = ServiceZones()
        z.addZone("T", listOf(0 to 0, 10 to 0, 0 to 10))
        assertEquals("T", z.zoneFor(5, 5))         // exactly on the diagonal
        assertEquals("T", z.zoneFor(2, 2))
        assertNull(z.zoneFor(6, 6))
    }

    @Test
    fun rayPassingThroughAVertexIsCountedCorrectly() {
        val diamond = listOf(5 to 0, 10 to 5, 5 to 10, 0 to 5)
        val z = ServiceZones()
        z.addZone("D", diamond)
        assertEquals("D", z.zoneFor(1, 5))         // ray to the right goes through the corner (10, 5)
        assertEquals("D", z.zoneFor(5, 5))
        assertNull(z.zoneFor(-3, 5))               // left of the diamond, same row as two corners
        assertNull(z.zoneFor(9, 1))
    }

    @Test
    fun priorityThenIdDecideOverlaps() {
        val z = ServiceZones()
        z.addZone("big", listOf(0 to 0, 100 to 0, 100 to 100, 0 to 100), priority = 1)
        z.addZone("hub-b", square, priority = 5)
        z.addZone("hub-a", square, priority = 5)
        assertEquals("hub-a", z.zoneFor(5, 5))
        assertEquals("big", z.zoneFor(50, 50))
    }

    @Test
    fun boundingBoxSkipsFarZones() {
        val z = ServiceZones()
        for (i in 0 until 50) z.addZone("z$i", listOf(i * 100 to 0, i * 100 + 10 to 0, i * 100 + 10 to 10, i * 100 to 10))
        assertEquals("z7", z.zoneFor(705, 5))
        assertEquals(1, z.lastFullChecks)
        assertNull(z.zoneFor(5_000, 5_000))
        assertEquals(0, z.lastFullChecks)
    }

    @Test
    fun boxContainsPointButPolygonDoesNot() {
        val z = ServiceZones()
        z.addZone("T", listOf(0 to 0, 10 to 0, 0 to 10))
        assertNull(z.zoneFor(9, 9))
        assertEquals(1, z.lastFullChecks)
    }

    @Test
    fun invalidZones() {
        val z = ServiceZones()
        assertFailsWith<IllegalArgumentException> { z.addZone("x", listOf(0 to 0, 1 to 1)) }
        z.addZone("ok", square)
        assertFailsWith<IllegalArgumentException> { z.addZone("ok", square) }
    }
}
