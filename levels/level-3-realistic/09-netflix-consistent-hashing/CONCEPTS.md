# Concepts — Consistent hashing for cache servers

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Hash mod N and its problem

The simplest way to spread keys over N servers is `hash(key) % N`. But when N changes (a server is added or fails), almost every key maps to a different server — every cache goes cold at once, or every database shard must move data.

## Consistent hashing (the hash ring)

Map both servers and keys onto the same circle of numbers (0 … 2³²−1). A key belongs to the first server found going **clockwise** from the key's position. When a server is added, it only takes keys from its clockwise neighbour; when one is removed, only its keys move. On average only `1/N` of keys move.

## Virtual nodes

With only a few points on the ring, the gaps between servers are uneven, so some servers get much more load. Giving each server many **virtual nodes** (many points on the ring) evens out the load and also spreads a failed server's keys across all remaining servers instead of one neighbour. Servers with more capacity can simply get more virtual nodes.

## Sorted maps with ceiling lookup

"First position ≥ h" is a **ceiling** query on a sorted structure — `TreeMap.ceilingEntry` in O(log n). Wrap-around is "if nothing is found, take the first entry".

## Where it's used

Amazon Dynamo and Cassandra (partitioning data), memcached clients (choosing a cache server), load balancers (sticky routing), and CDNs. A related idea, **rendezvous (highest random weight) hashing**, solves the same problem differently.
