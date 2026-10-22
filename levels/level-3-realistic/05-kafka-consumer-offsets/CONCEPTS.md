# Concepts — Consumer groups, commits and replay

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Consumer groups

A **consumer group** is one logical application reading a topic. Different groups (e.g. "email-service" and "analytics") each read **all** messages independently, each with their own progress. That's how one stream of events can feed many systems.

## Position vs. committed offset

- **Position**: where the consumer will read next (in memory, lost on crash).
- **Committed offset**: the progress the group has saved durably.

After a crash, the consumer resumes from the **committed** offset. Anything read but not committed will be read again.

## Delivery guarantees

- **At-most-once**: commit *before* processing. Crash after commit → message lost, never duplicated.
- **At-least-once**: commit *after* processing. Crash before commit → message processed again, never lost.
- **Exactly-once**: needs extra machinery (transactions, or idempotent processing). Most systems choose at-least-once and make processing idempotent.

## Consumer lag

**Lag** = how far the group is behind the end of the log. Rising lag means consumers can't keep up — one of the most-watched metrics in any streaming system.

## Replay

Because the log keeps data, a consumer can **seek** back and re-read: to rebuild a database after a bug fix, or to test a new service on real historical data. Being able to replay is one of the biggest advantages of a log over a classic message queue.
