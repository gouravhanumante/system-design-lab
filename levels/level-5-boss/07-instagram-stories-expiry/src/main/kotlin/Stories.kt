enum class Audience { EVERYONE, CLOSE_FRIENDS }

class Stories(private val ttlSeconds: Long = 86_400) {

    fun addCloseFriend(owner: String, friend: String) {
        TODO()
    }

    fun removeCloseFriend(owner: String, friend: String) {
        TODO()
    }

    fun post(owner: String, storyId: String, audience: Audience, now: Long) {
        TODO()
    }

    fun canView(viewer: String, storyId: String, now: Long): Boolean {
        TODO()
    }

    /** Returns canView(...) and records a unique view when it is true (owner views are not recorded). */
    fun view(viewer: String, storyId: String, now: Long): Boolean {
        TODO()
    }

    fun viewers(storyId: String): List<String> {
        TODO()
    }

    /** Owner's stories the viewer can see right now, oldest first. */
    fun visibleStories(owner: String, viewer: String, now: Long): List<String> {
        TODO()
    }

    fun highlight(owner: String, storyId: String, now: Long): Boolean {
        TODO()
    }
}
