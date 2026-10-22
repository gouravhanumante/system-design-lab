# Concepts — Follow graph and "suggested for you"

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Graphs and adjacency sets

A **graph** is nodes (users) plus edges (follows). A **directed** graph has one-way edges: A follows B doesn't mean B follows A. The common in-memory representation is an **adjacency set**: for each node, the set of nodes it points to.

## Storing both directions

"Who does A follow?" and "who follows A?" are both frequent questions. Storing only one direction makes the other question a full scan. So systems store edges **twice** (outgoing and incoming), at the cost of keeping both in sync on every change.

## Two-hop traversal

"Friends of friends" means walking two edges: from you to your followees, then to theirs. The number of nodes reached grows fast (5,000 × 5,000 = 25 million), so real recommendation systems sample, limit, or precompute offline.

## Ranking by overlap

A good signal for "you may know this person": how many of your connections are connected to them. Counting overlaps with a hash map (`score[c]++`) and sorting by score is the basic version of collaborative filtering.
