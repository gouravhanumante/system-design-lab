class GroupChat {

    fun addMember(user: String) {
        TODO()
    }

    fun removeMember(user: String) {
        TODO()
    }

    /** Throws IllegalStateException if the sender is not a member. */
    fun send(sender: String, messageId: String, text: String) {
        TODO()
    }

    fun inbox(user: String): List<String> {
        TODO()
    }

    fun markRead(user: String, messageId: String) {
        TODO()
    }

    fun readBy(messageId: String): Set<String> {
        TODO()
    }

    fun isReadByAll(messageId: String): Boolean {
        TODO()
    }
}
