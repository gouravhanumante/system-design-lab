# Concepts — OTP with expiry and attempt limit

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Brute-force protection

A 4-digit code has only 10,000 possible values. Without limits, a script could try all of them in seconds. Two defences:
- **Attempt limit / lockout:** after N wrong guesses, stop accepting guesses for that code.
- **Expiry:** the code only works for a short time, so there's no time to guess.

With 3 attempts, an attacker's chance is 3/10,000 per code. Real systems also limit attempts per phone number and per IP address, because an attacker can create many rides.

## Time-to-live (TTL)

A **TTL** says how long data stays valid. After that it is treated as gone. Stores like Redis can delete keys automatically when their TTL runs out — perfect for OTPs, sessions and caches. The rule "expired at exactly `issuedAt + ttl`" must be applied consistently.

## Order of checks matters

When several things can be wrong at once (expired *and* locked *and* wrong code), the **order** of the checks decides which error the user sees and whether an attempt is counted. Security code usually checks the "hard stops" (already used, locked) first, so they can't be bypassed. Specifying this order explicitly avoids subtle bugs.

## Result types instead of booleans

Returning `true/false` hides *why* something failed. An enum like `OK, WRONG, EXPIRED, LOCKED` lets the UI show the right message ("code expired — resend?") and lets monitoring count each failure type separately.
