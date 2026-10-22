import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DedupStoreTest {
    private fun b(s: String) = s.toByteArray(Charsets.ISO_8859_1)

    @Test
    fun roundTripsFileContents() {
        val s = DedupStore(4)
        s.put("f", b("hello world!"))
        assertContentEquals(b("hello world!"), s.get("f"))
    }

    @Test
    fun sharedChunksAreStoredOnce() {
        val s = DedupStore(4)
        s.put("f1", b("AAAABBBB"))
        s.put("f2", b("AAAACCCC"))
        assertEquals(12L, s.storedBytes())
        assertEquals(16L, s.logicalBytes())
    }

    @Test
    fun identicalFilesCostOneCopy() {
        val s = DedupStore(4)
        s.put("a", b("ABCDEFGH"))
        s.put("b", b("ABCDEFGH"))
        s.put("c", b("ABCDEFGH"))
        assertEquals(8L, s.storedBytes())
        assertEquals(24L, s.logicalBytes())
    }

    @Test
    fun shortFinalChunkCountsItsOwnSize() {
        val s = DedupStore(4)
        s.put("f", b("AAAAAA"))                   // AAAA + AA
        assertEquals(6L, s.storedBytes())
        s.put("g", b("AA"))                       // the short chunk AA is shared
        assertEquals(6L, s.storedBytes())
        assertEquals(8L, s.logicalBytes())
    }

    @Test
    fun repeatedChunkInsideOneFileIsStoredOnceAndReleasedCorrectly() {
        val s = DedupStore(4)
        s.put("f", b("AAAAAAAA"))
        assertEquals(4L, s.storedBytes())
        assertEquals(8L, s.logicalBytes())
        s.delete("f")
        assertEquals(0L, s.storedBytes())
        assertEquals(0L, s.logicalBytes())
    }

    @Test
    fun deleteOnlyFreesChunksNobodyElseUses() {
        val s = DedupStore(4)
        s.put("f1", b("AAAABBBB"))
        s.put("f2", b("AAAACCCC"))
        s.delete("f1")
        assertEquals(8L, s.storedBytes())          // AAAA (still used by f2) + CCCC
        assertContentEquals(b("AAAACCCC"), s.get("f2"))
        s.delete("f2")
        assertEquals(0L, s.storedBytes())
    }

    @Test
    fun overwritingReleasesTheOldChunks() {
        val s = DedupStore(4)
        s.put("f", b("AAAABBBB"))
        s.put("f", b("CCCC"))
        assertEquals(4L, s.storedBytes())
        assertEquals(4L, s.logicalBytes())
        assertContentEquals(b("CCCC"), s.get("f"))
    }

    @Test
    fun overwritingWithTheSameContentKeepsEverythingIntact() {
        val s = DedupStore(4)
        s.put("f", b("AAAABBBB"))
        s.put("f", b("AAAABBBB"))
        assertEquals(8L, s.storedBytes())
        s.delete("f")
        assertEquals(0L, s.storedBytes())
    }

    @Test
    fun emptyFilesAndUnknownNames() {
        val s = DedupStore(4)
        s.put("e", ByteArray(0))
        assertContentEquals(ByteArray(0), s.get("e"))
        assertEquals(0L, s.storedBytes())
        s.delete("never-existed")
        assertFailsWith<IllegalArgumentException> { s.get("never-existed") }
    }
}
