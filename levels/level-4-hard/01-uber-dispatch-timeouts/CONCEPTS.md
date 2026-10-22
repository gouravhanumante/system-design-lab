# Concepts — Dispatch with offer timeouts

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## State machine

A **state machine** says which states something can be in and which events move it between them. A ride offer here goes `OFFERED → MATCHED`, `OFFERED → OFFERED(next driver)`, or `OFFERED → FAILED`. Drawing it as boxes and arrows *before* coding makes bugs obvious (e.g. "what if accept arrives after the timeout?").

## Timeouts and retries

Networks and people don't always answer. A **timeout** turns "no answer" into a definite event you can act on. Two rules make timeouts safe:
1. After the deadline, a late answer must be **rejected** (otherwise two drivers might both "win").
2. The thing that timed out must be **released** (the driver goes back to the pool).

A **retry** then tries the next option. Real systems add **backoff** (wait longer between retries) so they don't hammer a struggling service.

## Reservation (avoiding double booking)

While a driver is considering an offer, nobody else may offer them a ride. Temporarily taking a resource out of the pool is a **reservation** or **lease**. The same idea protects cinema seats, hotel rooms and stock in a shopping cart. In a distributed system the reservation is usually an atomic "set if not already set" in a shared store, so two servers can't reserve the same driver at the same moment.

## Simulated time (passing `now` in)

The code receives `now` as a parameter instead of reading the system clock. That makes behaviour **deterministic** and testable: a test can jump straight to `now = 10` instead of sleeping 10 seconds. Production code passes the real clock; tests pass fake times. This is called *injecting* the clock.
