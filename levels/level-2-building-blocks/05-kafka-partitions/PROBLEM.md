# Partitions and key-based routing

| App | Level | Concept |
|-----|-------|---------|
| Kafka | 2 — Building blocks | Hash partitioning, per-partition ordering, round-robin |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

One log on one machine caps your throughput. Kafka splits a **topic** into several **partitions**, each its own log, spread over many machines. The producer's **key** decides the partition: all events for `user-42` land in the same partition, so they stay **in order**. Messages without a key are spread evenly.

## Build this (small scale)

A topic with a fixed number of partitions.

## API

```kotlin
data class Record(val partition: Int, val offset: Long, val key: String?, val value: String)

class Topic(partitions: Int) {
    fun produce(key: String?, value: String): Record
    fun read(partition: Int, fromOffset: Long, maxRecords: Int): List<Record>
    fun endOffset(partition: Int): Long
}
```

## Rules

- Partition numbers are `0 until partitions`. Each partition has its own offsets starting at `0`.
- **Keyed** messages: `partition = Math.floorMod(key.hashCode(), partitions)`. The same key always goes to the same partition.
- **Unkeyed** (`key == null`) messages are spread **round-robin**: the 1st goes to partition 0, the 2nd to 1, … then back to 0. Only unkeyed messages advance this counter.
- `produce` returns the stored `Record` (with its partition and offset).
- `read` works like a log: from `fromOffset` (inclusive), up to `maxRecords`, empty if at/after the end; negative offset → `IllegalArgumentException`.
- An invalid partition number (in `read` or `endOffset`) → `IllegalArgumentException`.

## Example

3 partitions. `produce("user-1", …)` always lands in the same partition; if `"user-1".hashCode()` is 1,000,000 the partition is `floorMod(1000000, 3) = 1`.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`choosePartition()`** — key hash, or round robin when there's no key. 🎯 `keyedMessagesGoToHashPartition`, `negativeHashCodesStillMapToAValidPartition`, `unkeyedMessagesAreRoundRobin`, `keyedMessagesDoNotAdvanceTheRoundRobinCounter` (with step 2)
2. **`produce()`**. 🎯 `sameKeyAlwaysSamePartitionAndOrderIsKept`, `partitionsHaveIndependentOffsets`
3. **`requireValidPartition()`**, **`read()`**, **`endOffset()`**. 🎯 `readBehavesLikeALogPerPartition`, `invalidPartitionsAreRejected`

## Think about (answer in NOTES.md)

- Ordering is guaranteed **inside** a partition only. What does that mean for a consumer that reads two partitions?
- What happens to key → partition mapping if you add a 4th partition to a 3-partition topic? Why do people say "choose the partition count carefully up front"?
- A single key is extremely hot (a celebrity's user id). What goes wrong, and what can you do?

---

## Run the tests

```bash
./gradlew :l2-kafka-partitions:test
```

Your code goes in `src/main/kotlin/Topic.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
