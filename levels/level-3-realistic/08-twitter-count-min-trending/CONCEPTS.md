# Concepts — Trending hashtags with a Count-Min Sketch

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Probabilistic data structures

Some questions don't need exact answers. **Probabilistic** structures trade a small, bounded error for huge memory savings. Classic examples: **Bloom filter** ("have I seen this?" — false positives possible, false negatives never), **HyperLogLog** ("how many distinct items?"), and **Count-Min Sketch** ("roughly how often did I see this?").

## Count-Min Sketch

A table of `depth` rows × `width` counters. Each row uses a different hash function to pick one counter per item. Adding an item increments one counter in every row. To estimate, read the item's counter in each row and take the **minimum**. Collisions can only *add* other items' counts, so estimates are **never too low**, and taking the minimum picks the row with the fewest collisions. More width → fewer collisions; more depth → less chance that every row collides.

## Hash functions as "different seeds"

You often need several independent hash functions. A common trick is one good hash function with different seeds (here: the row number mixed into the hash). Independence matters: if all rows collided on the same items, extra rows wouldn't help.

## Heavy hitters (top-k)

Finding the most frequent items in a stream without storing every item is the **heavy hitters** problem. Pairing a sketch (for counts) with a small candidate set (for names) is a standard approach; algorithms like *Space-Saving* and *Misra–Gries* are others.

## Mergeable summaries

Two sketches of the same size can be combined by adding cell by cell. So each server can count its own traffic, and a central job merges them — no need to ship raw events around.
