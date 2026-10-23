# Tweet search with an inverted index

| App | Level | Concept |
|-----|-------|---------|
| Twitter | 5 — Boss | Inverted index, positional postings, phrase and exclusion queries, deletes |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Searching "world cup final" across billions of tweets can't scan every tweet. Search engines build an **inverted index**: for every word, the list of documents (and positions) where it appears. A query then **intersects** a few short lists instead of reading everything. Twitter's search also supports exact **phrases** (`"world cup"`), **exclusions** (`-spoiler`) and shows the newest results first.

## Build this (small scale)

An in-memory tweet search engine.

## API

```kotlin
class TweetSearch {
    fun index(tweetId: Long, text: String)
    fun delete(tweetId: Long)
    fun search(query: String, limit: Int): List<Long>    // newest (largest id) first
}
```

## Rules — tokenizing (same for tweets and queries)

- Lowercase the text, then split on every character that is **not** `a–z`, `0–9`, `#` or `@`. Drop empty tokens. Each token has a **position**: 0, 1, 2, … in the order they appear.
  `"Final: ARG 3-3 FRA! #WorldCup"` → `final, arg, 3, 3, fra, #worldcup` (positions 0–5).

## Rules — queries

- A query is made of **parts**:
  - a **phrase**: text between double quotes, e.g. `"world cup"` → its tokens must appear in the tweet **consecutively and in that order**;
  - an **exclusion**: a word starting with `-`, e.g. `-spoiler` → tweets containing that token are removed;
  - otherwise a plain **term** → the tweet must contain that token.
- A tweet matches when it satisfies **all** terms and phrases and contains **none** of the excluded tokens.
- A query with **no** terms or phrases (only exclusions, or empty) returns an empty list.
- Results: matching tweet ids, largest id first, at most `limit`.

## Rules — index

- Indexing an id that is already indexed **replaces** the old text.
- `delete` removes the tweet from all results (unknown id: nothing happens).

## Example

Tweets: `1 "world cup final tonight"`, `2 "the cup of the world"`, `3 "World Cup spoiler: ARG wins"`.
`search("world cup", 10)` → `[3, 2, 1]`. `search("\"world cup\"", 10)` → `[3, 1]`. `search("\"world cup\" -spoiler", 10)` → `[1]`.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`tokenize()`** + **`index()`** + **`delete()`** — the inverted index with positions.
2. **`parseQuery()`**.
3. **`search()`** for plain terms (AND, newest first, limit). 🎯 `termsAreAndedAndNewestComesFirst`, `tokenizingIsCaseInsensitiveAndKeepsHashtagsAndMentions`, `limitApplies`, `deleteAndReplace`, `manyTweets`
4. **Exclusions**. 🎯 `exclusionsRemoveMatches`
5. **`containsPhrase()`** — phrases. 🎯 `phrasesNeedConsecutiveTokensInOrder`, `phraseCanMatchLaterOccurrence`, `mixedQueryWithPhraseTermsAndExclusion`

## Think about (answer in NOTES.md)

- Which list should you start intersecting with, and why? (Hint: "the" appears in half of all tweets.)
- Deletes in real search engines are usually **tombstones** that are cleaned up during *segment merges*. Why not delete from every posting list immediately?
- Ranking by newest is easy. What signals would you add to rank by *relevance* (engagement, author, term frequency — look up BM25)?

---

## Run the tests

```bash
./gradlew :l5-twitter-search-inverted-index:test
```

Your code goes in `src/main/kotlin/TweetSearch.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
