# Concepts — Search autocomplete with a trie

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Trie (prefix tree)

A **trie** stores strings character by character: each node is a prefix, each edge a character. All strings starting with "iph" live under the node reached by i → p → h. Finding that node takes as many steps as the prefix has characters, regardless of how many strings are stored.

## Precomputing answers per node

Walking the whole subtree under "i" to find the best suggestions would be slow (it contains everything starting with "i"). Instead, each node **stores its top-k** results, updated on every insert. Reads become O(prefix length). This is a general strategy: when reads vastly outnumber writes, do the work at write time.

## Read-optimised vs. write-optimised

Every index makes a trade: faster reads, slower and bigger writes. Here one insert updates up to `length + 1` nodes. For autocomplete — millions of keystrokes, comparatively few new queries — that's the right trade. Many real systems rebuild such indexes offline in batches and swap them in.

## Why monotonic counts make the cache safe

The per-node top-k is only correct because counts **only go up**: a query can only enter a node's top list when its count increases, which is exactly when that node is updated. If counts could decrease, a query just below the cutoff might deserve to move up and nobody would notice. Spotting which assumptions keep an optimisation correct is a key engineering skill.

## Normalisation

"iPhone  Case" and "iphone case" should be the same query. Lowercasing, trimming and collapsing spaces before storing — and normalising the prefix the same way — avoids duplicate entries.
