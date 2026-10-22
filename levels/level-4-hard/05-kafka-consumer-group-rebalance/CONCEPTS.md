# Concepts — Group coordinator and rebalancing

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Partition assignment

Within one consumer group, each partition is read by **exactly one** member, so no message is processed twice by the group. The coordinator splits partitions among members. **Range** assignment hands out consecutive blocks; **round-robin** deals them like cards; **sticky** assignment tries to keep members on the partitions they already had.

## Rebalancing

When membership changes (join, leave, crash), the coordinator **rebalances**: recomputes the assignment. In the classic protocol every consumer pauses during a rebalance ("stop-the-world"), so frequent rebalances hurt throughput. Incremental/cooperative rebalancing only moves the partitions that must move.

## Heartbeats and failure detection

A machine that crashed can't announce that it left. Instead, live members send **heartbeats**; if none arrive within the **session timeout**, the member is presumed dead. Short timeouts detect failures quickly but kick out members that are just slow (e.g. a long garbage-collection pause). Every distributed system tunes this trade-off.

## Generations (epochs) and fencing

Each rebalance increments a **generation** number. A consumer that was kicked out and wakes up still believes it owns partitions. When it tries to commit with an old generation, the coordinator rejects it. Using a number that only goes up to reject stale actors is called **fencing**, and it's everywhere: leader election, distributed locks, Kafka producers.
