# Idempotency keys for a payment API

| App | Level | Concept |
|-----|-------|---------|
| Stripe | 2 — Building blocks | Idempotent POST requests, stored responses, key conflicts, key expiry |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Your app sends "charge ₹499" and the network times out. Did the charge happen? If you retry blindly, the customer may pay twice. Stripe's answer: the client sends an **`Idempotency-Key`** header with every request. The server remembers the key and the response; a retry with the same key gets the **same response** without charging again. Using the same key for a *different* request is a client bug and is rejected.

## Build this (small scale)

A charge endpoint with idempotency keys. The real money movement is done by an injected `processor`.

## API

```kotlin
data class ChargeRequest(val customer: String, val amountPaise: Long, val currency: String)
data class ChargeResponse(val chargeId: String, val amountPaise: Long, val replayed: Boolean)
class IdempotencyConflict(message: String) : RuntimeException(message)     // provided

class PaymentApi(processor: (ChargeRequest) -> String, ttlSeconds: Long = 86_400) {
    fun createCharge(idempotencyKey: String, request: ChargeRequest, now: Long): ChargeResponse
    val processorCalls: Int
}
```

## Rules

- A blank key → `IllegalArgumentException`.
- A key is **live** for `ttlSeconds` after the response was stored (`now - storedAt < ttlSeconds`).
- Live key, **equal** request → return the stored response with `replayed = true`. The processor is **not** called.
- Live key, **different** request → throw `IdempotencyConflict`.
- Unknown or expired key → call `processor(request)` (`processorCalls + 1`). It returns a charge id; store `ChargeResponse(id, amount, replayed = false)` with `storedAt = now` and return it.
- If the processor **throws**, rethrow the same exception and store **nothing** — the client may retry with the same key.

## Example

`createCharge("k1", ₹499)` → `ch_1`, replayed false. Again with `k1` → `ch_1`, replayed true, still 1 processor call. `k1` with ₹999 → `IdempotencyConflict`.

## Think about (answer in NOTES.md)

- Two retries of the same key arrive at **the same time** on two servers. What can go wrong with your code, and how would a real system lock the key while the first request is in flight?
- Why should the stored record include the **request** (or a hash of it)?
- Which HTTP methods are naturally idempotent (GET, PUT, DELETE) and why is POST the one that needs keys?

---

## Run the tests

```bash
./gradlew :l2-stripe-idempotency-keys:test
```

Your code goes in `src/main/kotlin/PaymentApi.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
