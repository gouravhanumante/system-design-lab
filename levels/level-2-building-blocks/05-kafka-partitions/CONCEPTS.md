# Concepts — Partitions and key-based routing

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Partitioning (sharding)

One machine has limits. **Partitioning** splits data across several independent pieces so many machines can share the work. In Kafka a topic has N **partitions**, each its own log, each possibly on a different server. Partitioning is how almost every large system scales: databases, caches, search indexes.

## Hash partitioning

To decide where a record goes, compute `hash(key) mod N`. The same key always lands in the same partition, so all events for one user stay together **and in order**. Watch out: `hashCode()` can be negative, and `-7 % 3 == -1` in Kotlin, which is not a valid partition. `Math.floorMod` always returns 0…N-1.

## Ordering guarantees

Order is guaranteed **within one partition**, not across partitions. If you need all of a user's events in order, they must share a key. Global ordering across everything would require one partition — no parallelism. Choosing the key is choosing what stays ordered.

## Round-robin

Records without a key have nothing to group by, so they're spread evenly: partition 0, 1, 2, 0, 1, 2, … This balances load but gives no ordering between records.

## Re-partitioning

Changing N changes `hash mod N` for most keys, so existing keys suddenly map to different partitions and per-key order breaks. That's why partition counts are chosen carefully up front. **Consistent hashing** (in the Netflix problems) reduces how many keys move when nodes change.
