import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class UsersTableTest {
    private val cities = listOf("Pune", "Delhi", "Mumbai", "Chennai", "Kolkata")

    private fun table(n: Int = 10_000): Pair<UsersTable, Map<Int, Pair<Int, String>>> {
        val t = UsersTable()
        val rnd = Random(8)
        val rows = HashMap<Int, Pair<Int, String>>()
        for (id in 1..n) {
            val age = rnd.nextInt(18, 81); val city = cities[rnd.nextInt(cities.size)]
            t.insert(id, age, city); rows[id] = age to city
        }
        return t to rows
    }

    @Test
    fun withoutAnIndexEveryRowIsExamined() {
        val (t, rows) = table()
        val result = t.findByAge(25, 26)
        assertEquals(rows.filter { it.value.first in 25..26 }.keys.sorted(), result)
        assertEquals(10_000, t.lastRowsExamined)
        t.findByCity("Pune")
        assertEquals(10_000, t.lastRowsExamined)
    }

    @Test
    fun sortedIndexOnlyTouchesMatchingRows() {
        val (t, rows) = table()
        t.createIndex("age")
        val result = t.findByAge(25, 26)
        assertEquals(rows.filter { it.value.first in 25..26 }.keys.sorted(), result)
        assertEquals(result.size, t.lastRowsExamined)
        assertTrue(t.lastRowsExamined < 600)
        t.findByCity("Pune")
        assertEquals(10_000, t.lastRowsExamined)                 // no city index yet
    }

    @Test
    fun hashIndexForEquality() {
        val (t, rows) = table()
        t.createIndex("city")
        val result = t.findByCity("Delhi")
        assertEquals(rows.filter { it.value.second == "Delhi" }.keys.sorted(), result)
        assertEquals(result.size, t.lastRowsExamined)
        assertEquals(emptyList(), t.findByCity("Atlantis"))
        assertEquals(0, t.lastRowsExamined)
    }

    @Test
    fun indexesStayCorrectAfterInsertsAndDeletes() {
        val t = UsersTable()
        t.createIndex("age"); t.createIndex("city")
        t.insert(1, 30, "Pune"); t.insert(2, 31, "Pune"); t.insert(3, 40, "Delhi")
        t.delete(2); t.delete(99)
        t.insert(4, 30, "Delhi")
        assertEquals(listOf(1, 4), t.findByAge(30, 31))
        assertEquals(listOf(1), t.findByCity("Pune"))
        assertEquals(listOf(3, 4), t.findByCity("Delhi"))
    }

    @Test
    fun rangeEdgesAreInclusiveAndEmptyRangesWork() {
        val t = UsersTable()
        t.insert(5, 20, "A"); t.insert(3, 25, "A"); t.insert(9, 30, "A")
        t.createIndex("age")
        assertEquals(listOf(3, 5, 9), t.findByAge(20, 30))
        assertEquals(emptyList(), t.findByAge(31, 40))
        assertEquals(emptyList(), t.findByAge(30, 20))
    }

    @Test
    fun invalidOperations() {
        val t = UsersTable()
        t.insert(1, 20, "A")
        assertFailsWith<IllegalArgumentException> { t.insert(1, 30, "B") }
        assertFailsWith<IllegalArgumentException> { t.createIndex("name") }
        t.createIndex("age"); t.createIndex("age")
        assertEquals(listOf(1), t.findByAge(0, 100))
    }
}
