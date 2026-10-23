data class LedgerTxn(val id: String, val amountPaise: Long, val day: Int)
data class BankLine(val ref: String, val amountPaise: Long, val day: Int)

data class Report(
    val matched: List<Pair<String, String>>,
    val amountMismatch: List<Pair<String, String>>,
    val missingInBank: List<String>,
    val missingInLedger: List<BankLine>,
)

/** Working state while reconciling. A bank line is identified by its INDEX in the bank list (refs can repeat). */
private class Work(val bank: List<BankLine>) {
    val bankUsed = BooleanArray(bank.size)
    val ledgerUsed = HashSet<String>()
    val matched = ArrayList<Pair<String, String>>()       // (ledger id, bank ref)
    val mismatched = ArrayList<Pair<String, String>>()    // (ledger id, bank ref)
}

/**
 * Pass 1 (Step 1) — for each ledger txn in list order: find the FIRST unused bank line whose `ref` equals the txn id.
 * Found → mark both used; same amount → matched, different amount → mismatched.
 */
private fun matchByReference(ledger: List<LedgerTxn>, w: Work) {
    TODO("Step 1")
}

/**
 * Pass 2 (Step 2) — for ledger txns still unused, sorted by day then id: among unused bank lines with the SAME amount
 * and `|bank.day - txn.day| <= dayTolerance`, pick the closest day; tie → earlier day; tie → earlier in the bank list.
 * Found → mark both used, add to matched.
 */
private fun matchFuzzy(ledger: List<LedgerTxn>, w: Work, dayTolerance: Int) {
    TODO("Step 2")
}

/**
 * Steps 1–3 — duplicate ledger ids → `IllegalArgumentException`. Run pass 1, then pass 2, then build the report:
 * matched and amountMismatch sorted by ledger id · missingInBank = unused ledger ids sorted ·
 * missingInLedger = unused bank lines in their original order.
 */
fun reconcile(ledger: List<LedgerTxn>, bank: List<BankLine>, dayTolerance: Int = 2): Report {
    TODO("Step 3")
}
