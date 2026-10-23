# Consumer groups, commits and replay

| App | Level | Concept |
|-----|-------|---------|
| Kafka | 3 — Realistic | Committed offsets, at-least-once delivery, lag |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Kafka does not delete a message when you read it — many independent **consumer groups** can read the same topic at their own pace. Each group remembers, per partition, how far it has **committed**. If a consumer crashes after reading but *before* committing, it restarts from the last commit and sees some messages **again** — that is **at-least-once** delivery. The gap between the end of the log and the group's commit is called **lag**.

## Build this (small scale)

Consumer-group bookkeeping on top of a topic (the `Topic` class is **provided** in the starter file).

## API

```kotlin
class ConsumerGroups(topic: Topic) {
    fun poll(group: String, partition: Int, maxRecords: Int): List<Record>
    fun commit(group: String, partition: Int)
    fun restart(group: String)                       // simulates a crash + restart of the group's consumers
    fun seek(group: String, partition: Int, offset: Long)
    fun committed(group: String, partition: Int): Long
    fun lag(group: String, partition: Int): Long
}
```

## Rules

- For each `(group, partition)` there are two numbers, both starting at `0`:
  - **position** — the next offset `poll` will return,
  - **committed** — the offset the group has durably saved.
- `poll` returns up to `maxRecords` records starting at the position and **advances the position** by the number returned. Nothing is committed automatically.
- `commit(group, partition)` sets committed := position for that partition.
- `restart(group)` resets the **position** of every partition of that group back to its **committed** offset (unread/uncommitted messages will be delivered again).
- `seek(group, partition, offset)` sets the position to `offset` (to replay or skip). It must satisfy `0 <= offset <= endOffset(partition)`, otherwise `IllegalArgumentException`.
- `committed` returns the committed offset. `lag = endOffset(partition) − committed`.
- Different groups never influence each other: each group gets every message.

## Example

Partition has 5 messages. Group `g`: `poll(…, 3)` → offsets 0–2, `commit`, `poll(…, 2)` → offsets 3–4, **crash** (`restart("g")`), `poll(…, 10)` → offsets 3–4 again.

## Think about (answer in NOTES.md)

- If the consumer commits **before** processing each message, what delivery guarantee do you get? What about committing **after**? Which would you pick for "charge the customer"?
- How would you make processing **idempotent** so that duplicates are harmless?
- Who stores committed offsets in a real cluster, and why not on the consumer?

---

## Run the tests

```bash
./gradlew :l3-kafka-consumer-offsets:test
```

Your code goes in `src/main/kotlin/ConsumerGroups.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
