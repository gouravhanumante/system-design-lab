# Database indexes vs full table scans

| App | Level | Concept |
|-----|-------|---------|
| PostgreSQL | 0 — Foundations | Full scan, sorted (B-tree-like) index, hash index, binary search, index maintenance |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

`SELECT * FROM users WHERE age BETWEEN 25 AND 30` on 100 million rows: without an index the database reads **every row** (a full table scan). With an index on `age` — a sorted structure — it jumps straight to 25 and reads only the matching rows. Indexes make reads fast but make every write a bit slower, because each index must be updated too. Knowing **when an index helps** is one of the most practical backend skills.

## Build this (small scale)

A users "table" that can answer queries with or without indexes and reports how many rows it examined.

## API

```kotlin
class UsersTable {
    fun insert(id: Int, age: Int, city: String)
    fun delete(id: Int)
    fun createIndex(column: String)               // "age" (sorted) or "city" (hash)
    fun findByAge(minAge: Int, maxAge: Int): List<Int>
    fun findByCity(city: String): List<Int>
    val lastRowsExamined: Int
}
```

## Rules

- `insert`: duplicate id → `IllegalArgumentException`. `delete` of an unknown id does nothing.
- `createIndex("age")` builds a **sorted** index; `createIndex("city")` builds a **hash** index. Any other column → `IllegalArgumentException`. Creating an index twice does nothing. Indexes must stay correct for rows inserted or deleted **after** they were created.
- `findByAge(min, max)` returns ids with `min <= age <= max`, **sorted by id**. `findByCity` returns ids with exactly that city, sorted by id.
- `lastRowsExamined` (set by every find):
  - without a usable index: the number of rows in the table (full scan);
  - with an index: the number of index entries that **matched** (an index lets you go straight to them).

## Example

10,000 users, ages 18–80. `findByAge(25, 26)` without an index examines 10,000 rows; after `createIndex("age")` it examines only the ~300 matching ones.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`insert` + `delete`** into `rows` (no indexes yet). Duplicate id → exception.
2. **`findByAge` / `findByCity` without an index** — a full scan; set `lastRowsExamined` to the number of rows. 🎯 `withoutAnIndexEveryRowIsExamined`
3. **`createIndex("age")`** — build the sorted index from existing rows, and use it in `findByAge`. 🎯 `sortedIndexOnlyTouchesMatchingRows`, `rangeEdgesAreInclusiveAndEmptyRangesWork`
4. **`createIndex("city")`** — the hash index, used in `findByCity`. 🎯 `hashIndexForEquality`
5. **Keep indexes up to date** in `insert`/`delete` (via `addToIndexes`). 🎯 `indexesStayCorrectAfterInsertsAndDeletes`, `invalidOperations`

## Think about (answer in NOTES.md)

- Why can a **hash** index answer `city = 'Pune'` but not `age BETWEEN 25 AND 30`? Why can a sorted index do both?
- An index on `gender` (two values) on 100 million rows: is it useful? What is **selectivity**?
- Each index slows down writes. For a table with 50 writes per second and 50,000 reads per second, how many indexes would you accept? And the reverse?
- Look up the **B-tree**: why do databases use it instead of a sorted array?

---

## Run the tests

```bash
./gradlew :l0-postgresql-database-index:test
```

Your code goes in `src/main/kotlin/UsersTable.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
