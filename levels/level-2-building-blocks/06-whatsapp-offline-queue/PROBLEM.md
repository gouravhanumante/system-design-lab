# Delivering messages to offline users

| App | Level | Concept |
|-----|-------|---------|
| WhatsApp | 2 — Building blocks | Per-user queue, FIFO delivery on reconnect, TTL, idempotent sends |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Your friend's phone is off. WhatsApp keeps the message on its server and delivers it the moment the phone reconnects — in the original order. It does not keep it forever (messages for phones that stay offline for ~30 days are discarded), and if your phone retries a send because of a flaky connection, the friend must not receive it twice.

## Build this (small scale)

A server-side inbox that delivers immediately to online users and queues for offline ones. Time is passed in (`now`, seconds).

## API

```kotlin
class OfflineInbox(ttlSeconds: Long = 30L * 24 * 3600) {
    fun send(to: String, messageId: String, text: String, now: Long)
    fun connect(user: String, now: Long): List<String>      // returns the texts delivered because of this connect
    fun disconnect(user: String)
    fun isOnline(user: String): Boolean
    fun delivered(user: String): List<String>               // everything ever delivered to the user, in delivery order
}
```

## Rules

- Users start **offline**.
- `send` to an **online** user delivers immediately (the text is appended to `delivered(user)`).
- `send` to an **offline** user queues the message (remember when it was sent).
- `connect` marks the user online. It delivers every queued message that has **not expired**, oldest first, and returns their texts (also appended to `delivered`). A message sent at `t` is expired when `now - t >= ttlSeconds`; expired messages are silently dropped. The queue is then empty.
- `connect` for a user who is already online delivers nothing and returns an empty list.
- `disconnect` marks the user offline.
- **Idempotent sends:** if the same `messageId` is sent to the same user again (a client retry), the repeat is ignored — whether the first copy was delivered or is still queued.

## Example

TTL 100. `send("bob","m1","hi",0)`, `send("bob","m2","there",50)`. `connect("bob", 100)` → only `["there"]` (m1 is exactly 100 s old = expired).

## Think about (answer in NOTES.md)

- Where would the real queue live (memory? database?) and what happens to queued messages if the server restarts?
- A user who is offline for 3 weeks reconnects with 20,000 queued messages. How should delivery be paced?
- How do you make the "same message id twice" check cheap when a user has millions of past messages?

---

## Run the tests

```bash
./gradlew :l2-whatsapp-offline-queue:test
```

Your code goes in `src/main/kotlin/OfflineInbox.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
