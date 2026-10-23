# Webhook delivery with backoff and a dead-letter queue

| App | Level | Concept |
|-----|-------|---------|
| Stripe | 3 — Realistic | At-least-once delivery, exponential backoff, dead-letter queue, redrive |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

When a payment succeeds, Stripe calls your server (`POST /webhooks`) to tell you. Your server may be down, slow or buggy. Stripe retries with **exponential backoff** (wait longer after each failure, so a struggling server isn't hammered) and after several failures moves the event to a **dead-letter queue** where a human can inspect it and **redrive** it later.

## Build this (small scale)

A dispatcher simulated with `tick(now)` calls. Sending is an injected function.

## API

```kotlin
class WebhookDispatcher(
    send: (endpoint: String, eventId: String) -> Boolean,
    maxAttempts: Int = 5, baseDelaySeconds: Long = 10,
) {
    fun enqueue(eventId: String, endpoint: String, now: Long)
    fun tick(now: Long): List<String>
    fun redrive(eventId: String, endpoint: String, now: Long): Boolean
    fun delivered(): List<String>
    fun deadLetters(): List<String>
    fun pending(): Int
}
```

## Rules

A **delivery** is one `(eventId, endpoint)` pair, written `"<eventId>-><endpoint>"`.

- `enqueue`: the delivery becomes pending, due at `now`. Enqueuing a delivery that already exists (pending, delivered or dead) does nothing.
- `tick(now)`: take every pending delivery with `due <= now`, ordered by `(due, eventId, endpoint)`, and attempt each **once**:
  - `send` returns `true` → delivered; log `"<delivery> #<attempt> ok"`.
  - `send` returns `false` **or throws** → log `"<delivery> #<attempt> fail"`. If `attempt >= maxAttempts` → moved to the dead-letter list. Otherwise it stays pending with `due = now + baseDelaySeconds × 2^(attempt − 1)`.
  - Return the log lines of this tick in order. Attempt numbers start at 1 and count every try.
- `redrive`: if the delivery is in the dead-letter list, move it back to pending, due at `now`, attempts reset to 0 → `true`. Otherwise `false`.
- `delivered()` / `deadLetters()`: deliveries in the order they reached that state. `pending()` = number pending.

## Example

base 10, max 3, endpoint always failing: attempt 1 at t=0, then due 10, attempt 2 at t=10, due 30, attempt 3 at t=30 → dead letter.

## Think about (answer in NOTES.md)

- Because of retries, your server may receive the **same event twice**. How should a webhook handler protect itself? (You've seen the answer several times in this repo.)
- Add **jitter** (a random part of the delay). Why does it help when thousands of endpoints fail at the same moment?
- Events for one customer can arrive out of order after retries. What should receivers do — and why does Stripe tell you to re-fetch the object instead of trusting the payload?

---

## Run the tests

```bash
./gradlew :l3-stripe-webhook-retries:test
```

Your code goes in `src/main/kotlin/WebhookDispatcher.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
