/** Provided: column used by `item` in `row`. Different rows behave like different hash functions. */
fun bucket(item: String, row: Int, width: Int): Int {
    var h = (row + 1) * 0x01000193
    for (c in item) h = (h xor c.code) * 0x01000193
    h = h xor (h ushr 15)
    return Math.floorMod(h, width)
}

class CountMinSketch(private val width: Int, private val depth: Int) {

    fun add(item: String, count: Long = 1) {
        TODO()
    }

    fun estimate(item: String): Long {
        TODO()
    }
}

class TrendingTags(private val k: Int, width: Int = 1024, depth: Int = 4) {

    fun observe(tag: String) {
        TODO()
    }

    /** Candidates with current estimates, highest first, ties by tag. */
    fun top(): List<Pair<String, Long>> {
        TODO()
    }
}
