class Cart(private val maxQuantityPerItem: Int = 10) {

    // ───────────── State ─────────────
    // Money is always a Long number of paise (₹1 = 100 paise) — never Double, so there's no rounding drift.

    /** One cart line: the unit price and how many. */
    private class Line(val unitPricePaise: Long, var quantity: Int)

    /** sku → line, in the order SKUs were first added. */
    private val cartLines = LinkedHashMap<String, Line>()

    /** The applied coupon as (percentOff, maxDiscountPaise), or `null`. */
    private var coupon: Pair<Int, Long>? = null

    // ───────────── Items ─────────────

    /**
     * Steps 1 + 4 — add [quantity] of [sku]. If it's already in the cart, increase its quantity (keeps its position).
     * `IllegalArgumentException` (and change NOTHING) if: quantity < 1 · price ≤ 0 · the price differs from the price
     * already in the cart for this sku · the new total quantity would be above [maxQuantityPerItem].
     */
    fun addItem(sku: String, unitPricePaise: Long, quantity: Int = 1) {
        TODO("Step 1")
    }

    /**
     * Step 2 — set the quantity exactly. 0 removes the line. Unknown sku, or quantity outside `0..maxQuantityPerItem`
     * → `IllegalArgumentException`.
     */
    fun setQuantity(sku: String, quantity: Int) {
        TODO("Step 2")
    }

    /** Step 2 — remove the line (unknown sku: do nothing). */
    fun remove(sku: String) {
        TODO("Step 2")
    }

    // ───────────── Coupon ─────────────

    /** Step 3 — set (or replace) the coupon. percentOff outside 1..100 or a negative cap → `IllegalArgumentException`. */
    fun applyCoupon(percentOff: Int, maxDiscountPaise: Long) {
        TODO("Step 3")
    }

    /** Step 3 */
    fun removeCoupon() {
        TODO("Step 3")
    }

    // ───────────── Totals ─────────────

    /** Step 1 — sum of unitPrice × quantity over all lines. */
    fun subtotalPaise(): Long {
        TODO("Step 1")
    }

    /** Step 3 — no coupon → 0. Otherwise `subtotal × percentOff / 100` (integer division = rounds down), but at most the cap. */
    fun discountPaise(): Long {
        TODO("Step 3")
    }

    /** Step 3 — subtotal − discount. */
    fun totalPaise(): Long {
        TODO("Step 3")
    }

    /** Step 1 — one string per line, in first-added order: `"<sku> x<qty> @ <unitPrice> = <lineTotal>"`, e.g. `"pen x3 @ 1000 = 3000"`. */
    fun lines(): List<String> {
        TODO("Step 1")
    }
}
