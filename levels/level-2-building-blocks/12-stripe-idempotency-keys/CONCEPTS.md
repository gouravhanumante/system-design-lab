# Concepts — Idempotency keys for a payment API

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Idempotency

An operation is **idempotent** if repeating it has the same effect as doing it once. `GET`, `PUT` and `DELETE` are idempotent by definition in HTTP; `POST` ("create a charge") is not. Retrying a non-idempotent request after a timeout can double-charge a customer.

## Idempotency keys

The client generates a unique key (usually a UUID) for each logical operation and sends it with every retry. The server stores `key → (request, response)`. A repeat gets the stored response; the side effect happens only once. Stripe, PayPal, Razorpay and AWS APIs all work this way.

## Request fingerprints

Storing the request (or a hash of it) with the key lets the server detect a **client bug**: the same key reused for a different operation. Refusing it is safer than guessing.

## Failure handling

If the operation fails before completing, nothing should be stored, so the client can retry with the same key. If it fails *after* the side effect but before storing the response, you have a gap — which is why real systems store the key and the result in the **same database transaction**.

## Concurrency

Two retries of the same key may arrive simultaneously. The first should "lock" the key (status IN_PROGRESS) so the second waits or gets a "409 Conflict – in progress" instead of running the charge again.

## Key expiry

Keys are kept for a limited time (Stripe: 24 hours). After that a key may be reused. This bounds storage while covering realistic retry windows.
