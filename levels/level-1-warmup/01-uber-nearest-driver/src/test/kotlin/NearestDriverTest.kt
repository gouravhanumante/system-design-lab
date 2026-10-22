import kotlin.test.Test
import kotlin.test.assertEquals

class NearestDriverTest {
    private val drivers = listOf(
        Driver("A", 0, 0),
        Driver("B", 3, 4),
        Driver("C", 1, 1),
        Driver("D", -2, 0),
        Driver("E", 10, 10),
    )

    @Test
    fun returnsClosestFirst() {
        assertEquals(listOf("A", "C"), DriverFinder(drivers).nearest(0, 0, 2))
    }

    @Test
    fun tiesBrokenByIdAlphabetically() {
        val tied = listOf(Driver("Z", 1, 0), Driver("M", 0, 1), Driver("B", -1, 0), Driver("Q", 0, -1))
        assertEquals(listOf("B", "M", "Q", "Z"), DriverFinder(tied).nearest(0, 0, 4))
    }

    @Test
    fun kLargerThanDriverCountReturnsAll() {
        assertEquals(listOf("A", "C", "D", "B", "E"), DriverFinder(drivers).nearest(0, 0, 50))
    }

    @Test
    fun zeroKAndEmptyList() {
        assertEquals(emptyList(), DriverFinder(drivers).nearest(0, 0, 0))
        assertEquals(emptyList(), DriverFinder(emptyList()).nearest(5, 5, 3))
    }

    @Test
    fun worksWithNegativeCoordinates() {
        assertEquals(listOf("D"), DriverFinder(drivers).nearest(-3, 0, 1))
    }

    @Test
    fun doesNotModifyTheInput() {
        val input = drivers.toMutableList()
        val copy = input.toList()
        DriverFinder(input).nearest(9, 9, 3)
        assertEquals(copy, input)
    }
}
