# Concepts — Video view counter and top videos

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Hash map counting

The basic counting pattern: `counts[key] = counts[key] + 1`. In Kotlin, `map.merge(key, 1, Long::plus)` does it in one call. Counting is the foundation of analytics, trending lists, rate limiters and billing.

## Ranking with tie-breaks

To rank, sort by count descending, and break ties by a second key (id) so the result is deterministic. `compareBy({ -it.value }, { it.key })` is a common Kotlin trick: the minus sign reverses the order for the number while keeping ids ascending.

## Hot keys

A **hot key** is one key that receives a huge share of traffic — a viral video's counter. In a distributed database each key lives on one machine, so one hot key overloads one machine while others sit idle. Fixes: **sharded counters** (split the counter into N sub-counters and add them up on read), **write batching** (Level 4), or caching reads.
