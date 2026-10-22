# Concepts — Find the nearest drivers

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Big-O complexity

Big-O describes how the work grows when the input grows. Scanning every driver once is **O(n)**: 10× more drivers, 10× more work. Sorting n items is **O(n log n)** — a bit worse than linear. Looking up a key in a hash map is **O(1)** on average: it doesn't get slower as the map grows.

In system design you use Big-O to answer "what happens at 1,000,000 drivers and 10,000 requests per second?". If one request costs O(n) and n is a million, that is 10 billion operations per second — impossible. That is the signal that you need a smarter structure (an *index*).

## Squared Euclidean distance

The straight-line distance between `(x1, y1)` and `(x2, y2)` is `sqrt(dx² + dy²)`. When you only need to **compare** distances ("which is closer?"), you can skip the square root: if `a < b` then `a² < b²` for non-negative numbers. Squared distances are integers (no floating-point rounding) and faster to compute.

Real maps use latitude/longitude, where the earth's curve matters (the *haversine* formula). On a small area a flat grid is a good approximation.

## Sorting with a comparator (tie-breaking)

A comparator tells the sort how to order two items. When two items are "equal" on the main key, you need a **tie-breaker**, otherwise the output can change from run to run and tests become flaky. In Kotlin: `compareBy({ it.distance }, { it.id })` sorts by distance, then by id.

Deterministic ordering matters in real systems too: two servers answering the same request should return the same result.

## Top-k with a heap

You often want only the best `k` items, not a full sort. A **max-heap of size k** keeps the k best seen so far: for each new item, if it is better than the worst in the heap, swap it in. Cost: O(n log k) instead of O(n log n). For k = 5 and n = 1,000,000 that's a big saving. Kotlin/Java: `java.util.PriorityQueue` with a reversed comparator.
