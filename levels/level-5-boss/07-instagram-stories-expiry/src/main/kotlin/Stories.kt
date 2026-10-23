enum class Audience { EVERYONE, CLOSE_FRIENDS }

class Stories(private val ttlSeconds: Long = 86_400) {

    // ───────────── State ─────────────

    private class Story(val id: String, val owner: String, val audience: Audience, val postedAt: Long) {
        /** Highlighted stories never expire. */
        var highlighted = false
        /** Unique viewers (not the owner), in the order they first viewed. */
        val viewers = LinkedHashSet<String>()
    }

    /** story id → story. */
    private val stories = LinkedHashMap<String, Story>()

    /** owner → their close-friends list (checked at VIEW time, not post time). */
    private val closeFriends = HashMap<String, MutableSet<String>>()

    // ───────────── Helpers ─────────────

    /** Step 1 — live = highlighted, or less than [ttlSeconds] since it was posted (`now - postedAt < ttlSeconds`). */
    private fun isLive(story: Story, now: Long): Boolean {
        TODO("Step 1")
    }

    /**
     * Step 1 — may [viewer] see [story] now? Must be live. The owner always may.
     * EVERYONE → anyone; CLOSE_FRIENDS → only people on the owner's close-friends list right now.
     */
    private fun isAllowed(viewer: String, story: Story, now: Long): Boolean {
        TODO("Step 1")
    }

    // ───────────── API ─────────────

    /** Step 2 */
    fun addCloseFriend(owner: String, friend: String) {
        TODO("Step 2")
    }

    /** Step 2 — they lose access to close-friends stories from now on. */
    fun removeCloseFriend(owner: String, friend: String) {
        TODO("Step 2")
    }

    /** Step 1 — post a story. An id that already exists → ignore. */
    fun post(owner: String, storyId: String, audience: Audience, now: Long) {
        TODO("Step 1")
    }

    /** Step 1 — [isAllowed] for an existing story; unknown story → `false`. */
    fun canView(viewer: String, storyId: String, now: Long): Boolean {
        TODO("Step 1")
    }

    /** Step 3 — like [canView], and when `true` and the viewer isn't the owner, record them as a viewer (once). */
    fun view(viewer: String, storyId: String, now: Long): Boolean {
        TODO("Step 3")
    }

    /** Step 3 — recorded viewers in first-view order (earlier views stay even if access is later removed). */
    fun viewers(storyId: String): List<String> {
        TODO("Step 3")
    }

    /** Step 4 — ids of [owner]'s stories that [viewer] can see right now, oldest first (tie → id). */
    fun visibleStories(owner: String, viewer: String, now: Long): List<String> {
        TODO("Step 4")
    }

    /**
     * Step 5 — keep a story forever. Only the owner, and only while the story is still live → `true`.
     * Otherwise (unknown story, not the owner, already expired) → `false`. The audience still applies after highlighting.
     */
    fun highlight(owner: String, storyId: String, now: Long): Boolean {
        TODO("Step 5")
    }
}
