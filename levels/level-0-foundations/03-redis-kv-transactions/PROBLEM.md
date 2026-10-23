# Key-value store with transactions

| App | Level | Concept |
|-----|-------|---------|
| Redis | 0 — Foundations | Transactions, atomicity, rollback, nested layers of changes |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Databases promise that a group of changes either **all** happen or **none** do (atomicity). "Move ₹500: subtract from A, add to B" must never stop halfway. Internally, uncommitted changes are kept *aside* and only applied on **commit**; **rollback** throws them away. This classic interview exercise builds that idea on top of a plain key-value store (Redis has `MULTI/EXEC`; SQL has `BEGIN/COMMIT/ROLLBACK`).

## Build this (small scale)

An in-memory key-value store with nested transactions.

## API

```kotlin
class KvStore {
    fun set(key: String, value: String)
    fun get(key: String): String?
    fun delete(key: String)
    fun count(value: String): Int
    fun begin()
    fun commit(): Boolean
    fun rollback(): Boolean
}
```

## Rules

- Outside a transaction, changes apply directly.
- `begin` opens a transaction; transactions can be **nested** (begin inside begin).
- Inside a transaction, `set`/`delete` only affect that transaction. `get` and `count` always show the store **as seen from the innermost transaction** (its changes, then its parent's, …, then the committed data).
- `commit` merges the **innermost** transaction's changes into its parent (or into the committed data if it was the outermost) and closes it → `true`. `rollback` discards the innermost transaction → `true`. With no open transaction both return `false`.
- `delete` of a key that does not exist (as seen from the current transaction) does nothing.
- `count(value)` = number of keys whose current value equals `value`.

## Example

```
set a 10
begin
  set a 20
  begin
    delete a      -> get a = null
  rollback        -> get a = 20
commit            -> get a = 20 (committed)
```

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`set` / `get` / `delete` with no transaction** (just use `data`). 🎯 `plainSetGetDelete`
2. **`begin` + `rollback`**: start writing into a new layer, and throw it away on rollback. Update `set`/`get`/`delete` to use the top layer when there is one. 🎯 `rollbackDiscardsChanges`, `noTransactionToCloseReturnsFalse`
3. **`commit`**: merge the top layer down. 🎯 `commitKeepsChanges`, `nestedTransactions`, `innerCommitStillRollsBackWithTheOuter`
4. **Deletes inside a transaction** (store a `null` marker). 🎯 `deleteInsideATransactionHidesCommittedValueUntilRollback`
5. **`count`** — count over the *current view*. 🎯 `countSeesTheCurrentView`

## Think about (answer in NOTES.md)

- What would **isolation** mean if two users had their own transactions at the same time? Which of them sees which changes?
- Real databases write changes to a **write-ahead log** before applying them. What does that protect against that your in-memory store can't?
- `count` walks all keys. How would you make it O(1) — and what does that cost inside nested transactions?

---

## Run the tests

```bash
./gradlew :l0-redis-kv-transactions:test
```

Your code goes in `src/main/kotlin/KvStore.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
