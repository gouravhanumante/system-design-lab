data class Versioned(val content: String, val revision: Int)

sealed class PushResult {
    data class Accepted(val revision: Int) : PushResult()
    data class Conflict(val serverCopy: Versioned) : PushResult()
}

class SyncServer {

    /** file name → latest content + revision (TreeMap keeps names sorted). */
    private val files = java.util.TreeMap<String, Versioned>()

    /**
     * Step 1 — a client uploads a change it made on top of [baseRevision].
     * - File doesn't exist: [baseRevision] must be 0 (otherwise `IllegalArgumentException`); store it as revision 1 → `Accepted(1)`.
     * - Same content as the server already has → `Accepted(current revision)` (nothing changes — not a conflict).
     * - [baseRevision] == current revision → store with revision + 1 → `Accepted(new revision)`.
     * - Otherwise someone else changed it first → `Conflict(the server's copy)`.
     */
    fun push(name: String, baseRevision: Int, content: String): PushResult {
        TODO("Step 1")
    }

    /** Step 1 — the server's copy, or `null`. */
    fun fetch(name: String): Versioned? {
        TODO("Step 1")
    }

    /** Step 1 — a copy of all files (so callers can't change the server's map). */
    fun snapshot(): Map<String, Versioned> {
        TODO("Step 1")
    }
}

class SyncClient(private val device: String, private val server: SyncServer) {

    /** The device's own copy of a file. */
    private class LocalFile(var content: String, var baseRevision: Int, var dirty: Boolean)
    // baseRevision = server revision this content is based on (0 = never uploaded); dirty = changed locally since last sync.

    /** file name → local copy (sorted by name, so `sync` handles files A→Z). */
    private val local = java.util.TreeMap<String, LocalFile>()

    /** Step 2 — edit offline. New file → base 0, dirty. Existing file with DIFFERENT content → change it, mark dirty. Same content → nothing. */
    fun edit(file: String, content: String) {
        TODO("Step 2")
    }

    /** Step 2 — the local content, or `null`. */
    fun read(file: String): String? {
        TODO("Step 2")
    }

    /** Step 2 — names of all local files. */
    fun files(): Set<String> {
        TODO("Step 2")
    }

    /**
     * Step 4 — a free name for a conflict copy: `"<name> (conflict <device>)"`; if that's taken locally,
     * `"<name> (conflict <device>) 2"`, then `3`, …
     */
    private fun conflictCopyName(name: String): String {
        TODO("Step 4")
    }

    /**
     * Steps 3–4 — go online.
     * Part 1 — PUSH: repeatedly take the first dirty file (A→Z) and `server.push(name, baseRevision, content)`:
     *   - `Accepted(r)` → baseRevision = r, not dirty.
     *   - `Conflict(server)` → nothing may be lost: save YOUR content as a new local file named [conflictCopyName]
     *     (base 0, dirty — so it gets uploaded too), and replace this file with the server's content/revision (not dirty).
     *   Keep going until no file is dirty.
     * Part 2 — PULL: for every file in `server.snapshot()`: not on this device → add it (not dirty);
     *   on this device, not dirty and older (baseRevision < server revision) → take the server's version.
     */
    fun sync() {
        TODO("Step 3")
    }
}
