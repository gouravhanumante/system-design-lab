# Concepts — Order saga with compensations

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Distributed transactions

Inside one database, a transaction makes several changes atomic. Across separate services (inventory, payments, shipping), there's no shared transaction. Options: **two-phase commit** (strong, but blocking and rarely used between microservices) or **sagas** (eventually consistent, widely used).

## Saga pattern

A saga is a sequence of local steps, each with a **compensating action** that semantically undoes it (refund for charge, release for reserve). If step N fails, run the compensations of steps N−1 … 1 in **reverse** order. The system may be briefly inconsistent, but ends in a consistent state.

## Orchestration vs. choreography

- **Orchestration**: a central coordinator tells each service what to do next (this problem). Easy to follow and debug.
- **Choreography**: services react to each other's events ("PaymentCharged" → shipping books a courier). No central point, but the flow is spread over many places and harder to see.

## Retries vs. permanent failure

Transient errors (timeouts, a busy server) deserve retries; permanent errors (card declined) should fail immediately. Real systems classify errors to decide. Retried steps must be **idempotent**.

## Execution logs and recovery

A saga's log ("ok:reserve", "ok:charge", …) is what makes it recoverable: after a crash, the orchestrator reads the log and continues or compensates. Workflow engines (Temporal, AWS Step Functions) store this log durably for you.

## Stuck sagas

If a compensation can't succeed (refund API down for a day), automation must stop and alert a human. Designing the "stuck" path explicitly — instead of retrying forever — is part of a production-ready saga.
