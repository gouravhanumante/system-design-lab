/** Provided: column used by `item` in `row`. Different rows behave like different hash functions. */
fun bucket(item: String, row: Int, width: Int): Int {
    var h = (row + 1) * 0x01000193
    for (c in item) h = (h xor c.code) * 0x01000193
    h = h xor (h ushr 15)
    return Math.floorMod(h, width)
}

class CountMinSketch(private val width: Int, private val depth: Int) {

    /** [depth] rows × [width] counters. Fixed memory, no matter how many different items you count. */
    private val table: Array<LongArray>

    /** Step 1 — `width < 1` or `depth < 1` → `IllegalArgumentException`; then create the table (all zeros). */
    init {
        // TODO Step 1: check the sizes, then assign `table = Array(depth) { LongArray(width) }`
        table = Array(depth) { LongArray(width) }
    }

    /** Step 1 — in EVERY row r, add [count] to the counter at column `bucket(item, r, width)`. */
    fun add(item: String, count: Long = 1) {
        TODO("Step 1")
    }

    /**
     * Step 1 — the smallest of the item's counters across all rows.
     * Other items may share a counter (collision) so each counter can only be too high, never too low — the minimum is the best guess.
     */
    fun estimate(item: String): Long {
        TODO("Step 1")
    }
}

class TrendingTags(private val k: Int, width: Int = 1024, depth: Int = 4) {

    /** Counts every tag ever seen (approximately). */
    private val sketch = CountMinSketch(width, depth)

    /** At most [k] tags we're tracking as possible top tags. */
    private val candidates = HashSet<String>()

    /** Step 3 — the candidate with the LOWEST estimate; tie → the alphabetically LARGEST tag. */
    private fun weakestCandidate(): String? {
        TODO("Step 3")
    }

    /**
     * Steps 2–3 — count the tag in the sketch, then:
     * - already a candidate → done;
     * - fewer than [k] candidates → add it;
     * - otherwise (step 3) replace the weakest candidate, but ONLY if this tag's estimate is strictly greater than the weakest's.
     */
    fun observe(tag: String) {
        TODO("Step 2")
    }

    /** Step 2 — candidates with their CURRENT estimates, highest first, ties by tag A→Z. */
    fun top(): List<Pair<String, Long>> {
        TODO("Step 2")
    }
}
