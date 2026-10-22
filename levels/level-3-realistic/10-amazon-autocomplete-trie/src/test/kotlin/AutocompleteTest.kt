import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AutocompleteTest {

    private fun sample(): Autocomplete {
        val a = Autocomplete()
        a.addQuery("iphone case", 5)
        a.addQuery("iphone charger", 3)
        a.addQuery("ipad", 4)
        return a
    }

    @Test
    fun suggestsByCountThenAlphabetically() {
        val a = sample()
        assertEquals(listOf("iphone case", "iphone charger"), a.suggest("iph"))
        assertEquals(listOf("iphone case", "ipad", "iphone charger"), a.suggest("ip"))
        assertEquals(emptyList(), a.suggest("samsung"))
    }

    @Test
    fun onlyVisitsThePrefixPath() {
        val a = sample()
        a.suggest("iph")
        assertEquals(4, a.lastNodesVisited)
        a.suggest("")
        assertEquals(1, a.lastNodesVisited)
        a.suggest("ixyz")
        assertTrue(a.lastNodesVisited <= 5)
    }

    @Test
    fun countsAccumulateAndReorder() {
        val a = sample()
        a.addQuery("iphone charger", 3)                      // now 6 > 5
        assertEquals(listOf("iphone charger", "iphone case"), a.suggest("iphone"))
    }

    @Test
    fun normalisation() {
        val a = Autocomplete()
        a.addQuery("  iPhone   Case ", 2)
        a.addQuery("iphone case")
        a.addQuery("   ")
        assertEquals(listOf("iphone case"), a.suggest("IPHONE C"))
        assertEquals(listOf("iphone case"), a.suggest(""))
        assertEquals(emptyList(), a.suggest(" iphone"))     // prefix is not trimmed
    }

    @Test
    fun limitAndTies() {
        val a = Autocomplete(maxSuggestions = 2)
        listOf("ab", "aa", "ac").forEach { a.addQuery(it) }
        assertEquals(listOf("aa", "ab"), a.suggest("a"))
    }

    @Test
    fun aQueryIsAlsoAPrefixOfLongerOnes() {
        val a = Autocomplete()
        a.addQuery("tv", 2); a.addQuery("tv stand", 3); a.addQuery("tv", 2)
        assertEquals(listOf("tv", "tv stand"), a.suggest("tv"))
        assertEquals(listOf("tv stand"), a.suggest("tv "))
    }

    @Test
    fun invalidCount() {
        assertFailsWith<IllegalArgumentException> { Autocomplete().addQuery("x", 0) }
    }

    @Test
    fun matchesBruteForceOnRandomData() {
        val a = Autocomplete(maxSuggestions = 4)
        val truth = HashMap<String, Long>()
        val rnd = Random(11)
        val words = listOf("shoe", "shoes", "shirt", "short", "shorts", "sofa", "soap", "sock", "socks", "s")
        repeat(3_000) {
            val q = words[rnd.nextInt(words.size)] + (if (rnd.nextInt(3) == 0) " ${rnd.nextInt(5)}" else "")
            val c = rnd.nextLong(1, 4)
            a.addQuery(q, c); truth.merge(q, c, Long::plus)
        }
        for (p in listOf("", "s", "sh", "sho", "shoe", "so", "soc", "socks ", "x")) {
            val expected = truth.entries.filter { it.key.startsWith(p) }
                .sortedWith(compareBy({ -it.value }, { it.key })).take(4).map { it.key }
            assertEquals(expected, a.suggest(p), "prefix '$p'")
            assertTrue(a.lastNodesVisited <= p.length + 1)
        }
    }
}
