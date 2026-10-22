# Concepts — Double-entry ledger

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Double-entry bookkeeping

Every transaction is recorded as at least two **entries** that cancel out: money leaves one account and arrives in another. The sum of all entries is always zero. Accountants have used this for 500 years because mistakes become visible immediately.

## Invariants as self-checks

"Sum of all balances is zero" is an **invariant** you can check at any time. In production, a background job verifying invariants catches bugs (lost or duplicated money) before customers do.

## Derived balances

A balance is the sum of an account's entries. Storing entries (append-only) and **deriving** balances means the history explains every rupee. For speed, real systems also cache the balance, but it must be updated in the **same transaction** as the entries.

## Idempotent transfers

A transaction id identifies a transfer. Repeating a request with the same id returns the first result instead of moving money again — essential when networks drop responses.

## Corrections, not deletions

Wrong entries are never edited or deleted. A new **reversing** entry cancels the mistake, keeping a truthful history. Ledgers are append-only, like logs.
