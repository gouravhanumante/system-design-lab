# Concepts — Fastest route and ETA (Dijkstra)

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Weighted graphs

A road network is a **graph**: junctions are nodes, roads are edges, and each edge has a **weight** (travel time). One-way streets are **directed** edges. The fastest route is the path with the smallest total weight.

## Dijkstra's algorithm

Keep the best known time to every node. Repeatedly take the **unsettled node with the smallest time** — its time can no longer improve, so settle it — and try to improve its neighbours through it. With a priority queue this runs in O((V + E) log V). It requires **non-negative** weights.

## Priority queues

A **priority queue** (a binary heap) always gives you the smallest element in O(log n). `java.util.PriorityQueue` is one. Many algorithms (Dijkstra, schedulers, top-k, event simulation) are built around it.

## Deterministic tie-breaking

When two routes are equally fast, users and tests should still get one stable answer. Including the path itself in the comparison (lexicographic order) makes the output deterministic.

## Faster routing at scale

Map services use **A\*** (Dijkstra plus a distance estimate to guide the search), **contraction hierarchies** (precomputed shortcuts) and traffic-aware edge weights. Delivery apps often precompute travel times between areas and only route precisely for the last stretch.
