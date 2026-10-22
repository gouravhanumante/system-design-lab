# Concepts — Replication, ISR and high watermark

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Leader/follower replication

Data on one machine dies with that machine. **Replication** keeps copies on several machines. In leader/follower replication, one **leader** accepts writes and **followers** copy from it. Readers usually read from the leader. MySQL, PostgreSQL, MongoDB and Kafka all work this way.

## In-sync replicas (ISR)

Followers can lag. The **ISR** is the set of replicas that are fully caught up. A follower that falls behind is removed from the ISR and must catch up to rejoin. Only ISR members are safe to promote to leader, because only they are guaranteed to have every committed record.

## Commit and the high watermark

A record is **committed** once every ISR member has it. The **high watermark** is the boundary: everything below it is committed and safe to show to consumers; everything above it might still be lost if the leader dies. Showing only committed data means consumers never see a record that later "disappears".

## Durability vs. availability

`min.insync.replicas` sets how many copies must exist before a write is accepted. Higher = safer (data survives more failures) but writes stop when too many machines are down. Lower = always accepting writes, but a single failure can lose data. This is a concrete instance of the **CAP** trade-off.

## Unclean leader election

If every in-sync replica is dead, you can either **wait** (unavailable, no data loss) or promote an out-of-sync replica (available again, but committed data may be lost). Kafka's `unclean.leader.election.enable` makes this choice explicit.

## Truncation after failover

When a new leader takes over, followers may hold records the new leader never got (they were never committed). Followers **truncate** their logs to match the new leader, so all replicas agree again. Accepting that uncommitted data can vanish is what makes the protocol consistent.
