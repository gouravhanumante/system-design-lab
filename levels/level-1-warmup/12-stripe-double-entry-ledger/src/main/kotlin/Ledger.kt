class Ledger {

    fun openAccount(id: String, allowNegative: Boolean = false) {
        TODO()
    }

    /** True if the money moved. Same txId again -> same result, nothing new written. */
    fun transfer(txId: String, from: String, to: String, amountPaise: Long): Boolean {
        TODO()
    }

    fun balance(id: String): Long {
        TODO()
    }

    /** "-500 t1" / "+500 t1" in the order written. */
    fun entries(id: String): List<String> {
        TODO()
    }

    fun sumOfBalances(): Long {
        TODO()
    }
}
