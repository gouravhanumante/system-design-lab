# Concepts — Order tracking with event sourcing

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Event sourcing

Instead of storing only the current state ("order is PICKED_UP"), store every **event** that happened ("placed", "accepted", "picked up by Ravi"). The current state is computed by replaying events. You get a full audit history for free and can build new views of old data later.

## Projections

A **projection** (or read model) is the current state derived from events, shaped for a particular question: "order status page", "rider's active deliveries", "average prep time per restaurant". Several projections can be built from the same events.

## Versioned events and ordering

Events from different services can arrive late or twice. A **per-entity version number** lets the consumer detect duplicates (version too low), gaps (version too high → buffer) and apply events strictly in order.

## Validating transitions

Events describe things that already happened elsewhere, but a projection should still refuse impossible sequences (delivered before picked up). Rejecting them, alerting, and not consuming the version keeps the state trustworthy.

## Replay and determinism

If applying events is **deterministic**, replaying the same events (in any delivery order, thanks to versions) always rebuilds the same state. That's how you recover after a bug: fix the code, wipe the projection, replay.

## Snapshots

Replaying years of events is slow. Periodically saving a **snapshot** of the state (plus the version it covers) lets you replay only events after the snapshot.

## CQRS

**Command Query Responsibility Segregation** separates the write side (validating and storing events) from the read side (projections). Event sourcing and CQRS are often used together.
