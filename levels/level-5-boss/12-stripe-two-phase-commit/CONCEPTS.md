# Concepts — Two-phase commit across banks (UPI-style)

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Atomic commitment

When one action spans several independent systems (two banks), **atomic commitment** ensures all of them commit or none do. Without it, money could leave one bank and never arrive at the other.

## Two-phase commit (2PC)

1. **Prepare**: the coordinator asks every participant to get ready (check balance, lock the amount) and vote yes/no. A "yes" is a promise: "I can commit if you tell me to, even after a crash".
2. **Commit / abort**: if everyone voted yes, the coordinator decides COMMIT; otherwise ABORT. Then it tells everyone.

## The durable decision log

The coordinator writes its decision to a **durable log before** telling anyone. If it crashes, a restarted coordinator reads the log: decided → finish telling everyone; not decided → abort. Without the log, participants could end up disagreeing.

## Blocking

A participant that voted yes can't decide alone: maybe others voted no, maybe the coordinator decided commit. It must wait, holding locks, until the coordinator comes back. That's why 2PC is called **blocking** and why it's rarely used between microservices (sagas are used instead).

## In-doubt transactions

If the decision is COMMIT but a participant can't be reached, the transaction is **in doubt** — final, but not yet fully applied. Recovery keeps retrying until every participant has committed.

## Consensus

Replacing the single coordinator with a replicated group that agrees via **Paxos/Raft** removes the blocking problem: the decision survives any single machine failure. Google Spanner and CockroachDB do this.
