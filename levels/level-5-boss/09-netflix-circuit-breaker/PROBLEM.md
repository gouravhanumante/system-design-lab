# Circuit breaker with fallbacks

| App | Level | Concept |
|-----|-------|---------|
| Netflix | 5 — Boss | Fail fast, CLOSED/OPEN/HALF_OPEN states, recovery probing |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

The Netflix home page calls dozens of services (recommendations, ratings, artwork…). If the ratings service hangs, every request waiting on it piles up and the **whole site** goes down — a *cascading failure*. Netflix's Hystrix library made the **circuit breaker** famous: after repeated failures, stop calling the sick service for a while and return a **fallback** (e.g. "no ratings shown") immediately. Then carefully test whether it has recovered.

## Build this (small scale)

A circuit breaker around any call. Time comes from an injected clock (milliseconds).

## API

```kotlin
enum class State { CLOSED, OPEN, HALF_OPEN }

class CircuitBreaker(
    failureThreshold: Int, openMillis: Long, halfOpenSuccesses: Int, clock: () -> Long,
) {
    fun <T> call(action: () -> T, fallback: () -> T): T
    fun state(): State
    val rejected: Int          // calls answered by the fallback WITHOUT trying the action
}
```

## Rules

Read `clock()` once at the start of each `call`.

- **CLOSED** (normal): run `action`.
  - Success → reset the consecutive-failure count, return the result.
  - Exception → failure count + 1. If it reaches `failureThreshold` → go **OPEN** (remember `openedAt = now`). Return `fallback()` either way.
- **OPEN**: if `now - openedAt < openMillis` → don't run the action, `rejected + 1`, return `fallback()`. Otherwise switch to **HALF_OPEN** and handle this call as HALF_OPEN.
- **HALF_OPEN** (testing recovery): run `action`.
  - Success → success count + 1; when it reaches `halfOpenSuccesses` → **CLOSED** (all counters reset). Return the result.
  - Exception → back to **OPEN** with `openedAt = now`, success count reset. Return `fallback()`.
- `state()` reports the stored state; it does **not** look at the clock (the OPEN → HALF_OPEN switch happens inside the next `call`).
- Constructor arguments must be positive (`openMillis` ≥ 0 is allowed), else `IllegalArgumentException`.

## Example

Threshold 3, open 1000 ms, 2 successes needed. Three failures at t = 0 → OPEN. Calls at t = 500 are rejected instantly. At t = 1000 a call is let through (HALF_OPEN); two successes later the breaker is CLOSED again.

## Think about (answer in NOTES.md)

- Why count **consecutive** failures? What would an *error-rate over a window* (e.g. 50% of the last 20 calls) do better?
- What makes a good fallback? Give one for: recommendations, payment, login.
- Breakers protect the *caller*. What protects the *sick service* from a thundering herd when every breaker closes at the same moment?

---

## Run the tests

```bash
./gradlew :l5-netflix-circuit-breaker:test
```

Your code goes in `src/main/kotlin/CircuitBreaker.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
