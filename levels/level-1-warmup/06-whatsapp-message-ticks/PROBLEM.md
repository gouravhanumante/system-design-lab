# Message ticks (sent, delivered, read)

| App | Level | Concept |
|-----|-------|---------|
| WhatsApp | 1 — Warm-up | Monotonic state, ordered enum, ignoring stale updates |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

The little ticks under a WhatsApp message tell a story: one grey tick = **sent** to the server, two grey = **delivered** to the other phone, two blue = **read**. Notifications arrive over the network in any order and sometimes twice, so a message that is already *read* must never slip back to *delivered*.

## Build this (small scale)

Track the status of messages and only ever move it forward.

## API

```kotlin
enum class Status { SENT, DELIVERED, READ }      // in this order

class MessageStatusTracker {
    fun send(messageId: String)
    fun markDelivered(messageId: String)
    fun markRead(messageId: String)
    fun status(messageId: String): Status?
}
```

## Rules

- `send` creates the message with status `SENT`. Sending an id that already exists changes nothing.
- `markDelivered` moves `SENT → DELIVERED`. It never lowers a status (a `READ` message stays `READ`).
- `markRead` moves the message to `READ` from `SENT` or `DELIVERED`.
- Marking an unknown message does nothing (no error, and it does **not** create the message).
- `status` of an unknown message is `null`.

## Example

`send("m")`, `markRead("m")`, `markDelivered("m")` → status is `READ`.

## Think about (answer in NOTES.md)

- Why must out-of-order updates be harmless? Name two network situations where a "delivered" event arrives after a "read" event.
- How would you compare statuses without a pile of `if`s? (Hint: enums have a natural order.)

---

## Run the tests

```bash
./gradlew :l1-whatsapp-message-ticks:test
```

Your code goes in `src/main/kotlin/MessageStatusTracker.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
