class CommentThreads {

    fun add(postId: String, commentId: String, author: String, text: String, parentId: String? = null) {
        TODO()
    }

    /** Soft delete. */
    fun delete(commentId: String) {
        TODO()
    }

    /** Indented lines, depth-first. */
    fun render(postId: String): List<String> {
        TODO()
    }

    /** Number of comments in the post that are not deleted. */
    fun count(postId: String): Int {
        TODO()
    }
}
