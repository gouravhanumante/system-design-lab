# Concepts — Delivery slots with capacity and a waitlist

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Capacity limits

A slot, a room or a class has a **capacity**. The rule "confirmed ≤ capacity" is an invariant that every operation (book, cancel, change capacity) must preserve. Writing one function that restores the invariant after any change (here: rebalance) is simpler than special-casing every operation.

## FIFO waitlists

A **waitlist** is a queue: first come, first promoted. Fairness rules must be explicit — including the unusual cases, like what happens to confirmed people when capacity shrinks.

## Idempotent booking

A retried "book" request must not create two bookings. Returning the existing status for a repeated request makes retries harmless.

## Atomic capacity checks

With many servers, "is there room? then add" must be atomic, or two servers will both fill the last place. Typical solutions: a conditional update (`UPDATE slot SET used = used + 1 WHERE used < capacity`), a row lock, or a single owner per slot.

## Overbooking

Airlines deliberately sell more seats than they have, because some people don't show. Whether to overbook is a business choice made with data — the system just needs a capacity number it can enforce.
