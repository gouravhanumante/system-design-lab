# Concepts — Continue watching

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Per-user (per-profile) state

Many features are "a small record per user per item": watch progress, cart lines, read receipts. The natural key is the pair `(profile, item)`. Storing it as a map of maps (or a table with a composite primary key) makes "everything for this profile" fast.

## Recency ordering

"Most recent first" needs a timestamp per record and a sort. For a short list per user that's fine; for huge lists you'd keep an index sorted by time.

## Thresholds and business rules

"Finished at 95%" is a **business rule**, not a technical one. Making it a parameter (instead of a magic number in the code) keeps it easy to tune and to test.

## Last write wins

When the same record is updated from several devices, the simplest conflict rule is **last write wins** — the newest update replaces older ones. It's fine for watch progress (losing a few seconds doesn't matter) but dangerous for data people would miss, like documents.

## Write-heavy workloads

Players report progress every few seconds for every viewer — far more writes than reads. Systems like this use stores optimised for fast writes (Cassandra, DynamoDB) and often keep only the latest value instead of every update.
