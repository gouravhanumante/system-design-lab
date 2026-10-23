class Ledger {

    // ───────────── State ─────────────

    /** An account never stores a "balance" field — the balance is the SUM of its entries. That's what makes it auditable. */
    private class Account(val allowNegative: Boolean) {
        /** (amount in paise: negative = money out, positive = money in, txId), in the order written. */
        val entries = ArrayList<Pair<Long, String>>()
    }

    /** account id → account. */
    private val accounts = HashMap<String, Account>()

    /** txId → the result it had the first time (`true` moved, `false` refused). */
    private val results = HashMap<String, Boolean>()

    // ───────────── Helpers ─────────────

    /** Step 1 — the account, or `IllegalArgumentException`. */
    private fun accountOrThrow(id: String): Account {
        TODO("Step 1")
    }

    // ───────────── API ─────────────

    /** Step 1 — new account. Id already exists → `IllegalArgumentException`. */
    fun openAccount(id: String, allowNegative: Boolean = false) {
        TODO("Step 1")
    }

    /**
     * Steps 2–3 — move money. Check in this order:
     * 1. amount ≤ 0, or from == to, or an unknown account → `IllegalArgumentException` (nothing recorded).
     * 2. Step 3: this txId was seen before → return its stored result, write NOTHING new.
     * 3. `from` can't go negative and its balance < amount → remember `false` for this txId, return `false` (no entries).
     * 4. Otherwise write TWO entries: `-amount` on `from`, `+amount` on `to` (same txId); remember `true`; return `true`.
     */
    fun transfer(txId: String, from: String, to: String, amountPaise: Long): Boolean {
        TODO("Step 2")
    }

    /** Step 1 — sum of the account's entries (unknown → `IllegalArgumentException`). */
    fun balance(id: String): Long {
        TODO("Step 1")
    }

    /** Step 1 — `"-500 t1"` / `"+500 t1"` (sign always shown), in the order written. */
    fun entries(id: String): List<String> {
        TODO("Step 1")
    }

    /** Step 1 — sum of every account's balance. In double-entry bookkeeping this is always 0. */
    fun sumOfBalances(): Long {
        TODO("Step 1")
    }
}
