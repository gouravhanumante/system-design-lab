class Inventory(private val holdSeconds: Long = 600) {

    fun addStock(sku: String, quantity: Int) {
        TODO()
    }

    fun hold(orderId: String, sku: String, quantity: Int, now: Long): Boolean {
        TODO()
    }

    fun confirm(orderId: String, now: Long): Boolean {
        TODO()
    }

    fun release(orderId: String) {
        TODO()
    }

    fun available(sku: String, now: Long): Int {
        TODO()
    }

    fun sold(sku: String): Int {
        TODO()
    }
}
