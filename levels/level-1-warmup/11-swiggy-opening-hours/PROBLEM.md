# Is the restaurant open?

| App | Level | Concept |
|-----|-------|---------|
| Swiggy | 1 — Warm-up | Time intervals, wrap-around past midnight and end of week, modular arithmetic |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

A biryani place is open **Friday 22:00 – 02:00**. At Saturday 01:30, is it open? Swiggy and Zomato answer this for every restaurant on every search, and the overnight case (and Sunday-night-into-Monday) is a famous source of bugs. The trick is to turn every moment into **"minutes since the start of the week"**.

## Build this (small scale)

Weekly opening hours with overnight support.

## API

```kotlin
class OpeningHours {
    fun addInterval(day: Int, open: String, close: String)       // day: 0 = Monday … 6 = Sunday, times "HH:MM"
    fun isOpen(day: Int, time: String): Boolean
    fun nextOpening(day: Int, time: String): Pair<Int, String>?
}
```

## Rules

- `day` must be `0..6`; times must be `"HH:MM"` with `00 <= HH <= 23` and `00 <= MM <= 59`. Anything else → `IllegalArgumentException`.
- An interval is open from `open` (inclusive) to `close` (exclusive).
- If `close` is **earlier** than `open`, the interval runs past midnight and ends at `close` on the **next** day (Sunday's overnight interval ends on Monday). `open == close` → `IllegalArgumentException`.
- A restaurant may have several intervals per day (lunch and dinner).
- `nextOpening(day, time)`: if open at that moment → return `(day, time)` unchanged. Otherwise return the **next** moment an interval starts, looking up to one week ahead (wrapping from Sunday to Monday). Times are formatted `"HH:MM"`. No intervals at all → `null`.

## Example

Friday `22:00–02:00`: `isOpen(4, "23:59")` = true, `isOpen(5, "01:59")` = true, `isOpen(5, "02:00")` = false. `nextOpening(5, "02:00")` = `(4, "22:00")` — next Friday.

## Think about (answer in NOTES.md)

- Time zones: the restaurant is in Kolkata, the server runs in UTC. Where should the conversion happen?
- Holidays and "closed today" overrides: how would you add exceptions on top of the weekly schedule?
- Search shows thousands of restaurants: would you compute `isOpen` per request or precompute something?

---

## Run the tests

```bash
./gradlew :l1-swiggy-opening-hours:test
```

Your code goes in `src/main/kotlin/OpeningHours.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
