# Work queue with acknowledgements

| App | Level | Concept |
|-----|-------|---------|
| Amazon SQS | 0 — Foundations | Message queues, visibility timeout, ack, redelivery, dead-letter queue |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

"Send the order-confirmation email" shouldn't slow down placing the order. Instead, the order service drops a **message** in a **queue** and a worker handles it later. If the worker crashes halfway, the message must not be lost. Amazon SQS solves this with a **visibility timeout**: a received message is hidden, not deleted. Only an explicit **ack** (delete) removes it. If no ack arrives in time, the message becomes visible again for another worker. A message that keeps failing (a "poison message") goes to a **dead-letter queue**.

This is different from Kafka (Level 1–5 Kafka problems): here each message is **consumed and deleted**; there is no log to replay.

## Build this (small scale)

A single queue with receive / ack / visibility timeout / dead-lettering. Time is passed in seconds.

## API

```kotlin
data class Message(val id: String, val body: String, val receipt: String, val receiveCount: Int)

class WorkQueue(visibilityTimeoutSeconds: Long = 30, maxReceives: Int = 3) {
    fun send(body: String): String
    fun receive(now: Long): Message?
    fun ack(receipt: String, now: Long): Boolean
    fun extendVisibility(receipt: String, extraSeconds: Long, now: Long): Boolean
    fun deadLetters(): List<String>
    fun visibleCount(now: Long): Int
    fun inFlightCount(now: Long): Int
}
```

## Rules

- `send` stores the message and returns its id: `"m1"`, `"m2"`, … It is visible immediately.
- `receive(now)` looks at messages in **send order** and takes the first **visible** one (visible = never received, or `now >= invisibleUntil`).
  - If that message has already been received `maxReceives` times, move it to the dead-letter list instead (record its id) and keep looking.
  - Otherwise: `receiveCount + 1`, `invisibleUntil = now + visibilityTimeoutSeconds`, a new receipt `"<id>#<receiveCount>"`, and return it.
  - Nothing visible → `null`.
- `ack(receipt, now)`: deletes the message **only if** the receipt is the message's **latest** receipt and `now < invisibleUntil`. Returns whether it deleted.
- `extendVisibility(receipt, extra, now)`: same validity check as `ack`; if valid set `invisibleUntil = now + extra` and return `true`.
- `visibleCount` / `inFlightCount` count messages (not dead-lettered or deleted) that are visible / invisible at `now`.

## Example

Timeout 30. Worker A receives `m1` at 0 (receipt `m1#1`) and crashes. At 30 `m1` is visible again; worker B receives it (`m1#2`). A wakes up and acks `m1#1` → `false` (stale receipt). B acks `m1#2` → deleted.

## Think about (answer in NOTES.md)

- This gives **at-least-once** delivery: in what situation does a message get processed twice even though nobody crashed?
- How do you pick the visibility timeout? What does `extendVisibility` let a slow worker do?
- When would you choose a queue like SQS, and when a log like Kafka?

---

## Run the tests

```bash
./gradlew :l0-amazon-sqs-work-queue-ack:test
```

Your code goes in `src/main/kotlin/WorkQueue.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
