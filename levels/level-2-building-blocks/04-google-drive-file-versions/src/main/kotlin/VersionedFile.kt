class VersionedFile(private val maxVersions: Int = 5) {

    // ───────────── State ─────────────

    /** version number → content, oldest first (LinkedHashMap keeps insertion order, so the first key is the oldest). */
    private val history = LinkedHashMap<Int, String>()

    /** The number the next new version gets: 1, 2, 3, … never reused, even after old versions are dropped. */
    private var nextVersion = 1

    /** The version that is "the file" right now (`null` before the first save). */
    private var currentVersion: Int? = null

    // ───────────── API ─────────────

    /**
     * Steps 1–2 — save new content and return the current version number.
     * - Same content as the current version → no new version; return the current number.
     * - Otherwise store it under `nextVersion` (then increase it), make it current.
     * - Step 2: while more than [maxVersions] versions are kept, remove the oldest.
     */
    fun save(content: String): Int {
        TODO("Step 1")
    }

    /** Step 1 — the current content, or `null` if nothing was saved yet. */
    fun current(): String? {
        TODO("Step 1")
    }

    /** Step 1 — the version numbers still kept, oldest first. */
    fun versions(): List<Int> {
        TODO("Step 1")
    }

    /** Step 1 — the content of [version]. Not kept (never existed or dropped) → `IllegalArgumentException`. */
    fun read(version: Int): String {
        TODO("Step 1")
    }

    /**
     * Step 3 — make an old version current again by SAVING its content as a new version (newer versions are kept, not deleted).
     * Returns the resulting current version. Restoring the current version changes nothing. Unknown → `IllegalArgumentException`.
     * Tip: this is one line using `read` and `save`.
     */
    fun restore(version: Int): Int {
        TODO("Step 3")
    }
}
