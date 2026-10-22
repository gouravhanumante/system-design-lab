import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SyncTest {

    private fun view(c: SyncClient) = c.files().associateWith { c.read(it) }

    // ---- server alone ----

    @Test
    fun serverAcceptsNewFilesOnlyWithBaseZero() {
        val s = SyncServer()
        assertEquals(PushResult.Accepted(1), s.push("a", 0, "x"))
        assertFailsWith<IllegalArgumentException> { s.push("b", 3, "x") }
        assertNull(s.fetch("b"))
    }

    @Test
    fun serverAppliesRevisionRules() {
        val s = SyncServer()
        s.push("a", 0, "v1")
        assertEquals(PushResult.Accepted(2), s.push("a", 1, "v2"))
        assertEquals(PushResult.Accepted(2), s.push("a", 1, "v2"))                  // same content: no new revision
        assertEquals(PushResult.Conflict(Versioned("v2", 2)), s.push("a", 1, "v3"))  // stale base
        assertEquals(Versioned("v2", 2), s.fetch("a"))
        assertEquals(PushResult.Conflict(Versioned("v2", 2)), s.push("a", 0, "other")) // created twice
        assertEquals(mapOf("a" to Versioned("v2", 2)), s.snapshot())
    }

    // ---- clients ----

    @Test
    fun newFilesPropagateToOtherDevices() {
        val server = SyncServer()
        val a = SyncClient("laptop", server); val b = SyncClient("phone", server)
        a.edit("a.txt", "hello")
        a.sync(); b.sync()
        assertEquals("hello", b.read("a.txt"))
        assertEquals(setOf("a.txt"), b.files())
    }

    @Test
    fun updatesPropagateAndRevisionsIncrease() {
        val server = SyncServer()
        val a = SyncClient("laptop", server); val b = SyncClient("phone", server)
        a.edit("a.txt", "v1"); a.sync(); b.sync()
        b.edit("a.txt", "v2"); b.sync(); a.sync()
        assertEquals("v2", a.read("a.txt"))
        assertEquals(2, server.fetch("a.txt")!!.revision)
    }

    @Test
    fun editingToTheSameContentDoesNothing() {
        val server = SyncServer()
        val a = SyncClient("laptop", server)
        a.edit("a.txt", "v1"); a.sync()
        a.edit("a.txt", "v1"); a.sync()
        assertEquals(1, server.fetch("a.txt")!!.revision)
    }

    @Test
    fun concurrentEditsCreateAConflictCopyAndLoseNothing() {
        val server = SyncServer()
        val a = SyncClient("laptop", server); val b = SyncClient("phone", server)
        a.edit("a.txt", "v1"); a.sync(); b.sync()
        a.edit("a.txt", "A-version"); a.sync()                  // revision 2
        b.edit("a.txt", "B-version"); b.sync()                  // based on revision 1 -> conflict
        assertEquals("A-version", b.read("a.txt"))
        assertEquals("B-version", b.read("a.txt (conflict phone)"))
        assertEquals(Versioned("B-version", 1), server.fetch("a.txt (conflict phone)"))
        a.sync()
        assertEquals("B-version", a.read("a.txt (conflict phone)"))
        assertEquals(view(a), view(b))
    }

    @Test
    fun identicalConcurrentEditsAreNotAConflict() {
        val server = SyncServer()
        val a = SyncClient("laptop", server); val b = SyncClient("phone", server)
        a.edit("a.txt", "v1"); a.sync(); b.sync()
        a.edit("a.txt", "same"); b.edit("a.txt", "same")
        a.sync(); b.sync()
        assertEquals(setOf("a.txt"), b.files())
        assertEquals(2, server.fetch("a.txt")!!.revision)
    }

    @Test
    fun sameNewFileNameCreatedOnTwoDevices() {
        val server = SyncServer()
        val a = SyncClient("laptop", server); val b = SyncClient("phone", server)
        a.edit("n.txt", "from laptop"); b.edit("n.txt", "from phone")
        a.sync(); b.sync()
        assertEquals("from laptop", b.read("n.txt"))
        assertEquals("from phone", b.read("n.txt (conflict phone)"))
        assertTrue(server.snapshot().containsKey("n.txt (conflict phone)"))
    }

    @Test
    fun secondConflictOnTheSameFileGetsANumberedCopy() {
        val server = SyncServer()
        val a = SyncClient("laptop", server); val b = SyncClient("phone", server)
        a.edit("a.txt", "v1"); a.sync(); b.sync()
        a.edit("a.txt", "A2"); a.sync()
        b.edit("a.txt", "B1"); b.sync()                        // conflict -> "a.txt (conflict phone)"
        a.edit("a.txt", "A3"); a.sync()
        b.edit("a.txt", "B2"); b.sync()                        // conflict again -> numbered copy
        assertEquals("B1", b.read("a.txt (conflict phone)"))
        assertEquals("B2", b.read("a.txt (conflict phone) 2"))
    }

    @Test
    fun threeDevicesConvergeAfterEveryoneSyncsTwice() {
        val server = SyncServer()
        val a = SyncClient("A", server); val b = SyncClient("B", server); val c = SyncClient("C", server)
        a.edit("x", "x0"); a.sync(); b.sync(); c.sync()
        a.edit("x", "xA"); b.edit("x", "xB"); c.edit("y", "yC")
        a.sync(); b.sync(); c.sync()
        a.sync(); b.sync(); c.sync()
        assertEquals(view(a), view(b))
        assertEquals(view(b), view(c))
        assertEquals("xA", a.read("x"))
        assertEquals("xB", a.read("x (conflict B)"))
        assertEquals("yC", a.read("y"))
    }
}
