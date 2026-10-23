class ContinueWatching(private val finishedThreshold: Double = 0.95) {

    // ───────────── State ─────────────

    /** The latest progress report for one title. */
    private class Progress(val positionSec: Int, val durationSec: Int, val updatedAt: Long)

    /** profile → (title id → latest progress). Each profile is separate. */
    private val progressByProfile = HashMap<String, HashMap<String, Progress>>()

    // ───────────── Helper ─────────────

    /** Step 1 — finished = watched at least [finishedThreshold] of it (`position >= threshold × duration`). */
    private fun isFinished(p: Progress): Boolean {
        TODO("Step 1")
    }

    // ───────────── API ─────────────

    /** Step 1 — the player reports progress. Always REPLACES the previous report for that title (even rewinding). */
    fun progress(profile: String, titleId: String, positionSec: Int, durationSec: Int, now: Long) {
        TODO("Step 1")
    }

    /** Step 1 — where to resume: the saved position, or 0 if never watched or finished. */
    fun resumePosition(profile: String, titleId: String): Int {
        TODO("Step 1")
    }

    /** Step 2 — unfinished titles, most recently updated first (tie → title id A→Z), at most [limit]. */
    fun row(profile: String, limit: Int): List<String> {
        TODO("Step 2")
    }

    /** Step 3 — "remove from row": forget that title's progress for this profile. */
    fun remove(profile: String, titleId: String) {
        TODO("Step 3")
    }
}
