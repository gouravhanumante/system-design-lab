# Ride-start OTP

| App | Level | Concept |
|-----|-------|---------|
| Rapido | 1 — Warm-up | Per-ride secret, one-time use, injected randomness |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Before a Rapido captain can start your ride, you read out a short code. It proves the person on the bike is the one who was assigned to *you*, and that you are the one who booked it. Uber and Rapido both do some version of this.

## Build this (small scale)

A tiny service that creates a one-time code per ride and verifies it. Randomness is **injected** (`generator`) so the tests can predict the codes.

## API

```kotlin
class RideOtp(generator: () -> String) {
    fun issue(rideId: String): String
    fun verify(rideId: String, code: String): Boolean
}
```

## Rules

- `issue` asks `generator` for a new code the **first** time it sees a ride and stores it. Calling `issue` again for the same ride returns the same code and does **not** call `generator` again.
- `verify` is `true` only if the ride exists and `code` matches exactly.
- A code works **once**: after a successful `verify`, every later `verify` for that ride returns `false`.
- A wrong code does not use up the ride's code.
- Different rides never affect each other.

## Example

`generator` yields `"4821"`. `issue("r1")` → `"4821"`. `verify("r1", "0000")` → `false`, then `verify("r1", "4821")` → `true`, then `verify("r1", "4821")` → `false`.

## Think about (answer in NOTES.md)

- Why a code **per ride** instead of one fixed PIN per user? What leaks if a PIN is overheard once?
- Why is injecting the generator better than calling `Random` inside the class?

---

## Run the tests

```bash
./gradlew :l1-rapido-ride-otp:test
```

Your code goes in `src/main/kotlin/RideOtp.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
