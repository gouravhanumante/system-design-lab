# Concepts — Surge pricing for a zone

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Sliding time window

"Requests in the last 60 seconds" is a **sliding window**: the window moves forward with the clock, so old events fall out of it. The usual implementation keeps events in arrival order and, before answering, removes from the front everything that is too old.

Edge cases matter: is an event exactly 60 seconds old inside or outside? Pick a rule (`now - t < window`) and apply it everywhere. Windows are everywhere in system design: rate limiters, surge pricing, trending topics, fraud detection.

## Queue (FIFO) and deque

A **queue** is first-in-first-out: you add at the back and remove from the front, both O(1). A **deque** (double-ended queue) lets you add/remove at both ends. Kotlin's `ArrayDeque` is a good default. Because timestamps arrive in increasing order, the oldest event is always at the front — perfect for expiring old entries.

## Supply and demand pricing

Surge pricing is a **feedback loop**: high demand relative to supply → higher price → some riders wait, more drivers come online → ratio drops → price falls. The *bands* (1.2×, 1.5×, …) keep the price from jumping wildly on small changes. In real systems the inputs are aggregated per zone every few seconds by a streaming job, not computed on every request.

## Time buckets (counting instead of storing)

Instead of storing every event, you can store **one counter per second (or per 10 seconds)**. A 60-second window is then at most 60 numbers no matter how much traffic arrives. You trade a little precision at the edges for constant memory. You'll build this properly in the Rapido hot-zones problem.
