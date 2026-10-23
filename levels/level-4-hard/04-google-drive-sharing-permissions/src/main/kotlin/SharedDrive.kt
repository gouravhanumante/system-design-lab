enum class Permission { NONE, VIEW, EDIT, OWNER }   // ordered: you can compare them, e.g. `p >= Permission.EDIT`

class SharedDrive {

    // ───────────── State ─────────────

    private class Folder(val parent: Folder?) {
        val children = HashMap<String, Folder>()
        /** Grants set directly on this folder: user OR group name → permission. */
        val grants = HashMap<String, Permission>()
        /** If true, grants from folders ABOVE this one don't apply here (or below). */
        var inheritanceBroken = false
    }

    /** "/" — nobody owns it; anyone may create top-level folders. */
    private val root = Folder(parent = null)

    /** group name → its members. */
    private val groups = HashMap<String, MutableSet<String>>()

    // ───────────── Helpers ─────────────

    /** Step 1 — `"/team/q1"` → `["team", "q1"]`. */
    private fun splitPath(path: String): List<String> {
        TODO("Step 1")
    }

    /** Step 1 — the folder at [path], or `null`. */
    private fun findFolder(path: String): Folder? {
        TODO("Step 1")
    }

    /** Step 1 — the folder at [path], or `IllegalArgumentException`. */
    private fun folderOrThrow(path: String): Folder {
        TODO("Step 1")
    }

    /**
     * Steps 2–4, 6 — the permission [user] really has on [folder]:
     * - "Who I am" = the user plus every group they're in RIGHT NOW (so joining a group later still works).
     * - Look at the folder's grants for any of those names, then its parent's, and so on up to the root —
     *   but stop after a folder with `inheritanceBroken` (don't look above it).
     * - Return the HIGHEST permission found (NONE if nothing).
     */
    private fun effectivePermission(user: String, folder: Folder): Permission {
        TODO("Step 2")
    }

    /** Step 3 — throw `SecurityException` unless [by] is OWNER of [folder]. */
    private fun requireOwner(folder: Folder, by: String) {
        TODO("Step 3")
    }

    // ───────────── API ─────────────

    /** Step 1 — add [user] to [group] (creating the group if needed). */
    fun addToGroup(group: String, user: String) {
        TODO("Step 1")
    }

    /**
     * Steps 2 + 5 — create a folder; the creator gets an OWNER grant on it.
     * - Path is `/`, parent missing, or name exists → `IllegalArgumentException`.
     * - Step 5: inside another folder (not the root), the creator needs at least EDIT on the parent → otherwise `SecurityException`.
     */
    fun createFolder(path: String, creator: String) {
        TODO("Step 2")
    }

    /** Step 3 — only an OWNER may share. Sets (or replaces) [principal]'s grant on that folder. */
    fun share(path: String, principal: String, permission: Permission, by: String) {
        TODO("Step 3")
    }

    /** Step 3 — only an OWNER may revoke. Removes the grant set on THIS folder (inherited grants still apply). */
    fun revoke(path: String, principal: String, by: String) {
        TODO("Step 3")
    }

    /** Step 6 — only an OWNER may do it. From now on grants above this folder are ignored for it and its sub-folders. */
    fun breakInheritance(path: String, by: String) {
        TODO("Step 6")
    }

    /** Step 2 — [user]'s effective permission on [path] (missing folder → `IllegalArgumentException`). */
    fun permissionOf(user: String, path: String): Permission {
        TODO("Step 2")
    }
}
