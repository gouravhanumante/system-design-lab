# Order tracking with event sourcing

| App | Level | Concept |
|-----|-------|---------|
| Swiggy | 5 — Boss | Event sourcing, versioned events, projections, out-of-order buffering, replay |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

An order's life is a sequence of **events**: placed, accepted by the restaurant, picked up by a rider, delivered (or cancelled). With **event sourcing** you store those events as the source of truth and **derive** the current state (a *projection*) by applying them in order. Events arrive from different services over a message bus — sometimes late, sometimes twice — so each carries a per-order **version** number. Replaying all stored events must always rebuild exactly the same state.

## Build this (small scale)

A projection that applies versioned order events, tolerates duplicates and out-of-order delivery, rejects impossible transitions, and can be rebuilt from history.

## API

```kotlin
enum class EventType { PLACED, ACCEPTED, PICKED_UP, DELIVERED, CANCELLED }
data class OrderEvent(val orderId: String, val version: Int, val type: EventType, val value: String = "")
data class OrderView(val orderId: String, val status: EventType, val items: List<String>,
                     val rider: String?, val cancelReason: String?, val version: Int)
enum class ApplyResult { APPLIED, DUPLICATE, BUFFERED, REJECTED }

class OrderProjection {
    fun apply(e: OrderEvent): ApplyResult
    fun view(orderId: String): OrderView?
    fun bufferedCount(orderId: String): Int
}

fun rebuild(events: List<OrderEvent>): OrderProjection
```

## Rules

- For each order, the **expected** version is `current version + 1` (`1` for an order never seen).
- `apply(e)`:
  - `e.version < expected` → `DUPLICATE` (ignored).
  - `e.version > expected` → `BUFFERED` (kept for later; if that version is already buffered, keep the first one).
  - `e.version == expected` → if the transition is valid, apply it and return `APPLIED`, then keep applying buffered events while the **next** expected version is in the buffer. If the transition is invalid → `REJECTED`, nothing stored, expected version unchanged. (During draining, an invalid buffered event is dropped and draining stops.)
- **Valid transitions** (`value` meaning in brackets):
  - version 1 must be `PLACED` (`value` = comma-separated items, e.g. `"dosa,coffee"`);
  - `ACCEPTED` after `PLACED`;
  - `PICKED_UP` (rider name) after `ACCEPTED`;
  - `DELIVERED` after `PICKED_UP`;
  - `CANCELLED` (reason) after `PLACED` or `ACCEPTED`.
  - Nothing is valid after `DELIVERED` or `CANCELLED`.
- `view` is `null` until the order has an applied `PLACED` event. `version` in the view = number of applied events.
- `bufferedCount` = how many events wait in that order's buffer.
- `rebuild(events)` applies them to a fresh projection in the given order. For a valid history, **any** order of the events must produce the same views.

## Example

Events arrive as v3 `PICKED_UP(ravi)`, v1 `PLACED(dosa)`, v1 again, v2 `ACCEPTED` → `BUFFERED, APPLIED, DUPLICATE, APPLIED` — and applying v2 also drains v3, so the view shows `PICKED_UP` by `ravi`, version 3.

## Think about (answer in NOTES.md)

- Why store events instead of just the latest state? Name three things you can do with the history (audit, debugging, new projections like "average prep time").
- Replaying 5 years of events on every restart is slow. What are **snapshots**?
- A buffered event whose predecessor never arrives sits forever. What would you do (timeout, fetch from the event store, alert)?

---

## Run the tests

```bash
./gradlew :l5-swiggy-order-event-sourcing:test
```

Your code goes in `src/main/kotlin/OrderProjection.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
