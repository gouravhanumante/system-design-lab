# Read replicas and read-your-writes

| App | Level | Concept |
|-----|-------|---------|
| Flipkart | 0 — Foundations | Primary/replica, replication lag, eventual consistency, read-your-writes routing |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

During a sale, Flipkart's product and profile databases get far more reads than writes. A standard fix: one **primary** handles writes, several **read replicas** copy it and serve reads. But copying takes time (**replication lag**). If you update your address and the very next page reads from a replica that hasn't caught up, your old address appears — confusing! The fix: **read-your-writes** — for a short time after you write, *your* reads go to the primary.

## Build this (small scale)

A simulated primary + replicas with a fixed lag. Time is passed in seconds.

## API

```kotlin
class ReplicatedDb(replicaCount: Int = 2, lagSeconds: Long = 5) {
    fun write(user: String, key: String, value: String, now: Long)
    fun read(user: String, key: String, now: Long): String?
    val lastReadFrom: String
    val staleReads: Int
}
```

## Rules

- Writes always go to the primary and are visible there immediately. A write made at time `t` reaches every replica at `t + lagSeconds`. So a replica, at time `now`, shows the latest value written at a time `<= now − lagSeconds` (or `null`).
- `read(user, key, now)`:
  - If `user` has written **anything** within the last `lagSeconds` (`now − lastWriteTime < lagSeconds`), read from the **primary** → `lastReadFrom = "primary"`.
  - Otherwise read from a replica, **round robin** (`replica-0`, `replica-1`, …, then back to 0) → `lastReadFrom = "replica-<i>"`.
- `staleReads` counts replica reads whose result differs from what the primary would return at that moment.
- `replicaCount >= 1` and `lagSeconds >= 0`, else `IllegalArgumentException`.

## Example

Lag 5. Alice writes `address = Pune` at 10. Alice reads at 11 → primary → `Pune`. Bob reads at 11 → replica → `null` (stale!). At 15 the replicas have it.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **Config check** in `init`. 🎯 `invalidConfig`
2. **`write()`** — append `(time, value)` to the key's history; remember the user's last write time.
3. **`valueAt()`** helper — the value a key had at a moment in time.
4. **`read()` from replicas**: value as of `now - lagSeconds`, rotate `replica-0`, `replica-1`, …, count stale reads. 🎯 `otherUsersMaySeeStaleDataUntilTheLagPasses`, `replicasShowOldValuesNotJustMissingOnes`, `zeroLagMeansReplicasAreAlwaysFresh`
5. **Read-your-writes**: a user who wrote recently reads from the primary. 🎯 `writerReadsTheirOwnWriteFromThePrimary`, `readYourWritesWindowEnds`, `anyRecentWriteRoutesAllOfTheUsersReadsToPrimary`

## Think about (answer in NOTES.md)

- "Read-your-writes" protects Alice. What about Alice's **second device**? And what about Bob seeing Alice's update?
- Other consistency guarantees: **monotonic reads** (never see time go backwards). Can round robin across replicas with *different* lags break that?
- Replicas also give you failover. If the primary dies, which replica should be promoted? (Kafka L5's ISR.)

---

## Run the tests

```bash
./gradlew :l0-flipkart-read-replicas:test
```

Your code goes in `src/main/kotlin/ReplicatedDb.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
