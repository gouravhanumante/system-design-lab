# Concepts — Read replicas and read-your-writes

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Primary and replicas

One **primary** database accepts writes; **replicas** copy every change and serve reads. Since most apps read far more than they write, replicas multiply read capacity. They also provide **failover**: a replica can be promoted if the primary dies.

## Replication lag

Copying takes time — milliseconds normally, seconds or more under load. During that **lag**, replicas return older data. Asynchronous replication is fast for writes but allows lag; synchronous replication avoids lag but makes every write wait.

## Eventual consistency

"If no new writes happen, all replicas will eventually show the same data." It's weaker than strong consistency (every read sees the latest write) but much cheaper and good enough for many features (product listings, like counts).

## Read-your-writes

A user who just changed something must see their own change. Common fix: route that user's reads to the primary for a short time after a write, or pass the write's log position so a replica waits until it has caught up.

## Other consistency guarantees

**Monotonic reads**: a user never sees data go backwards (can break if consecutive reads hit replicas with different lag; sticky replica routing fixes it). **Consistent prefix**: you never see an answer before its question. Knowing these names helps you explain trade-offs precisely.
