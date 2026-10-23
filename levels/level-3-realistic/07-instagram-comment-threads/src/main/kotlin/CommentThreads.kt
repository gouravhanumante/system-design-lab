class CommentThreads {

    // ───────────── State ─────────────

    /** One comment and its replies (in the order they were added). */
    private class Comment(val postId: String, val author: String, val text: String) {
        val replies = ArrayList<Comment>()
        /** Soft delete: the comment stays in the tree so its replies keep their place. */
        var deleted = false
    }

    /** comment id → comment (all posts). */
    private val comments = HashMap<String, Comment>()

    /** post id → its top-level comments, in order. */
    private val topLevel = HashMap<String, MutableList<Comment>>()

    // ───────────── API ─────────────

    /**
     * Step 1 — add a comment. [parentId] null = top-level comment on the post; otherwise a reply to that comment.
     * `IllegalArgumentException` if: the comment id already exists · the parent doesn't exist · the parent belongs to another post.
     * Replying to a deleted comment is allowed.
     */
    fun add(postId: String, commentId: String, author: String, text: String, parentId: String? = null) {
        TODO("Step 1")
    }

    /** Step 2 — soft delete (set the flag). Unknown id → `IllegalArgumentException`. */
    fun delete(commentId: String) {
        TODO("Step 2")
    }

    /** Step 4 — `true` if this comment is not deleted, or ANY reply below it (at any depth) is not deleted. */
    private fun hasVisibleContent(comment: Comment): Boolean {
        TODO("Step 4")
    }

    /**
     * Steps 3–4 — add [comment] and then its replies (depth-first) to [out].
     * - Indent = two spaces per level: `"author: text"`, replies `"  author: text"`, theirs `"    author: text"`.
     * - Step 4: a deleted comment shows as `"[deleted]"` (same indent) if something below it is still visible;
     *   if nothing in its branch is visible, skip the whole branch.
     */
    private fun renderInto(comment: Comment, depth: Int, out: MutableList<String>) {
        TODO("Step 3")
    }

    /** Step 3 — all lines for the post's top-level comments in order (empty list if none). */
    fun render(postId: String): List<String> {
        TODO("Step 3")
    }

    /** Step 1 — comments on the post that are not deleted. */
    fun count(postId: String): Int {
        TODO("Step 1")
    }
}
