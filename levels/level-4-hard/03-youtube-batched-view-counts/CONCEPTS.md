# Concepts — View counts at scale — write batching

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Write batching (buffering)

Each database write has a fixed cost (network round trip, disk sync, locks). If 50,000 views per second each do `+1`, the database drowns. **Batching** collects many small changes in memory and writes them as one `+N`. 100 views → 1 write is a 100× reduction.

## Read-your-writes with a buffer

While increments sit in the buffer, the database is behind. If reads only looked at the database, numbers would jump around. Adding the buffer to the database value when reading keeps the answer **exact** from this server's point of view.

## Durability trade-off

Anything that is only in memory is lost if the process crashes. Bigger batches = fewer writes but more potential loss and staler data in the database. Mitigations: flush every N seconds as well as every N views, write a cheap append-only log first (a **write-ahead log**), or accept small losses (fine for view counts, never for payments).

## Sharded counters

Another way to handle a hot counter: split it into N rows (`views_0 … views_9`), have each write pick one at random, and sum them on read. Writes no longer contend on one row. Read cost grows with N, so it's a trade-off.
