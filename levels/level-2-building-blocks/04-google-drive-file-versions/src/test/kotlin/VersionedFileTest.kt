import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class VersionedFileTest {

    @Test
    fun emptyFile() {
        val f = VersionedFile()
        assertNull(f.current())
        assertEquals(emptyList(), f.versions())
    }

    @Test
    fun savesCreateIncreasingVersions() {
        val f = VersionedFile()
        assertEquals(1, f.save("a"))
        assertEquals(2, f.save("b"))
        assertEquals("b", f.current())
        assertEquals(listOf(1, 2), f.versions())
        assertEquals("a", f.read(1))
    }

    @Test
    fun savingIdenticalContentDoesNotCreateAVersion() {
        val f = VersionedFile()
        f.save("a")
        assertEquals(1, f.save("a"))
        f.save("b")
        assertEquals(2, f.save("b"))
        assertEquals(listOf(1, 2), f.versions())
        assertEquals(3, f.save("a"))            // same as version 1, but not the *current* content
    }

    @Test
    fun oldVersionsAreDroppedAndNumbersAreNeverReused() {
        val f = VersionedFile(3)
        listOf("a", "b", "c", "d", "e").forEach { f.save(it) }
        assertEquals(listOf(3, 4, 5), f.versions())
        assertFailsWith<IllegalArgumentException> { f.read(1) }
        assertFailsWith<IllegalArgumentException> { f.read(2) }
        assertEquals("c", f.read(3))
        assertEquals(6, f.save("f"))
        assertEquals(listOf(4, 5, 6), f.versions())
    }

    @Test
    fun restoreCreatesANewVersionAndKeepsTheNewerOnes() {
        val f = VersionedFile(5)
        f.save("a"); f.save("b"); f.save("c")
        assertEquals(4, f.restore(1))
        assertEquals("a", f.current())
        assertEquals(listOf(1, 2, 3, 4), f.versions())
        assertEquals("c", f.read(3))
    }

    @Test
    fun restoringTheCurrentVersionChangesNothing() {
        val f = VersionedFile(5)
        f.save("a"); f.save("b")
        assertEquals(2, f.restore(2))
        assertEquals(listOf(1, 2), f.versions())
    }

    @Test
    fun restoreCanPushOldVersionsOut() {
        val f = VersionedFile(3)
        listOf("a", "b", "c", "d", "e").forEach { f.save(it) }
        assertEquals(6, f.restore(3))               // content "c"
        assertEquals(listOf(4, 5, 6), f.versions())
        assertEquals("c", f.current())
    }

    @Test
    fun restoreOfUnknownVersionFails() {
        val f = VersionedFile(2)
        f.save("a"); f.save("b"); f.save("c")
        assertFailsWith<IllegalArgumentException> { f.restore(1) }
        assertFailsWith<IllegalArgumentException> { f.restore(99) }
    }
}
