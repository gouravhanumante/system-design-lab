# Group chat fan-out and "read by all"

| App | Level | Concept |
|-----|-------|---------|
| WhatsApp | 3 — Realistic | Snapshot of recipients at send time, per-recipient state, fan-out |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

A message in a group of 200 people is **fanned out**: the server creates one delivery per recipient. Who counts as a recipient is decided **when the message is sent** — someone who joins later must not see old messages, and the sender never "receives" their own message. Blue ticks appear only when everyone who is *still in the group* has read it.

## Build this (small scale)

A single group chat with membership changes, per-recipient read tracking and a "read by everyone" check.

## API

```kotlin
class GroupChat {
    fun addMember(user: String)
    fun removeMember(user: String)
    fun send(sender: String, messageId: String, text: String)
    fun inbox(user: String): List<String>                 // texts the user received, in send order
    fun markRead(user: String, messageId: String)
    fun readBy(messageId: String): Set<String>
    fun isReadByAll(messageId: String): Boolean
}
```

## Rules

- `send` by a user who is **not a member** throws `IllegalStateException`. Sending a `messageId` that already exists is ignored.
- The **recipients** of a message are the members at the moment of sending, **except the sender**. This set never changes afterwards.
- `inbox(user)` = texts of all messages that listed the user as a recipient, in send order (leaving the group does not delete them).
- `markRead(user, messageId)` records the read only if the user is a recipient of that message; otherwise nothing happens (sender, outsiders, unknown message).
- `readBy` returns the users who have read the message (empty for an unknown message).
- `isReadByAll`: `true` when **every recipient who is still a member** has read it. If no such recipient remains it is vacuously `true`. Unknown message → `false`.
- Adding an existing member or removing a non-member does nothing.

## Example

Members `a, b, c`. `a` sends `m1` → recipients `{b, c}`. `d` joins afterwards → `d` never gets `m1`. `b` reads → not read by all. `c` leaves → `isReadByAll(m1)` is `true` (only `b` is left and has read it).

## Think about (answer in NOTES.md)

- For a group of 1,000 members and 50 messages per second, what does "one delivery row per recipient" cost? What would you store instead?
- Why is the recipient list a **snapshot** rather than "whoever is in the group now"? What bug would "now" cause when someone is added later?
- How does end-to-end encryption complicate fan-out? (Each recipient needs the message encrypted for them.)

---

## Run the tests

```bash
./gradlew :l3-whatsapp-group-fanout:test
```

Your code goes in `src/main/kotlin/GroupChat.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
