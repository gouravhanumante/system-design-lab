# OTP with expiry and attempt limit

| App | Level | Concept |
|-----|-------|---------|
| Rapido | 2 — Building blocks | Time-to-live, attempt counting, lockout, result codes |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Rapido-style systems verify the rider with a **per-ride OTP**; other apps use a **static PIN** the rider keeps for months. A 4-digit code has only 10,000 possibilities, so a per-ride OTP needs two guard rails or it can be brute-forced: it must **expire**, and it must **lock** after a few wrong guesses.

## Build this (small scale)

An OTP service that issues codes, enforces a time limit and a maximum number of wrong attempts, and tells the caller *why* a verification failed. Time is passed in (`now`, seconds).

## API

```kotlin
enum class VerifyResult { OK, WRONG, EXPIRED, LOCKED, UNKNOWN_RIDE, ALREADY_USED }

class OtpService(ttlSeconds: Long = 600, maxAttempts: Int = 3, generator: () -> String) {
    fun issue(rideId: String, now: Long): String
    fun verify(rideId: String, code: String, now: Long): VerifyResult
}
```

## Rules

- `issue` **always** asks `generator` for a fresh code and *replaces* any previous one for that ride: attempts reset to 0, used/locked flags cleared, expiry = `now + ttlSeconds`.
- `verify` checks, **in this order**:
  1. no code was ever issued → `UNKNOWN_RIDE`
  2. already verified successfully → `ALREADY_USED`
  3. wrong attempts so far `>= maxAttempts` → `LOCKED` (even if the code is right)
  4. `now >= expiresAt` → `EXPIRED` (does **not** count as an attempt)
  5. code differs → attempts + 1, return `WRONG`
  6. otherwise → mark used, return `OK`
- So with `maxAttempts = 3`: three wrong guesses each return `WRONG`; the fourth call returns `LOCKED` no matter what.

## Example

TTL 600, issued at `t = 0`. `verify("r", "1234", 599)` can succeed. `verify("r", "1234", 600)` → `EXPIRED`.

## Think about (answer in NOTES.md)

- Compare with a **static PIN**: what protection does it lose? What does it gain (works offline, no SMS cost)?
- Locking per *ride* is not enough if an attacker can create many rides. What else would you limit (per phone number, per IP)?
- What should happen to the old code when the rider taps "resend OTP"?

---

## Run the tests

```bash
./gradlew :l2-rapido-otp-expiry-attempts:test
```

Your code goes in `src/main/kotlin/OtpService.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
