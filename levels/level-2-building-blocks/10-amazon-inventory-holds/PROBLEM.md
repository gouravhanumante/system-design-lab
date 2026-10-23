# Inventory holds during checkout

| App | Level | Concept |
|-----|-------|---------|
| Amazon | 2 — Building blocks | Reservations with expiry, preventing overselling, idempotent confirm |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

The last PS5 in stock: two people click "Buy" at the same time. Without care, both pay and one order can't be shipped (**overselling**). Stores **hold** stock while you go through checkout — but only for a few minutes, otherwise abandoned checkouts would lock stock forever.

## Build this (small scale)

Stock per SKU, temporary holds that expire, and confirmation that turns a hold into a sale. Time is passed in (`now`, seconds).

## API

```kotlin
class Inventory(holdSeconds: Long = 600) {
    fun addStock(sku: String, quantity: Int)
    fun hold(orderId: String, sku: String, quantity: Int, now: Long): Boolean
    fun confirm(orderId: String, now: Long): Boolean
    fun release(orderId: String)
    fun available(sku: String, now: Long): Int
    fun sold(sku: String): Int
}
```

## Rules

- `available = stock − (quantity of active holds) − sold`. Unknown SKU → `0`.
- A hold created at `t` is **active** until `t + holdSeconds` (at exactly that time it has expired).
- `hold`: `quantity >= 1` else `IllegalArgumentException`.
  - If `orderId` already has an **active** hold with the same SKU and quantity → return `true` (idempotent retry, nothing changes). An existing hold for that order with a different SKU/quantity, or one that was confirmed → `IllegalArgumentException`.
  - Otherwise, if `available >= quantity` create the hold (replacing any expired/released hold of that order) and return `true`; else return `false`.
- `confirm`: active hold → it becomes a sale (`sold` increases, stock is no longer held) → `true`. Already confirmed → `true` (idempotent). Expired, released or unknown → `false`.
- `release` cancels an active hold (unknown / confirmed / expired: nothing happens).
- `addStock` adds to the SKU's stock (`quantity >= 1` else `IllegalArgumentException`).

## Example

Stock 1. Order A holds 1 at `t = 0` → available 0, so B's hold fails. A abandons; at `t = 600` the hold has expired → available 1, B succeeds. A tries `confirm` at 601 → `false`.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`addStock()`**, **`isActive()`**, **`available()`**, **`hold()`** (new orders only). 🎯 `holdReducesAvailability`, `cannotOversell`, `holdsExpireExactlyOnTime`
2. **`confirm()`** + **`sold()`**. 🎯 `confirmTurnsHoldIntoSaleAndIsIdempotent`
3. **`release()`**. 🎯 `releaseReturnsStock`
4. **Repeated holds** for the same order in `hold()`. 🎯 `repeatedHoldIsIdempotentButConflictingHoldIsAnError`, `anOrderCanHoldAgainAfterItsHoldExpiredOrWasReleased`, `stockCanBeAddedLaterAndInputsAreValidated`

## Think about (answer in NOTES.md)

- In a real database, two servers check `available >= 1` at the same moment. How do you make check-and-hold **atomic**? (Row lock? `UPDATE … WHERE available >= 1`? Optimistic version check?)
- Expired holds: clean them up eagerly with a job, or lazily when computing availability? Which does your code do?
- A flash sale with 1,000,000 people and 10,000 units. What would you put in front of the inventory service?

---

## Run the tests

```bash
./gradlew :l2-amazon-inventory-holds:test
```

Your code goes in `src/main/kotlin/Inventory.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
