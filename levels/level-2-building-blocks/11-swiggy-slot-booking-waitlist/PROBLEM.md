# Delivery slots with capacity and a waitlist

| App | Level | Concept |
|-----|-------|---------|
| Swiggy | 2 — Building blocks | Capacity limits, FIFO waitlist promotion, idempotent booking |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Swiggy Instamart and grocery apps sell **delivery slots** ("Tomorrow 7–9 AM"). Each slot has limited riders, so only so many orders fit. When it's full, new orders join a **waitlist**; when someone cancels — or ops adds riders — the waitlist moves forward in order. When ops *removes* capacity, the most recent confirmations are bumped back.

## Build this (small scale)

Slot booking with capacity, a FIFO waitlist, cancellations and capacity changes.

## API

```kotlin
enum class BookingStatus { CONFIRMED, WAITLISTED }

class SlotBooking {
    fun addSlot(slotId: String, capacity: Int)
    fun book(slotId: String, orderId: String): BookingStatus
    fun cancel(orderId: String)
    fun setCapacity(slotId: String, capacity: Int)
    fun status(orderId: String): String?
    fun confirmed(slotId: String): List<String>
    fun waitlist(slotId: String): List<String>
}
```

## Rules

- `addSlot`: `capacity >= 0`, slot id must be new (else `IllegalArgumentException`).
- `book`: unknown slot → `IllegalArgumentException`. If the order is already booked **in this slot**, return its current status (idempotent). If it is booked in **another** slot → `IllegalArgumentException`. Otherwise: CONFIRMED if there is room, else appended to the waitlist.
- `cancel`: removes the order wherever it is (unknown: nothing happens). If a confirmed order was cancelled, the **first** waitlisted order is promoted.
- `setCapacity` (`>= 0`, unknown slot → `IllegalArgumentException`):
  - bigger → promote from the front of the waitlist until full or the waitlist is empty;
  - smaller than the number confirmed → the **most recently confirmed** orders are moved to the **front** of the waitlist, keeping their order among themselves.
- `confirmed(slot)` lists confirmed orders in the order they were confirmed (a promoted order counts as confirmed at promotion time). `waitlist(slot)` lists waiting orders front first.
- `status(order)`: `"CONFIRMED:<slot>"`, `"WAITLISTED:<slot>:<position>"` (position 1 = next in line), or `null`.

## Example

Capacity 2: book a, b, c → a, b confirmed; c waitlisted at 1. Cancel a → c confirmed. `setCapacity(1)` → c (most recently confirmed) goes back to the **front** of the waitlist.

## Think about (answer in NOTES.md)

- Two app servers book the last place in the same slot at the same moment. How do you stop both from succeeding?
- Should a promoted customer be notified, and what if they no longer want the slot? (Offer + timeout, like Uber dispatch.)
- Is "most recently confirmed gets bumped" fair? What alternatives exist?

---

## Run the tests

```bash
./gradlew :l2-swiggy-slot-booking-waitlist:test
```

Your code goes in `src/main/kotlin/SlotBooking.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
