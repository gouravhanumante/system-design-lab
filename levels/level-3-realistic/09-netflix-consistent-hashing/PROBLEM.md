# Consistent hashing for cache servers

| App | Level | Concept |
|-----|-------|---------|
| Netflix | 3 — Realistic | Hash ring, virtual nodes, minimal key movement |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

A city has 3 Netflix cache servers. Which server should hold *Stranger Things*? A simple `hash(title) % 3` works — until a 4th server is added and `% 4` sends almost every title to a different server, so every cache goes cold at once. **Consistent hashing** places servers and keys on a **ring**: each key belongs to the next server clockwise. Adding or removing a server only moves the keys next to it.

## Build this (small scale)

A hash ring with **virtual nodes** (each server appears at several points so load is spread evenly).

## API

```kotlin
fun ringPosition(s: String): Long          // provided: position on the ring, 0 .. 2^32 - 1

class HashRing(virtualNodes: Int) {
    fun addNode(node: String)
    fun removeNode(node: String)
    fun nodeFor(key: String): String?
    fun nodes(): Set<String>
}
```

## Rules

- `addNode(node)` puts `virtualNodes` points on the ring, at `ringPosition("$node#$i")` for `i` in `0 until virtualNodes`. Adding an existing node does nothing.
- `removeNode` removes all of that node's points (unknown node: nothing happens).
- `nodeFor(key)`: let `h = ringPosition(key)`. The owner is the node of the **first point with position ≥ h**; if there is none, wrap around to the point with the smallest position. Empty ring → `null`.
- If two points have exactly the same position, the one belonging to the alphabetically **smaller** node name wins (it's what is stored at that position).
- `virtualNodes` must be ≥ 1, else `IllegalArgumentException`.

## Example

Ring points at 100 (`A`), 500 (`B`), 900 (`A`). A key at 450 → `B`; at 950 → wraps to 100 → `A`.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`init`** check + **`nodes()`**. 🎯 `invalidVirtualNodeCount`
2. **`rebuildRing()`**, **`addNode()`**, **`removeNode()`**.
3. **`nodeFor()`**. 🎯 `emptyRingHasNoOwner`, `singleNodeOwnsEverything`, `ownerIsTheFirstPointClockwise`, `removingANodeOnlyMovesItsOwnKeys`, `addingANodeOnlyTakesKeysForItself`, `virtualNodesSpreadTheLoad`, `addingTwiceAndRemovingUnknownAreHarmless`

## Think about (answer in NOTES.md)

- Measure it: with 4 servers, how many of 10,000 keys move when a 5th is added — with your ring, and with `hash % N`?
- Why do virtual nodes make load more even? What does a *weighted* server (twice the disk) look like on the ring?
- Where else is consistent hashing used? (Dynamo/Cassandra partitions, load balancers, memcached clients.)

---

## Run the tests

```bash
./gradlew :l3-netflix-consistent-hashing:test
```

Your code goes in `src/main/kotlin/HashRing.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
