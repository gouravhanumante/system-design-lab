class Cart(private val maxQuantityPerItem: Int = 10) {

    fun addItem(sku: String, unitPricePaise: Long, quantity: Int = 1) {
        TODO()
    }

    fun setQuantity(sku: String, quantity: Int) {
        TODO()
    }

    fun remove(sku: String) {
        TODO()
    }

    fun applyCoupon(percentOff: Int, maxDiscountPaise: Long) {
        TODO()
    }

    fun removeCoupon() {
        TODO()
    }

    fun subtotalPaise(): Long {
        TODO()
    }

    fun discountPaise(): Long {
        TODO()
    }

    fun totalPaise(): Long {
        TODO()
    }

    /** "<sku> x<qty> @ <unitPrice> = <lineTotal>" in the order SKUs were first added. */
    fun lines(): List<String> {
        TODO()
    }
}
