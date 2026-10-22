import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ReplicatedDbTest {

    @Test
    fun writerReadsTheirOwnWriteFromThePrimary() {
        val db = ReplicatedDb(2, 5)
        db.write("alice", "address", "Pune", 10)
        assertEquals("Pune", db.read("alice", "address", 11))
        assertEquals("primary", db.lastReadFrom)
        assertEquals(0, db.staleReads)
    }

    @Test
    fun otherUsersMaySeeStaleDataUntilTheLagPasses() {
        val db = ReplicatedDb(2, 5)
        db.write("alice", "address", "Pune", 10)
        assertNull(db.read("bob", "address", 11))
        assertEquals("replica-0", db.lastReadFrom)
        assertEquals(1, db.staleReads)
        assertNull(db.read("bob", "address", 14))
        assertEquals("replica-1", db.lastReadFrom)
        assertEquals("Pune", db.read("bob", "address", 15))
        assertEquals("replica-0", db.lastReadFrom)
        assertEquals(2, db.staleReads)
    }

    @Test
    fun replicasShowOldValuesNotJustMissingOnes() {
        val db = ReplicatedDb(1, 5)
        db.write("alice", "k", "v1", 0)
        db.write("alice", "k", "v2", 20)
        assertEquals("v1", db.read("bob", "k", 22))
        assertEquals("v2", db.read("bob", "k", 25))
        assertEquals(1, db.staleReads)
    }

    @Test
    fun readYourWritesWindowEnds() {
        val db = ReplicatedDb(2, 5)
        db.write("alice", "k", "v", 10)
        db.read("alice", "k", 14)
        assertEquals("primary", db.lastReadFrom)
        assertEquals("v", db.read("alice", "k", 15))
        assertEquals("replica-0", db.lastReadFrom)
    }

    @Test
    fun anyRecentWriteRoutesAllOfTheUsersReadsToPrimary() {
        val db = ReplicatedDb(2, 5)
        db.write("admin", "price", "999", 0)
        db.write("alice", "cart", "phone", 100)
        assertEquals("999", db.read("alice", "price", 101))
        assertEquals("primary", db.lastReadFrom)
    }

    @Test
    fun zeroLagMeansReplicasAreAlwaysFresh() {
        val db = ReplicatedDb(3, 0)
        db.write("a", "k", "v", 5)
        assertEquals("v", db.read("a", "k", 5))
        assertEquals("replica-0", db.lastReadFrom)
        assertEquals(0, db.staleReads)
    }

    @Test
    fun invalidConfig() {
        assertFailsWith<IllegalArgumentException> { ReplicatedDb(0, 5) }
        assertFailsWith<IllegalArgumentException> { ReplicatedDb(1, -1) }
    }
}
