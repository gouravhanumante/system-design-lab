class ReplicatedDb(private val replicaCount: Int = 2, private val lagSeconds: Long = 5) {

    // ───────────── State ─────────────

    /** key → every write to it, oldest first, as (time, value). Keeping history lets you see "the value at time t". */
    private val history = HashMap<String, MutableList<Pair<Long, String>>>()

    /** user → time of their most recent write (any key). */
    private val lastWriteAt = HashMap<String, Long>()

    /** How many replica reads so far — used to rotate replica-0, replica-1, … */
    private var replicaReads = 0

    /** Where the last read was served from: `"primary"` or `"replica-<n>"`. */
    var lastReadFrom: String = ""
        private set

    /** How many replica reads returned something different from what the primary had at that moment. */
    var staleReads: Int = 0
        private set

    /** Step 1 — `replicaCount < 1` or `lagSeconds < 0` → `IllegalArgumentException`. */
    init {
        // TODO Step 1 (use require(...))
    }

    // ───────────── API ─────────────

    /** Step 2 — the primary accepts the write at time [now]: add it to `history`, update `lastWriteAt[user]`. */
    fun write(user: String, key: String, value: String, now: Long) {
        TODO("Step 2")
    }

    /** Step 3 — the latest value written to [key] at or before time [t]; `null` if nothing was written yet. */
    private fun valueAt(key: String, t: Long): String? {
        TODO("Step 3")
    }

    /**
     * Steps 4 + 5 — read [key] for [user] at time [now].
     * - Step 5: if this user wrote anything less than `lagSeconds` ago (`now - lastWrite < lagSeconds`)
     *   → read from the primary: `lastReadFrom = "primary"`, return `valueAt(key, now)`.
     * - Step 4, otherwise → a replica, which is `lagSeconds` behind:
     *   `lastReadFrom = "replica-<replicaReads % replicaCount>"`, then `replicaReads++`,
     *   return `valueAt(key, now - lagSeconds)`; if that differs from `valueAt(key, now)`, `staleReads++`.
     */
    fun read(user: String, key: String, now: Long): String? {
        TODO("Step 4")
    }
}
