# Snowflake IDs

| App | Level | Concept |
|-----|-------|---------|
| Twitter | 2 — Building blocks | Unique, time-sortable ids without a central counter; bit packing |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Twitter creates thousands of tweets per second on hundreds of servers. Asking one central database "give me the next id" for every tweet would be a bottleneck and a single point of failure. Twitter's **Snowflake** lets each server generate ids **on its own**, guaranteed unique and roughly sorted by time, by packing three numbers into one 64-bit `Long`:

```
 0 | 41 bits: milliseconds since epoch | 10 bits: worker id | 12 bits: sequence
```

## Build this (small scale)

An id generator for one worker, plus a function to unpack an id.

## API

```kotlin
data class SnowflakeParts(val timestamp: Long, val workerId: Int, val sequence: Int)

class SnowflakeGenerator(workerId: Int, epochMillis: Long = 0, clock: () -> Long) {
    fun nextId(): Long
}

fun decode(id: Long): SnowflakeParts        // timestamp is relative to the epoch
```

## Rules

- `workerId` must be in `0..1023`, else `IllegalArgumentException`.
- `nextId()` reads `clock()` **once** at the start; `ts = clock() - epochMillis`. If `ts < 0` or the clock went **backwards** compared to the last id's timestamp → `IllegalStateException`.
- Same millisecond as the previous id → `sequence + 1`. New millisecond → sequence `0`.
- If the sequence would exceed `4095` (more than 4,096 ids in one millisecond), keep calling `clock()` until it returns a later millisecond, then use sequence `0` with that new timestamp.
- `id = (ts shl 22) or (workerId shl 12) or sequence`.
- `decode` reverses the packing.

## Example

Worker 5, epoch 0, clock 1000 → first id has parts `(1000, 5, 0)` = `1000 shl 22 or 5 shl 12` = `4194324480`.

## Think about (answer in NOTES.md)

- 41 bits of milliseconds is about 69 years. Why do real systems use a **custom epoch** (e.g. 2010) instead of 1970?
- What really happens on servers when the clock jumps backwards (NTP correction)? What are your options besides throwing?
- Ids from *different* workers in the same millisecond: are they in perfect creation order? Does it matter for a timeline?
- How do workers get a unique worker id in the first place?

---

## Run the tests

```bash
./gradlew :l2-twitter-snowflake-ids:test
```

Your code goes in `src/main/kotlin/Snowflake.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
