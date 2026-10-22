# Concepts — Inventory holds during checkout

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Overselling and race conditions

Two buyers check "1 left?", both see yes, both buy. The gap between **checking** and **changing** is a **race condition**. Preventing it needs the check and the change to happen **atomically** — e.g. one SQL statement `UPDATE stock SET qty = qty - 1 WHERE sku = ? AND qty >= 1` and checking how many rows changed.

## Reservations (holds) with expiry

Holding stock during checkout guarantees the buyer can complete the purchase. An **expiry** stops abandoned carts from locking stock forever. This *lease* pattern — "you own this until time T unless you renew" — is used for seats, hotel rooms, distributed locks and Kafka consumer sessions.

## Lazy vs. eager expiry

**Lazy**: expired holds are ignored when computing availability; nothing needs to run in the background. **Eager**: a job removes them on schedule. Lazy is simpler and always correct at read time; eager keeps storage tidy. Many systems do both.

## Idempotent operations

Clients retry. "Hold again with the same order id" returns the existing hold, and "confirm again" returns success without selling twice. Making repeated calls safe is far simpler than trying to make sure they never happen.

## Available-to-promise

`available = stock − held − sold` is a simple **available-to-promise** calculation. Real retailers add stock in transit, safety stock and multiple warehouses, but the principle stays the same.
