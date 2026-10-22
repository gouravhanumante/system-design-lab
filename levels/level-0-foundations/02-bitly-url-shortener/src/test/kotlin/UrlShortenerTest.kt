import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UrlShortenerTest {

    @Test
    fun base62Encoding() {
        assertEquals("0", encodeBase62(0))
        assertEquals("9", encodeBase62(9))
        assertEquals("a", encodeBase62(10))
        assertEquals("Z", encodeBase62(61))
        assertEquals("10", encodeBase62(62))
        assertEquals("zz", encodeBase62(35L * 62 + 35))
        assertFailsWith<IllegalArgumentException> { encodeBase62(-1) }
    }

    @Test
    fun base62RoundTrip() {
        for (n in listOf(0L, 1L, 61L, 62L, 3843L, 3844L, 56_800_235_583L, Long.MAX_VALUE)) assertEquals(n, decodeBase62(encodeBase62(n)))
        assertEquals(7, encodeBase62(3_500_000_000_000L).length)
        assertFailsWith<IllegalArgumentException> { decodeBase62("ab_c") }
        assertFailsWith<IllegalArgumentException> { decodeBase62("") }
    }

    @Test
    fun shortenAndResolve() {
        val s = UrlShortener()
        assertEquals("1", s.shorten("https://amazon.in/dp/123"))
        assertEquals("2", s.shorten("https://flipkart.com/x"))
        assertEquals("https://amazon.in/dp/123", s.resolve("1"))
        assertNull(s.resolve("zzz"))
    }

    @Test
    fun sameUrlGivesSameCode() {
        val s = UrlShortener()
        val a = s.shorten("https://a.com")
        s.shorten("https://b.com")
        assertEquals(a, s.shorten("https://a.com"))
        assertEquals("3", s.shorten("https://c.com"))
    }

    @Test
    fun customAliases() {
        val s = UrlShortener()
        assertTrue(s.customAlias("diwali-sale", "https://shop.com/diwali"))
        assertFalse(s.customAlias("diwali-sale", "https://other.com"))
        assertEquals("https://shop.com/diwali", s.resolve("diwali-sale"))
        assertFailsWith<IllegalArgumentException> { s.customAlias("ab", "https://x.com") }
        assertFailsWith<IllegalArgumentException> { s.customAlias("has space", "https://x.com") }
        assertFailsWith<IllegalArgumentException> { s.customAlias("ok-alias", "ftp://x.com") }
    }

    @Test
    fun generatedCodesSkipAliasesAndAliasesCannotStealCodes() {
        val s = UrlShortener()
        assertTrue(s.customAlias("100", "https://reserved.com"))       // "100" is what counter 3844 would produce
        assertTrue(s.customAlias("abc", "https://abc.com"))
        assertEquals("1", s.shorten("https://one.com"))
        var last = ""
        for (i in 2..3_844) last = s.shorten("https://site$i.com")
        assertEquals("https://reserved.com", s.resolve("100"))
        assertEquals("101", last)                                        // counter 3844 ("100") was skipped
        assertFalse(s.customAlias("101", "https://steal.com"))          // taken by a generated code
    }

    @Test
    fun aliasForAUrlDoesNotChangeShortenBehaviour() {
        val s = UrlShortener()
        s.customAlias("mine", "https://a.com")
        assertEquals("1", s.shorten("https://a.com"))
    }

    @Test
    fun clicksAreCounted() {
        val s = UrlShortener()
        val c = s.shorten("https://a.com")
        s.resolve(c); s.resolve(c)
        s.resolve("nope")
        assertEquals(2, s.clicks(c))
        assertEquals(0, s.clicks("nope"))
    }

    @Test
    fun invalidLongUrls() {
        val s = UrlShortener()
        assertFailsWith<IllegalArgumentException> { s.shorten("amazon.in") }
        assertFailsWith<IllegalArgumentException> { s.shorten("javascript:alert(1)") }
    }
}
