# Replication, ISR and high watermark

| App | Level | Concept |
|-----|-------|---------|
| Kafka | 5 — Boss | Leader/follower replication, in-sync replicas, no loss of committed data |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

A partition lives on one broker — until that machine dies. Kafka keeps **replicas** on several brokers: one **leader** takes all writes, **followers** copy from it. A message counts as **committed** (visible to consumers) only when *every in-sync replica* has it; the committed boundary is the **high watermark**. If the leader dies, a new leader is chosen **only from the in-sync replicas (ISR)**, so nothing that was ever committed can be lost.

## Build this (small scale)

One replicated partition, simulated step by step (no threads, no time — *you* decide when followers fetch).

## API

```kotlin
class ReplicatedPartition(replicas: List<String>, minInSync: Int = 1) {
    fun leader(): String?
    fun isr(): List<String>
    fun produce(value: String): Long                 // returns the offset on the leader
    fun replicate(follower: String)                  // follower fetches everything it is missing from the leader
    fun highWatermark(): Long
    fun consume(): List<String>                      // committed records only
    fun fail(replica: String)
    fun recover(replica: String)
    fun logOf(replica: String): List<String>
}
```

## Rules

- Each replica has its own log (a list of values). At the start all replicas are alive and in the ISR (in the given order); the **leader is the first replica**.
- `produce`: if `isr().size < minInSync` throw `IllegalStateException` (this also covers "no leader"). Otherwise append to the **leader's** log and return the offset (its index). Followers do not receive it yet.
- `replicate(follower)`: if the follower is alive and is not the leader, it copies everything it is missing from the leader (its log becomes equal to the leader's log). If it was **not** in the ISR, it **rejoins** the ISR (appended at the **end** of the ISR list) — it is now fully caught up. Otherwise (dead / the leader / unknown name) nothing happens.
- `highWatermark()` = the smallest log length among the ISR members (all of them have at least that many records). With no leader it is `0`.
- `consume()` = the leader's first `highWatermark()` records (empty with no leader).
- `fail(replica)`: the replica dies and is removed from the ISR. If it was the leader, the **first replica of the remaining ISR** becomes leader (no leader if the ISR is empty) and **every other ISR member truncates its log to the new leader's log length**. Failing a dead replica does nothing; an unknown name → `IllegalArgumentException`.
- `recover(replica)`: if it is already alive, nothing happens. Otherwise it comes back alive but **outside the ISR**, with its log truncated to `highWatermark()` records (records above the high watermark may never have been committed). It rejoins only through `replicate`. **Special case:** if there is currently **no leader**, the recovered replica becomes the leader and the only ISR member, keeping its whole log (an "unclean" election: it may lose committed data — Kafka makes this opt-in).
- `logOf` returns a copy of the replica's log (dead or alive).

## Example

`[a, b, c]`: `produce("x")` → offset 0 on `a`; high watermark is still 0. `replicate(b)`: still 0 (c lags). `replicate(c)`: now 1 and `consume() = ["x"]`.

## Think about (answer in NOTES.md)

- `minInSync` trades **availability** for **durability**. With 3 replicas, what does `minInSync = 3` give up? What does `1` risk?
- Why must a new leader come from the ISR? Construct a scenario where electing an out-of-sync replica loses a committed record.
- A producer got an offset back from `produce` but the leader died before replication. What should the producer have asked for (`acks`)?

---

## Run the tests

```bash
./gradlew :l5-kafka-replication-isr:test
```

Your code goes in `src/main/kotlin/ReplicatedPartition.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
