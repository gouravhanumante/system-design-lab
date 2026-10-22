import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileTreeTest {

    private fun sample(): FileTree {
        val t = FileTree()
        t.mkdir("/a"); t.createFile("/a/x.txt", 10); t.mkdir("/a/b"); t.createFile("/a/b/y.txt", 5)
        return t
    }

    @Test
    fun rootStartsEmpty() {
        val t = FileTree()
        assertTrue(t.exists("/"))
        assertEquals(emptyList(), t.list("/"))
        assertEquals(0L, t.totalSize("/"))
    }

    @Test
    fun listIsSortedAndMarksFolders() {
        val t = sample()
        t.createFile("/a/Z.txt", 1); t.createFile("/a/a.txt", 1)
        assertEquals(listOf("Z.txt", "a.txt", "b/", "x.txt"), t.list("/a"))
        assertEquals(listOf("a/"), t.list("/"))
    }

    @Test
    fun totalSizeIsRecursive() {
        val t = sample()
        assertEquals(15L, t.totalSize("/a"))
        assertEquals(5L, t.totalSize("/a/b"))
        assertEquals(10L, t.totalSize("/a/x.txt"))
        assertEquals(15L, t.totalSize("/"))
    }

    @Test
    fun existsDistinguishesPresentAndMissing() {
        val t = sample()
        assertTrue(t.exists("/a/b/y.txt"))
        assertFalse(t.exists("/a/nope"))
        assertFalse(t.exists("/nope/deeper"))
    }

    @Test
    fun cannotCreateWithoutParentOrTwice() {
        val t = sample()
        assertFailsWith<IllegalArgumentException> { t.mkdir("/ghost/child") }
        assertFailsWith<IllegalArgumentException> { t.createFile("/ghost/f", 1) }
        assertFailsWith<IllegalArgumentException> { t.mkdir("/a") }
        assertFailsWith<IllegalArgumentException> { t.createFile("/a/x.txt", 3) }
        assertFailsWith<IllegalArgumentException> { t.mkdir("/a/x.txt") }
    }

    @Test
    fun cannotCreateInsideAFile() {
        val t = sample()
        assertFailsWith<IllegalArgumentException> { t.mkdir("/a/x.txt/sub") }
        assertFailsWith<IllegalArgumentException> { t.createFile("/a/x.txt/f", 1) }
    }

    @Test
    fun listingAFileOrMissingPathFails() {
        val t = sample()
        assertFailsWith<IllegalArgumentException> { t.list("/a/x.txt") }
        assertFailsWith<IllegalArgumentException> { t.list("/missing") }
        assertFailsWith<IllegalArgumentException> { t.totalSize("/missing") }
    }

    @Test
    fun deepTreesWork() {
        val t = FileTree()
        var p = ""
        repeat(200) { p += "/d$it"; t.mkdir(p) }
        t.createFile("$p/leaf", 7)
        assertEquals(7L, t.totalSize("/d0"))
    }
}
