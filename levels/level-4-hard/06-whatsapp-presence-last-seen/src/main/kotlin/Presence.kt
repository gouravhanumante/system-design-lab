enum class Visibility { EVERYONE, CONTACTS, NOBODY }

class Presence(private val onlineTimeoutSeconds: Long = 30) {

    fun heartbeat(user: String, now: Long) {
        TODO()
    }

    fun setVisibility(user: String, visibility: Visibility) {
        TODO()
    }

    /** Puts [contact] into [owner]'s contact list. */
    fun addContact(owner: String, contact: String) {
        TODO()
    }

    /** "online", "last seen <t>", "unknown" or "hidden". */
    fun view(viewer: String, target: String, now: Long): String {
        TODO()
    }
}
