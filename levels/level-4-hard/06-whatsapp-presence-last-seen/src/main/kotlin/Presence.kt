enum class Visibility { EVERYONE, CONTACTS, NOBODY }

class Presence(private val onlineTimeoutSeconds: Long = 30) {

    // ───────────── State ─────────────

    /** user → time of their latest heartbeat. */
    private val lastHeartbeat = HashMap<String, Long>()

    /** user → who may see their presence (default EVERYONE if never set). */
    private val visibility = HashMap<String, Visibility>()

    /** owner → the people in the owner's contact list. */
    private val contacts = HashMap<String, MutableSet<String>>()

    // ───────────── API ─────────────

    /** Step 1 — the app says "I'm here" (replaces the older time). */
    fun heartbeat(user: String, now: Long) {
        TODO("Step 1")
    }

    /** Step 2 */
    fun setVisibility(user: String, visibility: Visibility) {
        TODO("Step 2")
    }

    /** Step 2 — put [contact] into [owner]'s contact list (one direction only). */
    fun addContact(owner: String, contact: String) {
        TODO("Step 2")
    }

    /**
     * Step 2 — may [viewer] see [target]'s presence?
     * - Yourself → always yes.
     * - Reciprocity: if the VIEWER's own setting is NOBODY, they can't see anyone else.
     * - Then the TARGET's setting: EVERYONE → yes; CONTACTS → only if the viewer is in the target's contact list; NOBODY → no.
     */
    private fun canSee(viewer: String, target: String): Boolean {
        TODO("Step 2")
    }

    /**
     * Steps 1–2 — what [viewer] sees about [target], checked in this order:
     * 1. not allowed (step 2) → `"hidden"`
     * 2. target never sent a heartbeat → `"unknown"`
     * 3. last heartbeat less than `onlineTimeoutSeconds` ago → `"online"`
     * 4. otherwise → `"last seen <time of last heartbeat>"`, e.g. `"last seen 120"`
     */
    fun view(viewer: String, target: String, now: Long): String {
        TODO("Step 1")
    }
}
