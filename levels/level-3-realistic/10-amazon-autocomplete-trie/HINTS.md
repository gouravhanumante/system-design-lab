# Hints — Search autocomplete with a trie

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Walking to the prefix node is easy. The hard part is: how can the node already know its best `k` descendants?

</details>

<details><summary>Hint 2 — which data structure?</summary>

`Node(children: HashMap<Char, Node>, top: MutableList<Pair<String, Long>>)` and a separate `HashMap<String, Long>` for total counts.

</details>

<details><summary>Hint 3 — the approach</summary>

In `addQuery`, after updating the total, walk from the root along the query's characters. At **every** node on that path (root included), put/replace `(query, newTotal)` in its `top` list, sort by `(-count, query)`, and cut it to `maxSuggestions`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Shard the trie by the first character or two, and cache the top suggestions for the most common short prefixes at the edge (CDN).

</details>
