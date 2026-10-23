# Shopping cart with money in paise

| App | Level | Concept |
|-----|-------|---------|
| Amazon | 1 — Warm-up | Integer money, validation rules, coupon caps |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

A cart looks trivial until money is involved. Prices must be exact (no `0.1 + 0.2` surprises), quantities have limits, and a "20% off, up to ₹200" coupon needs an exact rounding rule. Every big store stores money as an **integer number of the smallest unit** — paise, cents.

## Build this (small scale)

A cart that keeps prices in paise (`Long`).

## API

```kotlin
class Cart(maxQuantityPerItem: Int = 10) {
    fun addItem(sku: String, unitPricePaise: Long, quantity: Int = 1)
    fun setQuantity(sku: String, quantity: Int)
    fun remove(sku: String)
    fun applyCoupon(percentOff: Int, maxDiscountPaise: Long)
    fun removeCoupon()
    fun subtotalPaise(): Long
    fun discountPaise(): Long
    fun totalPaise(): Long
    fun lines(): List<String>
}
```

## Rules

- `addItem`: `quantity >= 1` and `unitPricePaise > 0`, else `IllegalArgumentException`. Adding a SKU already in the cart **adds** to its quantity; its price must equal the stored price (else `IllegalArgumentException`). If the resulting quantity would exceed `maxQuantityPerItem` → `IllegalArgumentException` and **nothing** changes.
- `setQuantity`: unknown SKU → `IllegalArgumentException`. `0` removes the line. Negative or above the max → `IllegalArgumentException`.
- `remove` of an unknown SKU does nothing.
- `applyCoupon`: `percentOff` in `1..100` and `maxDiscountPaise >= 0`, else `IllegalArgumentException`. Only one coupon: applying another replaces the previous one.
- `discountPaise` = `min(subtotal × percentOff / 100 (rounded down), maxDiscountPaise)`, or `0` with no coupon.
- `totalPaise` = subtotal − discount.
- `lines()` in the order SKUs were first added: `"<sku> x<qty> @ <unitPrice> = <lineTotal>"`, e.g. `"BOOK x2 @ 49900 = 99800"`.

## Example

`BOOK` 2 × ₹499 + `PEN` 3 × ₹20.50 = 99,800 + 6,150 = 105,950 paise. Coupon 10%, max ₹100 → discount `min(10,595, 10,000)` = 10,000. Total 95,950.

## Think about (answer in NOTES.md)

- Why is `Double` a bad type for money? Find a concrete example where it gives a wrong total.
- The price of an item changes while it sits in your cart for a week. Which price should checkout use? What does Amazon show you?
- Where should the cart live for a logged-out user? For a logged-in user on two devices?

---

## Run the tests

```bash
./gradlew :l1-amazon-shopping-cart:test
```

Your code goes in `src/main/kotlin/Cart.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
