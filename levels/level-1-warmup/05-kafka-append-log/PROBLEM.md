# The append-only log

| App | Level | Concept |
|-----|-------|---------|
| Kafka | 1 — Warm-up | Append-only log, offsets, reading from a position |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Apache Kafka looks complicated from the outside, but its heart is one idea: a **log** — a list you can only add to at the end. Every message gets a number (its **offset**), and readers simply say "give me everything from offset 17". Nothing is ever updated in place.

## Build this (small scale)

The smallest possible Kafka: one append-only log kept in memory.

## API

```kotlin
data class Record(val offset: Long, val value: String)

class AppendLog {
    fun append(value: String): Long                               // returns the offset of the new record
    fun read(fromOffset: Long, maxRecords: Int): List<Record>
    fun endOffset(): Long                                         // offset the NEXT append will get
}
```

## Rules

- Offsets start at `0` and go up by one per append.
- `read` returns up to `maxRecords` records starting at `fromOffset` (inclusive), in offset order.
- `fromOffset >= endOffset()` → empty list (not an error: the reader is simply caught up). `fromOffset < 0` → `IllegalArgumentException`. `maxRecords <= 0` → empty list.
- Records never change after they are written.

## Example

Append `"a"`, `"b"`, `"c"` → offsets 0, 1, 2. `read(1, 5)` → records at offsets 1 and 2. `endOffset()` is 3.

## Think about (answer in NOTES.md)

- Why is an append-only log so fast on a real disk compared with updating rows in a database?
- Memory is limited, disks too. How would you delete *old* data without breaking readers who say "from offset 17"? (Look up log segments and retention.)

---

## Run the tests

```bash
./gradlew :l1-kafka-append-log:test
```

Your code goes in `src/main/kotlin/AppendLog.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
