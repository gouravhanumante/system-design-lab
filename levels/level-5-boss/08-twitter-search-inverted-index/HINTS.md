# Hints — Tweet search with an inverted index

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Split the job into three small pieces you can test by eye: tokenize, parse the query into (terms, phrases, exclusions), and match.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Postings: `Map<token, Map<tweetId, List<position>>>`. Also keep `Map<tweetId, List<token>>` so you can remove a tweet's postings on delete/replace.

</details>

<details><summary>Hint 3 — the approach</summary>

Candidates = intersection of the tweet-id sets of all required tokens (terms + every phrase token), starting from the smallest set. For each phrase check: is there a position `p` of the first token such that token `i` appears at `p + i` for every `i`? Then drop excluded tweets, sort descending, take `limit`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Real indexes are split into immutable **segments** (Lucene) and sharded by tweet id across many machines; a query goes to every shard and the top results are merged.

</details>
