class BitrateSelector(
    private val ladderKbps: List<Int>,
    private val safetyFactor: Double = 0.8,
    private val window: Int = 3,
) {

    // ───────────── State ─────────────

    /** Throughput of the last downloads in kbps, oldest first (keep at most [window]). */
    private val samples = ArrayDeque<Double>()

    /** Index in [ladderKbps] of the quality currently playing (starts at 0 = lowest). */
    private var currentRung = 0

    /** Step 1 — empty ladder, or not strictly ascending → `IllegalArgumentException`. */
    init {
        // TODO Step 1
    }

    // ───────────── API ─────────────

    /** Step 2 — record one segment: throughput kbps = `bytes * 8.0 / millis` (bits per ms = kilobits per second). Keep only the last [window]. */
    fun onSegmentDownloaded(bytes: Long, millis: Long) {
        TODO("Step 2")
    }

    /**
     * Step 2 — harmonic mean of the samples: `n / (1/s1 + 1/s2 + … + 1/sn)`. `null` if no samples.
     * (The harmonic mean is pulled down by slow downloads — safer than a normal average.)
     */
    fun estimateKbps(): Double? {
        TODO("Step 2")
    }

    /** Step 3 — index of the highest rung with `kbps <= budget` (0 if even the lowest is above the budget). */
    private fun highestAffordableRung(budgetKbps: Double): Int {
        TODO("Step 3")
    }

    /**
     * Step 3 — choose the quality for the next segment, remember it in `currentRung`, and return its kbps:
     * - No estimate yet, or `bufferSeconds < 5` (about to stall) → rung 0.
     * - Otherwise target = [highestAffordableRung] of `safetyFactor × estimate`.
     *   - target lower than current → drop straight to the target.
     *   - target higher → go up ONE rung only, and only if `bufferSeconds >= 15`.
     *   - otherwise stay.
     */
    fun choose(bufferSeconds: Double): Int {
        TODO("Step 3")
    }
}
