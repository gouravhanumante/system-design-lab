# Message ordering and gap recovery across devices

| App | Level | Concept |
|-----|-------|---------|
| WhatsApp | 5 — Boss | Server-assigned sequence numbers, buffering, gap detection, batched catch-up |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Your phone and laptop both show the same chat. Messages can arrive **out of order**, **twice**, or not at all (the laptop was asleep). WhatsApp-style systems let the **server** stamp each message in a chat with an increasing sequence number. A device shows messages only up to the first **gap**, notices that something is missing, and asks the server to fill it in — in batches, not one by one.

## Build this (small scale)

A chat server that assigns sequence numbers, and a device client that buffers pushed messages, displays the contiguous prefix, and catches up with `sync()`.

## API

```kotlin
data class Message(val chatId: String, val seq: Long, val messageId: String, val text: String)

class ChatServer {
    fun send(chatId: String, messageId: String, text: String): Message
    fun fetch(chatId: String, afterSeq: Long, limit: Int): List<Message>
    val fetchCount: Int                       // how many times fetch() has been called
}

class DeviceClient(server: ChatServer, chatId: String, batchSize: Int = 50) {
    fun receive(message: Message)            // a push from the server: may be late, duplicated or out of order
    fun displayed(): List<Message>
    fun hasGap(): Boolean
    fun sync()
}
```

## Rules — server

- Per chat, sequence numbers start at `1` and increase by one for every **new** message.
- `send` is **idempotent per chat**: if `messageId` was already sent in that chat, return the original `Message` and do not use a new number.
- `fetch(chatId, afterSeq, limit)` returns messages with `seq > afterSeq`, ascending, at most `limit`. Every call increases `fetchCount` (even if the result is empty).

## Rules — client

- `receive` stores the message by `seq`; a repeated `seq` is ignored.
- `displayed()` returns the **contiguous prefix**: messages `1, 2, …, n` where `n` is the largest number such that all of `1..n` have been received — in order.
- `hasGap()` is `true` when the client holds at least one message with a `seq` greater than `n + 1` (something between is missing).
- `sync()` repeatedly calls `server.fetch(chatId, afterSeq = n, limit = batchSize)`, `receive`s everything returned (and recomputes `n`), and **stops as soon as a fetch returns fewer than `batchSize` messages**. After `sync` the client has every message the server had when the last fetch happened.

## Example

Server has messages 1–5. The device receives only 1, 2, 5 via push: `displayed() = [1, 2]`, `hasGap() = true`. After `sync()` it shows 1–5.

## Think about (answer in NOTES.md)

- Why a server-side counter instead of device timestamps? (Think of phones with wrong clocks.)
- One counter per chat is a **bottleneck** for a giant group chat. How could the counter be made fast (batching, a single owner node per chat)?
- Why stop at "fewer than `batchSize`" instead of "until an empty page"? What do you save?

---

## Run the tests

```bash
./gradlew :l5-whatsapp-ordering-gap-sync:test
```

Your code goes in `src/main/kotlin/ChatSync.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
