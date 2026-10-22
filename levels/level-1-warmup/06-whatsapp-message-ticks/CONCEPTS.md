# Concepts — Message ticks (sent, delivered, read)

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Monotonic state

Some values should only ever move **forward**: a message goes sent → delivered → read, never backwards. If you only apply an update when it's "higher" than the current value, the order in which updates arrive stops mattering. That makes the system robust against reordering and duplicates.

## Out-of-order and duplicate delivery

On real networks, messages can arrive late, twice, or in a different order than sent: retries after a timeout, two different servers forwarding, a phone reconnecting and flushing a queue. Designing every handler to tolerate this is essential.

## Enums with a natural order

Kotlin enums are compared by declaration order: `SENT < DELIVERED < READ`. Declaring states in their logical order lets you write `if (new > current)` instead of a pile of `if`s.

## CRDT (max-register)

A **CRDT** (conflict-free replicated data type) is a data structure that several copies can update independently and later merge with the same result, regardless of order. "Keep the maximum" is the simplest CRDT: merging `max(a, b)` is order-independent. Collaborative editors and offline-first apps use more advanced CRDTs.
