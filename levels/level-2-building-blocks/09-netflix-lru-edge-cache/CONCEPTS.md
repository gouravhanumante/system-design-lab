# Concepts — LRU cache on an edge server

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Caching

A **cache** keeps copies of frequently used data somewhere faster or closer (memory instead of disk, a nearby server instead of a far one). Success is measured by the **hit ratio** = hits / (hits + misses). Every cache has limited space, so it needs an **eviction policy**.

## LRU eviction

**Least Recently Used** evicts the item that hasn't been accessed for the longest time, on the assumption that recently used items will be used again soon (*temporal locality*). Other policies: **LFU** (least frequently used), **FIFO**, **random**, and hybrids like **ARC** or **W-TinyLFU** (used by Caffeine).

## O(1) LRU: hash map + doubly linked list

A hash map finds an item by key in O(1). A doubly linked list keeps items in recency order, and moving an item to the front or removing the last one is O(1) when you have a pointer to the node. Together they give O(1) `get` and `put`. Java's `LinkedHashMap` with `accessOrder = true` does exactly this internally.

## Size-aware caching

When items have very different sizes, counting *items* is misleading — one 4 GB movie isn't the same as one 2 KB thumbnail. Measuring capacity in **bytes** and evicting until the new item fits is what real CDN caches do.

## Content delivery networks (CDN)

A **CDN** is a network of cache servers placed close to users. Netflix's Open Connect puts its own appliances inside internet providers so most video never crosses the wider internet. Static assets of almost every website are served this way.
