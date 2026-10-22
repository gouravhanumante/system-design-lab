import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SharedDriveTest {

    private fun drive(): SharedDrive {
        val d = SharedDrive()
        d.createFolder("/docs", "alice")
        d.createFolder("/docs/q1", "alice")
        return d
    }

    @Test
    fun creatorIsOwnerEveryoneElseHasNothing() {
        val d = drive()
        assertEquals(Permission.OWNER, d.permissionOf("alice", "/docs"))
        assertEquals(Permission.NONE, d.permissionOf("bob", "/docs"))
    }

    @Test
    fun sharedPermissionIsInheritedByChildren() {
        val d = drive()
        d.share("/docs", "bob", Permission.VIEW, "alice")
        assertEquals(Permission.VIEW, d.permissionOf("bob", "/docs"))
        assertEquals(Permission.VIEW, d.permissionOf("bob", "/docs/q1"))
        assertEquals(Permission.OWNER, d.permissionOf("alice", "/docs/q1"))   // owner inherits too
    }

    @Test
    fun groupSharesApplyToMembersEvenIfTheyJoinLater() {
        val d = drive()
        d.share("/docs", "team", Permission.EDIT, "alice")
        assertEquals(Permission.NONE, d.permissionOf("carol", "/docs/q1"))
        d.addToGroup("team", "carol")
        assertEquals(Permission.EDIT, d.permissionOf("carol", "/docs/q1"))
    }

    @Test
    fun highestPermissionWins() {
        val d = drive()
        d.addToGroup("team", "bob")
        d.share("/docs", "bob", Permission.VIEW, "alice")
        d.share("/docs/q1", "team", Permission.EDIT, "alice")
        assertEquals(Permission.EDIT, d.permissionOf("bob", "/docs/q1"))
        assertEquals(Permission.VIEW, d.permissionOf("bob", "/docs"))
    }

    @Test
    fun onlyOwnersCanShareRevokeOrBreakInheritance() {
        val d = drive()
        d.share("/docs", "bob", Permission.EDIT, "alice")
        assertFailsWith<SecurityException> { d.share("/docs", "carol", Permission.VIEW, "bob") }
        assertFailsWith<SecurityException> { d.revoke("/docs", "bob", "bob") }
        assertFailsWith<SecurityException> { d.breakInheritance("/docs/q1", "bob") }
        assertFailsWith<SecurityException> { d.share("/docs", "carol", Permission.VIEW, "stranger") }
    }

    @Test
    fun creatingFoldersNeedsEditOnTheParent() {
        val d = drive()
        d.share("/docs", "viewer", Permission.VIEW, "alice")
        d.share("/docs", "editor", Permission.EDIT, "alice")
        assertFailsWith<SecurityException> { d.createFolder("/docs/v", "viewer") }
        assertFailsWith<SecurityException> { d.createFolder("/docs/s", "stranger") }
        d.createFolder("/docs/e", "editor")
        assertEquals(Permission.OWNER, d.permissionOf("editor", "/docs/e"))
        assertEquals(Permission.OWNER, d.permissionOf("alice", "/docs/e"))     // parent's owner still owns it
        d.createFolder("/top", "anyone")                                       // top level is open
    }

    @Test
    fun revokeRemovesTheExplicitGrant() {
        val d = drive()
        d.share("/docs", "bob", Permission.EDIT, "alice")
        d.revoke("/docs", "bob", "alice")
        assertEquals(Permission.NONE, d.permissionOf("bob", "/docs/q1"))
        d.revoke("/docs", "bob", "alice")        // revoking nothing is fine
    }

    @Test
    fun sharingAgainReplacesTheEarlierGrant() {
        val d = drive()
        d.share("/docs", "bob", Permission.EDIT, "alice")
        d.share("/docs", "bob", Permission.VIEW, "alice")
        assertEquals(Permission.VIEW, d.permissionOf("bob", "/docs"))
    }

    @Test
    fun breakingInheritanceHidesFoldersFromAncestorGrants() {
        val d = drive()
        d.share("/docs", "bob", Permission.EDIT, "alice")
        d.createFolder("/docs/private", "alice")
        d.createFolder("/docs/private/deep", "alice")
        d.breakInheritance("/docs/private", "alice")
        assertEquals(Permission.EDIT, d.permissionOf("bob", "/docs/q1"))
        assertEquals(Permission.NONE, d.permissionOf("bob", "/docs/private"))
        assertEquals(Permission.NONE, d.permissionOf("bob", "/docs/private/deep"))
        assertEquals(Permission.OWNER, d.permissionOf("alice", "/docs/private/deep"))   // her own explicit grant is on /docs/private
        d.share("/docs/private", "bob", Permission.VIEW, "alice")
        assertEquals(Permission.VIEW, d.permissionOf("bob", "/docs/private/deep"))
    }

    @Test
    fun invalidPaths() {
        val d = drive()
        assertFailsWith<IllegalArgumentException> { d.permissionOf("alice", "/nope") }
        assertFailsWith<IllegalArgumentException> { d.createFolder("/docs", "alice") }
        assertFailsWith<IllegalArgumentException> { d.createFolder("/ghost/child", "alice") }
    }
}
