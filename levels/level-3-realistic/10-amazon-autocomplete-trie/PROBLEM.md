# Search autocomplete with a trie

| App | Level | Concept |
|-----|-------|---------|
| Amazon | 3 — Realistic | Trie (prefix tree), precomputed top-k per node, read-optimised indexes |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Type "iph" into Amazon's search box and suggestions appear in milliseconds: "iphone 15 case", "iphone charger"… ranked by how often people searched them. Searching through millions of past queries on every keystroke is far too slow. A **trie** (prefix tree) finds everything starting with a prefix by walking one node per character — and if each node already **remembers its best suggestions**, the answer is ready the moment you reach the node.

## Build this (small scale)

An autocomplete index that answers by visiting only the nodes of the prefix.

## API

```kotlin
class Autocomplete(maxSuggestions: Int = 5) {
    fun addQuery(query: String, count: Long = 1)
    fun suggest(prefix: String): List<String>
    val lastNodesVisited: Int
}
```

## Rules

- **Normalise** every added query: lowercase, trim, and collapse runs of spaces into one space. Empty after normalising → ignored. `count >= 1`, else `IllegalArgumentException`.
- Adding the same (normalised) query again **adds** to its total count.
- `suggest(prefix)`: lowercase the prefix (do not trim it). Return up to `maxSuggestions` stored queries that **start with** the prefix, highest count first, ties alphabetically. An empty prefix matches everything.
- `lastNodesVisited` = how many trie nodes the last `suggest` call touched, counting the root. It must be **at most `prefix.length + 1`** — no exploring below the prefix node at query time.

## Example

Queries: `iphone case` ×5, `iphone charger` ×3, `ipad` ×4. `suggest("iph")` → `["iphone case", "iphone charger"]`, visiting 4 nodes (root, i, p, h). `suggest("ip")` → `["iphone case", "ipad", "iphone charger"]`.

## Think about (answer in NOTES.md)

- Precomputing top-k in every node makes reads O(prefix length) but writes more expensive. How many nodes does one `addQuery` update?
- Your cache trick works because counts only **increase**. What breaks if a query can be removed or its count decreased?
- Real systems rebuild the trie offline from search logs every few hours and ship it to servers. Why not update it live on every search?

---

## Run the tests

```bash
./gradlew :l3-amazon-autocomplete-trie:test
```

Your code goes in `src/main/kotlin/Autocomplete.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
