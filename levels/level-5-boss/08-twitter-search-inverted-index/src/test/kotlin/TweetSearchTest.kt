import kotlin.test.Test
import kotlin.test.assertEquals

class TweetSearchTest {

    private fun sample(): TweetSearch {
        val s = TweetSearch()
        s.index(1, "world cup final tonight")
        s.index(2, "the cup of the world")
        s.index(3, "World Cup spoiler: ARG wins")
        s.index(4, "Final: ARG 3-3 FRA! #WorldCup")
        return s
    }

    @Test
    fun termsAreAndedAndNewestComesFirst() {
        val s = sample()
        assertEquals(listOf(3L, 2L, 1L), s.search("world cup", 10))
        assertEquals(listOf(4L, 1L), s.search("final", 10))
        assertEquals(listOf(4L, 3L), s.search("arg", 10))
        assertEquals(emptyList(), s.search("world basketball", 10))
    }

    @Test
    fun tokenizingIsCaseInsensitiveAndKeepsHashtagsAndMentions() {
        val s = sample()
        s.index(5, "Thanks @FIFA for #WorldCup!!")
        assertEquals(listOf(5L, 4L), s.search("#worldcup", 10))
        assertEquals(listOf(5L), s.search("@fifa", 10))
        assertEquals(listOf(4L), s.search("FRA", 10))
        assertEquals(listOf(4L), s.search("3", 10))
    }

    @Test
    fun phrasesNeedConsecutiveTokensInOrder() {
        val s = sample()
        assertEquals(listOf(3L, 1L), s.search("\"world cup\"", 10))
        assertEquals(emptyList(), s.search("\"cup world\"", 10))
        assertEquals(listOf(4L), s.search("\"3 3 fra\"", 10))
        assertEquals(listOf(2L), s.search("\"the world\"", 10))
    }

    @Test
    fun phraseCanMatchLaterOccurrence() {
        val s = TweetSearch()
        s.index(1, "cup and then world cup")
        assertEquals(listOf(1L), s.search("\"world cup\"", 10))
    }

    @Test
    fun exclusionsRemoveMatches() {
        val s = sample()
        assertEquals(listOf(1L), s.search("\"world cup\" -spoiler", 10))
        assertEquals(listOf(2L, 1L), s.search("cup -arg", 10))
        assertEquals(emptyList(), s.search("-spoiler", 10))       // nothing required -> nothing returned
        assertEquals(emptyList(), s.search("", 10))
    }

    @Test
    fun mixedQueryWithPhraseTermsAndExclusion() {
        val s = sample()
        s.index(10, "world cup final was the best final")
        assertEquals(listOf(10L, 1L), s.search("final \"world cup\" -spoiler", 10))
        assertEquals(listOf(10L), s.search("\"best final\" world", 10))
    }

    @Test
    fun limitApplies() {
        val s = sample()
        assertEquals(listOf(3L), s.search("cup", 1))
        assertEquals(emptyList(), s.search("cup", 0))
    }

    @Test
    fun deleteAndReplace() {
        val s = sample()
        s.delete(3)
        assertEquals(listOf(2L, 1L), s.search("world cup", 10))
        s.delete(999)
        s.index(1, "totally different text")
        assertEquals(listOf(2L), s.search("world cup", 10))
        assertEquals(listOf(1L), s.search("different", 10))
        assertEquals(emptyList(), s.search("tonight", 10))
    }

    @Test
    fun manyTweets() {
        val s = TweetSearch()
        for (i in 1..2_000L) s.index(i, if (i % 10 == 0L) "breaking news number $i" else "just another tweet $i")
        val r = s.search("\"breaking news\"", 5)
        assertEquals(listOf(2000L, 1990L, 1980L, 1970L, 1960L), r)
        assertEquals(listOf(1500L), s.search("breaking 1500", 5))
    }
}
