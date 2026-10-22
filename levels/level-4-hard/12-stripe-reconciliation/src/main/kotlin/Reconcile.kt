data class LedgerTxn(val id: String, val amountPaise: Long, val day: Int)
data class BankLine(val ref: String, val amountPaise: Long, val day: Int)

data class Report(
    val matched: List<Pair<String, String>>,
    val amountMismatch: List<Pair<String, String>>,
    val missingInBank: List<String>,
    val missingInLedger: List<BankLine>,
)

fun reconcile(ledger: List<LedgerTxn>, bank: List<BankLine>, dayTolerance: Int = 2): Report {
    TODO()
}
