class ReplicatedPartition(private val replicas: List<String>, private val minInSync: Int = 1) {

    // ───────────── State ─────────────

    /** Each replica's own copy of the log. */
    private val logs = HashMap<String, MutableList<String>>()

    /** In-sync replicas, in order (the leader is always in it). A recovered replica is added back at the END. */
    private val inSync = ArrayList<String>()

    /** Replicas that are currently down. */
    private val down = HashSet<String>()

    /** The current leader, or `null` if no replica is available. */
    private var currentLeader: String? = null

    /** Step 1 — every replica starts with an empty log and is in sync; the first replica is the leader. */
    init {
        // TODO Step 1
    }

    // ───────────── Helper ─────────────

    /** Step 4 — cut [replica]'s log down to [size] records (remove from the end). */
    private fun truncate(replica: String, size: Int) {
        TODO("Step 4")
    }

    // ───────────── API ─────────────

    /** Step 1 */
    fun leader(): String? {
        TODO("Step 1")
    }

    /** Step 1 — a copy of the in-sync list, in order. */
    fun isr(): List<String> {
        TODO("Step 1")
    }

    /**
     * Step 2 — append [value] to the LEADER's log only and return its offset.
     * No leader, or fewer than [minInSync] replicas in sync → `IllegalStateException`.
     */
    fun produce(value: String): Long {
        TODO("Step 2")
    }

    /**
     * Step 2 — [follower] copies the leader: first remove any extra records it has beyond the leader's log,
     * then copy the missing ones. Now it's caught up → add it to the in-sync list if it isn't there.
     * Does nothing if there's no leader, or [follower] is unknown, down, or is the leader.
     */
    fun replicate(follower: String) {
        TODO("Step 2")
    }

    /** Step 3 — number of records that EVERY in-sync replica has (the smallest in-sync log size). No leader → 0. */
    fun highWatermark(): Long {
        TODO("Step 3")
    }

    /** Step 3 — consumers only see committed data: the leader's records below the high watermark (none if no leader). */
    fun consume(): List<String> {
        TODO("Step 3")
    }

    /**
     * Step 4 — [replica] goes down. Unknown → `IllegalArgumentException`; already down → nothing.
     * Mark it down and remove it from the in-sync list. If it was the leader: the new leader is the FIRST remaining
     * in-sync replica (or `null` if none), and every other in-sync replica truncates its log to the new leader's size.
     */
    fun fail(replica: String) {
        TODO("Step 4")
    }

    /**
     * Step 5 — [replica] comes back. Unknown → `IllegalArgumentException`; not down → nothing.
     * - There is a leader: truncate the replica's log to the high watermark; it is NOT in sync until it `replicate`s.
     * - There is no leader ("unclean" recovery): it becomes the leader with whatever log it has, and the only in-sync replica.
     */
    fun recover(replica: String) {
        TODO("Step 5")
    }

    /** Step 1 — a copy of that replica's log. */
    fun logOf(replica: String): List<String> {
        TODO("Step 1")
    }
}
