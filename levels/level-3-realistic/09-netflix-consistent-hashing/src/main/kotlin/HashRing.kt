/** Provided: FNV-1a 32-bit hash, as an unsigned position on the ring (0 .. 2^32 - 1). */
fun ringPosition(s: String): Long {
    var h = 0x811C9DC5.toInt()
    for (b in s.toByteArray()) {
        h = h xor (b.toInt() and 0xFF)
        h *= 0x01000193
    }
    h = h xor (h ushr 16)
    h *= 0x45d9f3b
    h = h xor (h ushr 16)
    return h.toLong() and 0xFFFFFFFFL
}

class HashRing(private val virtualNodes: Int) {

    // ───────────── State ─────────────

    /** The ring: position → node that owns that point. TreeMap = sorted by position, so "next point clockwise" is one call. */
    private val ring = java.util.TreeMap<Long, String>()

    /** The real nodes (servers) currently in the ring. */
    private val members = LinkedHashSet<String>()

    /** Step 1 — `virtualNodes < 1` → `IllegalArgumentException`. */
    init {
        // TODO Step 1
    }

    // ───────────── Helper ─────────────

    /**
     * Step 2 — recompute `ring` from `members`: every node gets [virtualNodes] points at `ringPosition("<node>#<i>")`
     * for i = 0 until virtualNodes. If two points land on the same position, the smaller node name keeps it.
     */
    private fun rebuildRing() {
        TODO("Step 2")
    }

    // ───────────── API ─────────────

    /** Step 2 — add a server (already there → nothing). */
    fun addNode(node: String) {
        TODO("Step 2")
    }

    /** Step 2 — remove a server (unknown → nothing). Only the keys it owned move to other servers. */
    fun removeNode(node: String) {
        TODO("Step 2")
    }

    /**
     * Step 3 — the server for [key]: the first ring point at or after `ringPosition(key)` (clockwise);
     * if there is none, wrap around to the first point. Empty ring → `null`.
     * Tip: `ring.ceilingEntry(h) ?: ring.firstEntry()`.
     */
    fun nodeFor(key: String): String? {
        TODO("Step 3")
    }

    /** Step 1 — copy of the members. */
    fun nodes(): Set<String> {
        TODO("Step 1")
    }
}
