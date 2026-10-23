# Double-entry ledger

| App | Level | Concept |
|-----|-------|---------|
| Stripe | 1 — Warm-up | Double-entry bookkeeping, invariants, idempotent transactions |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Payment companies (Stripe, Razorpay, Paytm, every bank) never just "set a balance". Every movement of money is written **twice**: a debit from one account and a credit to another, with the same transaction id. Money is never created or destroyed inside the system, so the sum of all balances is always **zero** — a powerful self-check that catches bugs instantly. (Money coming from outside enters through an account that is allowed to go negative, like "bank" or "world".)

## Build this (small scale)

A ledger of accounts and transfers in paise.

## API

```kotlin
class Ledger {
    fun openAccount(id: String, allowNegative: Boolean = false)
    fun transfer(txId: String, from: String, to: String, amountPaise: Long): Boolean
    fun balance(id: String): Long
    fun entries(id: String): List<String>
    fun sumOfBalances(): Long
}
```

## Rules

- `openAccount`: duplicate id → `IllegalArgumentException`. New accounts start at 0.
- `transfer`: `amountPaise > 0`, both accounts must exist, and `from != to` — else `IllegalArgumentException` (and nothing is recorded).
  - If `txId` was used before, return the **same result as the first time** and change nothing (idempotent retry — even if the arguments differ or the balance changed meanwhile).
  - If `from` is not allowed to go negative and `balance(from) < amountPaise` → return `false` (and remember that `txId` failed).
  - Otherwise write **two entries**: `-<amount> <txId>` on `from` and `+<amount> <txId>` on `to`, and return `true`.
- `balance` = the sum of the account's entries. Unknown account → `IllegalArgumentException`.
- `entries(id)` lists the account's entries in the order written.

## Example

`world` (may go negative), `alice`, `bob`. `transfer("t1", world, alice, 500)` → alice 500, world −500. `transfer("t2", alice, bob, 800)` → `false`. Sum of balances: always 0.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`openAccount()`**, **`accountOrThrow()`**, **`balance()`**, **`entries()`**, **`sumOfBalances()`**.
2. **`transfer()`** — two entries per transfer. 🎯 `moneyMovesWithTwoEntries`, `insufficientFundsIsRefusedWithoutEntries`, `invalidTransfersThrowAndRecordNothing`, `sumIsAlwaysZero`
3. **Idempotency** by `txId`. 🎯 `retriesReturnTheOriginalResult`

## Think about (answer in NOTES.md)

- Why derive the balance from entries instead of storing a `balance` number? When *would* you also store one (and how keep it correct)?
- An entry was written by mistake. Why do accountants never delete it, and what do they do instead?
- How does the "sum is zero" invariant help you detect bugs in production?

---

## Run the tests

```bash
./gradlew :l1-stripe-double-entry-ledger:test
```

Your code goes in `src/main/kotlin/Ledger.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
