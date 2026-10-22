import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CartTest {

    private fun sample(): Cart {
        val c = Cart()
        c.addItem("BOOK", 49_900, 2)
        c.addItem("PEN", 2_050, 3)
        return c
    }

    @Test
    fun subtotalAndLines() {
        val c = sample()
        assertEquals(105_950L, c.subtotalPaise())
        assertEquals(listOf("BOOK x2 @ 49900 = 99800", "PEN x3 @ 2050 = 6150"), c.lines())
        assertEquals(105_950L, c.totalPaise())
    }

    @Test
    fun addingAgainIncreasesQuantityAndKeepsOrder() {
        val c = sample()
        c.addItem("BOOK", 49_900)
        assertEquals(listOf("BOOK x3 @ 49900 = 149700", "PEN x3 @ 2050 = 6150"), c.lines())
    }

    @Test
    fun couponIsCappedAndRoundsDown() {
        val c = sample()
        c.applyCoupon(10, 10_000)
        assertEquals(10_000L, c.discountPaise())
        assertEquals(95_950L, c.totalPaise())
        c.applyCoupon(3, 1_000_000)                  // replaces: 3 % of 105950 = 3178.5 -> 3178
        assertEquals(3_178L, c.discountPaise())
        c.removeCoupon()
        assertEquals(0L, c.discountPaise())
    }

    @Test
    fun hundredPercentCouponWithBigCap() {
        val c = sample()
        c.applyCoupon(100, Long.MAX_VALUE)
        assertEquals(0L, c.totalPaise())
    }

    @Test
    fun setQuantityAndRemove() {
        val c = sample()
        c.setQuantity("PEN", 1)
        assertEquals(101_850L, c.subtotalPaise())
        c.setQuantity("PEN", 0)
        assertEquals(listOf("BOOK x2 @ 49900 = 99800"), c.lines())
        c.remove("BOOK"); c.remove("NOTHING")
        assertEquals(0L, c.subtotalPaise())
        assertEquals(emptyList(), c.lines())
    }

    @Test
    fun quantityLimitIsAllOrNothing() {
        val c = Cart(maxQuantityPerItem = 5)
        c.addItem("X", 100, 4)
        assertFailsWith<IllegalArgumentException> { c.addItem("X", 100, 2) }
        assertEquals(listOf("X x4 @ 100 = 400"), c.lines())
        assertFailsWith<IllegalArgumentException> { c.setQuantity("X", 6) }
        assertFailsWith<IllegalArgumentException> { c.addItem("Y", 100, 6) }
        assertEquals(listOf("X x4 @ 100 = 400"), c.lines())
    }

    @Test
    fun invalidInputs() {
        val c = sample()
        assertFailsWith<IllegalArgumentException> { c.addItem("BOOK", 39_900) }        // price mismatch
        assertFailsWith<IllegalArgumentException> { c.addItem("Z", 0) }
        assertFailsWith<IllegalArgumentException> { c.addItem("Z", 100, 0) }
        assertFailsWith<IllegalArgumentException> { c.setQuantity("GHOST", 1) }
        assertFailsWith<IllegalArgumentException> { c.setQuantity("PEN", -1) }
        assertFailsWith<IllegalArgumentException> { c.applyCoupon(0, 100) }
        assertFailsWith<IllegalArgumentException> { c.applyCoupon(101, 100) }
        assertFailsWith<IllegalArgumentException> { c.applyCoupon(10, -1) }
        assertEquals(105_950L, c.totalPaise())
    }

    @Test
    fun noFloatingPointDrift() {
        val c = Cart(maxQuantityPerItem = 10)
        repeat(10) { c.addItem("SKU$it", 10, 10) }                                     // 100 x 0.10 rupees
        assertEquals(1_000L, c.subtotalPaise())
    }
}
