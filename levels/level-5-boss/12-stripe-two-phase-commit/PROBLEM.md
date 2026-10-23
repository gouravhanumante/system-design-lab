# Two-phase commit across banks (UPI-style)

| App | Level | Concept |
|-----|-------|---------|
| Stripe | 5 — Boss | Atomic commitment, prepare/commit votes, durable decision log, crash recovery |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

A UPI payment debits your bank and credits your friend's bank — two separate systems. Either **both** happen or **neither**. **Two-phase commit (2PC)** is the classic protocol: a coordinator first asks every participant "can you do this?" (**prepare**; each participant locks what it needs and votes yes/no). Only if all vote yes does it decide **COMMIT**; otherwise **ABORT**. The decision is written to a durable **log** *before* telling anyone, so that after a crash a new coordinator can read the log and finish the job.

## Build this (small scale)

A coordinator with a durable log (a `MutableList<String>` passed in, so a "restarted" coordinator can reuse it).

## API

```kotlin
interface Participant {                    // provided
    fun prepare(txId: String): Boolean
    fun commit(txId: String)
    fun abort(txId: String)
}

enum class Outcome { COMMITTED, ABORTED, IN_DOUBT }

class Coordinator(participants: Map<String, Participant>, log: MutableList<String>, commitRetries: Int = 3) {
    fun execute(txId: String, involved: List<String>): Outcome
    fun recover(): Map<String, Outcome>
}
```

## Rules — log format (exactly)

`"begin <txId> <p1>,<p2>,…"`, `"decision <txId> COMMIT"` / `"decision <txId> ABORT"`, `"done <txId>"`.

## Rules — `execute(txId, involved)`

- `involved` empty, an unknown participant name, or a `txId` that already has a `begin` line in the log → `IllegalArgumentException`.
- Append the `begin` line. Ask `prepare` of each participant **in order**. A `false` vote or an exception → stop asking the rest and **abort**.
- **Abort**: append `decision … ABORT`, call `abort` on **every** involved participant (ignore exceptions), append `done …`, return `ABORTED`.
- **Commit**: append `decision … COMMIT`, then call `commit` on every involved participant in order. If a call throws, retry it up to `commitRetries` more times. If a participant still fails, **do not** write `done` — return `IN_DOUBT` (the decision is final; `recover` will finish it). Otherwise append `done …` and return `COMMITTED`.

## Rules — `recover()`

For every transaction that has a `begin` but no `done`, in the order of their `begin` lines:
- no decision → append `decision … ABORT`, abort everyone, append `done`, outcome `ABORTED`;
- decision `ABORT` → abort everyone, append `done`, `ABORTED`;
- decision `COMMIT` → commit everyone (with retries); all succeed → append `done`, `COMMITTED`; else `IN_DOUBT`.

Return a map `txId → outcome` for the transactions it handled.

## Example

Participants `hdfc` and `sbi` both vote yes → log: `begin t1 hdfc,sbi`, `decision t1 COMMIT`, `done t1`. If `sbi` votes no → `hdfc` is told to abort even though it voted yes.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`execute()`** validation + phase 1 (prepare / votes) + **`abortAll()`**. 🎯 `oneNoVoteAbortsEveryoneAndStopsPreparing`, `anExceptionDuringPrepareCountsAsNo`, `invalidCalls`
2. **`commitAll()`** — phase 2 with retries. 🎯 `everyoneVotesYesSoItCommits`, `commitIsRetried`
3. **`recover()`** — read the log and finish unfinished transactions. 🎯 `commitThatKeepsFailingIsInDoubtAndRecoverFinishesIt`, `recoveryAfterACrashUsesOnlyTheLog`

## Think about (answer in NOTES.md)

- 2PC is called a **blocking** protocol: if the coordinator dies after participants voted yes, they must hold their locks until it comes back. Why can't a participant decide on its own?
- Why must the decision be written to the log *before* sending any commit?
- UPI actually uses a central switch (NPCI) plus reversal/reconciliation flows rather than textbook 2PC. Compare with the saga you built for Amazon: what does each give up?

---

## Run the tests

```bash
./gradlew :l5-stripe-two-phase-commit:test
```

Your code goes in `src/main/kotlin/TwoPhaseCommit.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
