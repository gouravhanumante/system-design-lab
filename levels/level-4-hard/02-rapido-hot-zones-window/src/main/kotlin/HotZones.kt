class HotZones(private val windowSeconds: Int = 300, private val bucketSeconds: Int = 10) {

    // ───────────── State ─────────────

    /** How many buckets fit in the window (300 / 10 = 30). */
    private val windowBuckets = windowSeconds / bucketSeconds

    /**
     * Time is cut into buckets of [bucketSeconds]; bucket number = `now / bucketSeconds`.
     * Each entry = (bucket number, zone → count in that bucket), oldest first. Only buckets that had events exist.
     */
    private val buckets = ArrayDeque<Pair<Long, HashMap<String, Int>>>()

    /** zone → total count over all buckets still in the window (kept up to date, so queries are fast). Zones at 0 are removed. */
    private val totals = HashMap<String, Int>()

    // ───────────── Helpers ─────────────

    /**
     * Step 3 — remove buckets that are too old: a bucket `b` is expired when `currentBucket - b >= windowBuckets`
     * (currentBucket = `now / bucketSeconds`). Subtract each removed bucket's counts from `totals`.
     */
    private fun expireOldBuckets(now: Long) {
        TODO("Step 3")
    }

    // ───────────── API ─────────────

    /** Step 1 — one ride request in [zone] at [now]: +1 in the current bucket (create it if the last bucket is older) and +1 in `totals`. */
    fun record(zone: String, now: Long) {
        TODO("Step 1")
    }

    /** Step 2 — up to [k] (zone, count) pairs, highest count first, ties by zone name A→Z. */
    fun topZones(k: Int, now: Long): List<Pair<String, Int>> {
        TODO("Step 2")
    }
}
