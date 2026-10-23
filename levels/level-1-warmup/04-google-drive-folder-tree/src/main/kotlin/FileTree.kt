class FileTree {

    // ───────────── State ─────────────

    /** A folder or a file. Folders have children (name → node); files have a size. */
    private class Node(val isFolder: Boolean, val size: Long = 0) {
        /** TreeMap keeps children sorted by name, which `list` needs. */
        val children = java.util.TreeMap<String, Node>()
    }

    /** The folder "/". */
    private val root = Node(isFolder = true)

    // ───────────── Helpers ─────────────

    /**
     * Step 1 — `"/photos/2024/a.jpg"` → `["photos", "2024", "a.jpg"]`; `"/"` → `[]`.
     * A path that doesn't start with `/` → `IllegalArgumentException`.
     */
    private fun splitPath(path: String): List<String> {
        TODO("Step 1")
    }

    /** Step 1 — the node at [path] (start at `root`, follow children name by name), or `null` if anything is missing. */
    private fun findNode(path: String): Node? {
        TODO("Step 1")
    }

    /**
     * Step 2 — put [node] at [path]. Each problem → `IllegalArgumentException`:
     * path is `/` itself · parent folder doesn't exist · parent is a file · the name already exists there.
     */
    private fun addNode(path: String, node: Node) {
        TODO("Step 2")
    }

    /** Step 4 — a file's size, or for a folder the sum of everything inside it (recursively). */
    private fun sizeOf(node: Node): Long {
        TODO("Step 4")
    }

    // ───────────── API ─────────────

    /** Step 2 — create an empty folder (the parent must already exist). */
    fun mkdir(path: String) {
        TODO("Step 2")
    }

    /** Step 2 — create a file with that size (the parent folder must already exist). */
    fun createFile(path: String, sizeBytes: Long) {
        TODO("Step 2")
    }

    /**
     * Step 3 — names of the direct children, sorted A→Z; folder names end with `/` (e.g. `["docs/", "a.txt"]`).
     * Missing path or a file → `IllegalArgumentException`.
     */
    fun list(path: String): List<String> {
        TODO("Step 3")
    }

    /** Step 2 — does something exist at [path]? (`"/"` always exists.) */
    fun exists(path: String): Boolean {
        TODO("Step 2")
    }

    /** Step 4 — total bytes at [path] (a file's own size, or everything inside a folder). Missing → `IllegalArgumentException`. */
    fun totalSize(path: String): Long {
        TODO("Step 4")
    }
}
