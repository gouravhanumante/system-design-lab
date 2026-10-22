# Reconciling the ledger with the bank statement

| App | Level | Concept |
|-----|-------|---------|
| Stripe | 4 — Hard | Multi-pass record matching, tolerance windows, exception reports |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Every night, payment companies compare **their** records with the **bank's** statement. Ideally every internal transaction has a bank line with the same reference and amount. In reality references go missing, amounts differ (fees!), and settlement happens a day or two late. **Reconciliation** matches what it can — exact matches first, then fuzzy ones — and produces an **exception report** for humans.

## Build this (small scale)

A function that reconciles two lists and reports the result.

## API

```kotlin
data class LedgerTxn(val id: String, val amountPaise: Long, val day: Int)
data class BankLine(val ref: String, val amountPaise: Long, val day: Int)     // ref may be ""

data class Report(
    val matched: List<Pair<String, String>>,          // (ledger id, bank ref)
    val amountMismatch: List<Pair<String, String>>,   // (ledger id, bank ref)
    val missingInBank: List<String>,                  // ledger ids
    val missingInLedger: List<BankLine>,
)

fun reconcile(ledger: List<LedgerTxn>, bank: List<BankLine>, dayTolerance: Int = 2): Report
```

## Rules

Each ledger transaction and each bank line can be used **at most once**. Duplicate ledger ids → `IllegalArgumentException`.

1. **Pass 1 — by reference.** For each ledger txn, the first unused bank line (in bank-list order) whose `ref == id`:
   - same amount → `matched`;
   - different amount → `amountMismatch`.
2. **Pass 2 — fuzzy.** For the remaining ledger txns, in order of `(day, id)`: among unused bank lines with the **same amount** and `|bank.day − txn.day| <= dayTolerance`, pick the one with the smallest day difference, then the smallest bank day, then the earliest position in the bank list → `matched`.
3. **Leftovers**: unused ledger ids → `missingInBank`; unused bank lines → `missingInLedger`.

Ordering in the report: `matched` and `amountMismatch` sorted by ledger id; `missingInBank` sorted; `missingInLedger` in bank-list order.

## Example

Ledger `t1 ₹100 day 1`, `t2 ₹250 day 2`. Bank `t1 ₹100 day 2`, `"" ₹250 day 4`. → `t1` matches by reference; `t2` matches the reference-less line by amount (2 days late is within tolerance).

## Think about (answer in NOTES.md)

- Fuzzy matching can pair the **wrong** transactions (two ₹250 payments on the same day). How would you lower that risk, and what should the report show?
- Banks deduct fees: ₹1,000 charged, ₹980 settled. How would you model expected fees so these still match?
- Real reconciliation runs over millions of rows. Which part of your algorithm is slow, and how would you index the bank lines?

---

## Run the tests

```bash
./gradlew :l4-stripe-reconciliation:test
```

Your code goes in `src/main/kotlin/Reconcile.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
