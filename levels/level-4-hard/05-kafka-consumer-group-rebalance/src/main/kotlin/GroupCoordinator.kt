class GroupCoordinator(private val partitions: Int, private val sessionTimeoutSeconds: Long = 10) {

    // ───────────── State ─────────────

    /** Current members → time of their last join/heartbeat. */
    private val lastSeen = HashMap<String, Long>()

    /** member → the partitions it owns (result of the last rebalance). */
    private var currentAssignment: Map<String, List<Int>> = emptyMap()

    /** How many rebalances have happened (starts at 0). */
    private var generationNumber = 0

    // ───────────── Helper ─────────────

    /**
     * Step 1 — recompute `currentAssignment` and `generationNumber++`.
     * Range assignment: sort members by name; each gets `partitions / members` consecutive partitions, and the FIRST
     * `partitions % members` members get one extra. E.g. 7 partitions, members a, b, c → a=[0,1,2], b=[3,4], c=[5,6].
     * More members than partitions → the last ones get an empty list. No members → empty map.
     */
    private fun rebalance() {
        TODO("Step 1")
    }

    // ───────────── API ─────────────

    /** Step 2 — add [member] (lastSeen = now). Only a NEW member causes a rebalance; re-joining just refreshes lastSeen. */
    fun join(member: String, now: Long) {
        TODO("Step 2")
    }

    /** Step 2 — remove [member] and rebalance. Unknown member → nothing (no rebalance). */
    fun leave(member: String) {
        TODO("Step 2")
    }

    /** Step 3 — known member: lastSeen = now, return `true`. Unknown (never joined, or kicked out) → `false`. */
    fun heartbeat(member: String, now: Long): Boolean {
        TODO("Step 3")
    }

    /**
     * Step 4 — members silent for `sessionTimeoutSeconds` or more (`now - lastSeen >= timeout`) are removed.
     * If any were removed, rebalance ONCE (not once per member). Return their names sorted (empty list if none).
     */
    fun checkTimeouts(now: Long): List<String> {
        TODO("Step 4")
    }

    /** Step 2 — the current assignment. */
    fun assignment(): Map<String, List<Int>> {
        TODO("Step 2")
    }

    /** Step 2 — number of rebalances so far. */
    fun generation(): Int {
        TODO("Step 2")
    }
}
