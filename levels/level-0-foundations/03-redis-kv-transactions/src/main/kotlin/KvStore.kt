class KvStore {

    // ───────────── State ─────────────

    /** The committed data (what you see with no transaction open). */
    private val data = HashMap<String, String>()

    /**
     * One layer per open transaction — the last one is the innermost.
     * A layer holds only the changes made inside that transaction:
     *   key → new value, or key → `null` meaning "deleted in this transaction".
     */
    private val layers = ArrayList<HashMap<String, String?>>()

    // ───────────── Reads and writes ─────────────

    /** Step 1/2 — no transaction: write into `data`. Inside a transaction: write into the top layer only. */
    fun set(key: String, value: String) {
        TODO("Step 1")
    }

    /**
     * Step 1/2 — look in the layers from the top (innermost) down; the first layer that *contains the key* wins
     * (even if its value is `null` = deleted). If no layer has it, look in `data`. Missing → `null`.
     */
    fun get(key: String): String? {
        TODO("Step 1")
    }

    /**
     * Step 1/4 — remove [key]. Deleting a key that doesn't exist does nothing.
     * No transaction: remove from `data`. Inside one: put `key → null` in the top layer (don't touch `data`!).
     */
    fun delete(key: String) {
        TODO("Step 1")
    }

    /** Step 5 — how many keys currently have [value], as `get` would see them (layers included, deleted keys excluded). */
    fun count(value: String): Int {
        TODO("Step 5")
    }

    // ───────────── Transactions ─────────────

    /** Step 2 — open a new (possibly nested) transaction: push an empty layer. */
    fun begin() {
        TODO("Step 2")
    }

    /**
     * Step 3 — close the innermost transaction and keep its changes.
     * - No transaction open → return `false`.
     * - Pop the top layer. If another layer is still open, copy the changes into it;
     *   otherwise apply them to `data` (a `null` value means remove the key). Return `true`.
     */
    fun commit(): Boolean {
        TODO("Step 3")
    }

    /** Step 2 — close the innermost transaction and throw its changes away. No transaction open → `false`. */
    fun rollback(): Boolean {
        TODO("Step 2")
    }
}
