import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChunkedUploadTest {
    private val c0 = "AAAA".toByteArray()
    private val c1 = "BBBB".toByteArray()
    private val c2 = "CC".toByteArray()

    private fun UploadSessionOf3() = ChunkedUpload(3)

    @Test
    fun outOfOrderUploadAndAssemble() {
        val u = UploadSessionOf3()
        assertEquals(ReceiveResult.ACCEPTED, u.receive(2, c2, checksumOf(c2)))
        assertEquals(ReceiveResult.ACCEPTED, u.receive(0, c0, checksumOf(c0)))
        assertEquals(listOf(1), u.missingChunks())
        assertFalse(u.isComplete())
        assertEquals(ReceiveResult.ACCEPTED, u.receive(1, c1, checksumOf(c1)))
        assertTrue(u.isComplete())
        assertEquals(emptyList(), u.missingChunks())
        assertContentEquals("AAAABBBBCC".toByteArray(), u.assemble())
    }

    @Test
    fun missingChunksAreListedAscending() {
        val u = ChunkedUpload(5)
        u.receive(3, c0, checksumOf(c0))
        assertEquals(listOf(0, 1, 2, 4), u.missingChunks())
    }

    @Test
    fun duplicateKeepsTheFirstCopy() {
        val u = ChunkedUpload(1)
        assertEquals(ReceiveResult.ACCEPTED, u.receive(0, c0, checksumOf(c0)))
        assertEquals(ReceiveResult.DUPLICATE, u.receive(0, c1, checksumOf(c1)))
        assertContentEquals(c0, u.assemble())
    }

    @Test
    fun corruptChunkIsRejectedAndCanBeRetried() {
        val u = ChunkedUpload(1)
        assertEquals(ReceiveResult.CORRUPT, u.receive(0, c0, checksumOf(c0) + 1))
        assertFalse(u.isComplete())
        assertEquals(listOf(0), u.missingChunks())
        assertEquals(ReceiveResult.ACCEPTED, u.receive(0, c0, checksumOf(c0)))
        assertTrue(u.isComplete())
    }

    @Test
    fun outOfRangeIndexesAreRejected() {
        val u = UploadSessionOf3()
        assertEquals(ReceiveResult.OUT_OF_RANGE, u.receive(-1, c0, checksumOf(c0)))
        assertEquals(ReceiveResult.OUT_OF_RANGE, u.receive(3, c0, checksumOf(c0)))
        assertEquals(listOf(0, 1, 2), u.missingChunks())
    }

    @Test
    fun assembleBeforeCompleteThrows() {
        val u = UploadSessionOf3()
        u.receive(0, c0, checksumOf(c0))
        assertFailsWith<IllegalStateException> { u.assemble() }
    }

    @Test
    fun corruptCheckHappensBeforeDuplicateCheck() {
        val u = ChunkedUpload(1)
        u.receive(0, c0, checksumOf(c0))
        assertEquals(ReceiveResult.CORRUPT, u.receive(0, c1, 12345L))
    }

    @Test
    fun largeUploadInShuffledOrder() {
        val n = 500
        val chunks = List(n) { i -> ByteArray(7) { (i * 31 + it).toByte() } }
        val u = ChunkedUpload(n)
        (0 until n).shuffled(kotlin.random.Random(7)).forEach { i ->
            assertEquals(ReceiveResult.ACCEPTED, u.receive(i, chunks[i], checksumOf(chunks[i])))
        }
        assertTrue(u.isComplete())
        assertContentEquals(chunks.reduce { a, b -> a + b }, u.assemble())
    }

    @Test
    fun zeroChunkUploadIsTriviallyComplete() {
        val u = ChunkedUpload(0)
        assertTrue(u.isComplete())
        assertContentEquals(ByteArray(0), u.assemble())
    }
}
