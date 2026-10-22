# Concepts — Trip lifecycle, fare and cancellation fees

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## State machine

A ride has a lifecycle: `REQUESTED → MATCHED → ARRIVED → STARTED → COMPLETED`, plus cancellations. A **state machine** lists the allowed transitions; everything else is an error. Writing it as a table (current state × action → next state) and checking it at the start of every operation prevents "impossible" situations like completing a ride that never started.

## Guard clauses and fail-fast errors

A guard clause is an early check that throws if the input or current state is invalid: `check(state == STARTED)`. Failing **loudly and early** beats silently doing something wrong — especially with money. Kotlin has `require` (bad argument → `IllegalArgumentException`) and `check` (bad state → `IllegalStateException`).

## Money and rounding

Money calculations need an explicit **rounding rule** and an explicit **order of operations** (apply the multiplier, *then* round, *then* apply the minimum). Different orders give different totals, and customers notice. Real payment systems store money as integers of the smallest unit (paise, cents) to avoid floating-point errors like `0.1 + 0.2 = 0.30000000000000004`.

## Idempotency

An operation is **idempotent** if doing it twice has the same effect as doing it once. Mobile apps retry requests when the network drops, so "complete trip" might arrive twice. Without idempotency the rider could be charged twice. Common technique: the client sends a unique **request id**, and the server remembers which ids it has already processed.

## Snapshotting inputs at the right time

The surge multiplier is captured **when the ride is requested**, not when it ends. The rule: store the inputs that the customer agreed to, at the moment they agreed. Prices, exchange rates and tax rates all follow this rule.
