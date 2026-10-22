# Concepts — Live demand heatmap (hot zones)

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Time buckets (counting instead of storing)

Storing every event and recounting is too expensive at high traffic. Instead, split time into **buckets** (e.g. 10 seconds) and keep only a **count per zone per bucket**. A 5-minute window is 30 buckets — memory depends on the number of buckets, not the number of events. The cost: the window edge is only accurate to one bucket.

## Ring buffer / expiring old buckets

As time moves on, the oldest bucket leaves the window. Either keep a fixed array of N buckets and overwrite the oldest (**ring buffer**), or keep a queue of buckets and drop from the front. Either way, only the expiring bucket is touched — no full recount.

## Running totals

Instead of summing 30 buckets on every query, keep a **running total**: add when an event arrives, subtract a whole bucket when it expires. Queries become cheap. This "maintain the aggregate incrementally" idea is how streaming systems (Flink, Kafka Streams) compute live dashboards.

## Top-k

"Which k zones are hottest?" means sorting by count and taking k. With many zones, a heap of size k avoids sorting everything. Across many servers, each computes its local counts and a coordinator **merges** them — merging counts is easy (add them); merging only each server's top-k can be wrong, which is a well-known trap.
