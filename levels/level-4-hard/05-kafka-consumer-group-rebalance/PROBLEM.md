# Group coordinator and rebalancing

| App | Level | Concept |
|-----|-------|---------|
| Kafka | 4 — Hard | Partition assignment, membership, heartbeats, generations |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Ten consumers in the same group share a topic's partitions: each partition is read by **exactly one** member of the group, so work is split without duplicates. When a member joins, leaves or silently dies, the coordinator **rebalances** — recomputes who owns what. Detecting a dead member is done with **heartbeats**: no heartbeat for too long means the member is kicked out.

## Build this (small scale)

A group coordinator that assigns partitions with the **range** strategy, handles join/leave, and expires members that stopped sending heartbeats. Time is passed in (`now`, seconds).

## API

```kotlin
class GroupCoordinator(partitions: Int, sessionTimeoutSeconds: Long = 10) {
    fun join(member: String, now: Long)
    fun leave(member: String)
    fun heartbeat(member: String, now: Long): Boolean
    fun checkTimeouts(now: Long): List<String>
    fun assignment(): Map<String, List<Int>>
    fun generation(): Int
}
```

## Rules

- **Range assignment**: sort members by name. With `P` partitions and `M` members, every member gets `P / M` partitions and the first `P % M` members (in sorted order) get one extra. Partitions are handed out as **consecutive ranges in ascending order**.
  Example: 7 partitions, members `a, b, c` → `a=[0,1,2]`, `b=[3,4]`, `c=[5,6]`.
- `assignment()` contains **every** member (members with nothing to do map to an empty list). With no members it is empty.
- `join` of a **new** member records its heartbeat time (`now`) and triggers a rebalance. `join` of a member that is already in the group only counts as a heartbeat (no rebalance).
- `leave` of a member triggers a rebalance; leaving an unknown member does nothing.
- `heartbeat` refreshes a known member and returns `true`; for an unknown member (e.g. one already kicked out) it returns `false`.
- `checkTimeouts(now)` removes every member with `now − lastHeartbeat >= sessionTimeoutSeconds`. If at least one was removed there is **one** rebalance (even if several members were removed). It returns the removed names sorted alphabetically.
- `generation()` starts at `0` and increases by `1` on every rebalance. (Consumers use it to detect that their assignment is stale.)

## Example

Timeout 10. `a` and `b` join at time 0, `a` sends a heartbeat at 8. At `checkTimeouts(10)`, `b` has been silent for 10 s → removed; `a` receives all partitions.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`rebalance()`** — range assignment over the sorted members.
2. **`join()`** / **`leave()`** + **`assignment()`** / **`generation()`**. 🎯 `rangeAssignmentSplitsEvenly`, `extraPartitionsGoToTheFirstMembers`, `moreMembersThanPartitionsLeavesSomeIdle`, `emptyGroupHasNoAssignment`, `joiningAndLeavingRebalances`, `rejoiningOrLeavingUnknownMembersDoesNotRebalance`, `everyPartitionIsOwnedByExactlyOneMember`
3. **`heartbeat()`**. 🎯 `heartbeatKeepsAMemberAlive` (with step 4)
4. **`checkTimeouts()`**. 🎯 `timeoutBoundaryAndRepeatedChecks`, `severalTimeoutsAtOnceCauseOneRebalance`, `kickedMembersLearnItFromTheirNextHeartbeat`

## Think about (answer in NOTES.md)

- During a rebalance every consumer stops reading ("stop-the-world"). What would **incremental / sticky** assignment change?
- A consumer is alive but slow (long garbage collection). It gets kicked out, then wakes up and keeps processing. How does the **generation** protect you?
- Why is it a bad idea to choose a very short session timeout? A very long one?

---

## Run the tests

```bash
./gradlew :l4-kafka-consumer-group-rebalance:test
```

Your code goes in `src/main/kotlin/GroupCoordinator.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
