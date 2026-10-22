# Concepts — Key-value store with transactions

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Key-value stores

The simplest database: `set(key, value)`, `get(key)`, `delete(key)`. Redis, Memcached and DynamoDB are key-value stores at heart. Many larger systems are built from key-value storage underneath.

## Transactions and ACID

A **transaction** groups operations so they behave as one. **ACID**: **A**tomic (all or nothing), **C**onsistent (rules hold before and after), **I**solated (concurrent transactions don't see each other's partial work), **D**urable (committed data survives crashes).

## Commit and rollback

Changes inside a transaction are kept aside. **Commit** makes them permanent; **rollback** throws them away. Nested transactions (or *savepoints* in SQL) let you undo part of the work without abandoning everything.

## Layers and tombstones

Each open transaction is a **layer** of changes on top of the committed data; reads look from the top layer down. A delete inside a transaction can't just remove the key (the committed value would show through), so it writes a **tombstone** — a marker meaning "deleted here".

## Write-ahead logging (WAL)

Real databases write each change to an append-only **log** on disk before applying it. After a crash, replaying the log restores committed transactions and discards uncommitted ones. That's how durability is achieved.

## Isolation levels

When many users work at once, databases offer levels like *read committed*, *repeatable read* and *serializable*, trading correctness against speed. Postgres and MySQL use **MVCC** (multiple versions per row) so readers don't block writers.
