# Follow graph and "suggested for you"

| App | Level | Concept |
|-----|-------|---------|
| Instagram | 2 — Building blocks | Directed graph as adjacency sets, 2-hop traversal, ranking by overlap |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Instagram is a giant **directed graph**: "A follows B" is an edge from A to B. Two questions come up constantly: *who are our mutual connections?* and *who might I want to follow?* — the second is usually answered by looking at people followed by the people **you** follow.

## Build this (small scale)

A follow graph with mutual-follow lookup and a simple suggestion engine.

## API

```kotlin
class FollowGraph {
    fun follow(follower: String, followee: String): Boolean
    fun unfollow(follower: String, followee: String): Boolean
    fun followers(user: String): Set<String>
    fun following(user: String): Set<String>
    fun commonFollowing(a: String, b: String): Set<String>
    fun suggestions(user: String, limit: Int): List<String>
}
```

## Rules

- `follow` returns `true` if a new edge was added. Following yourself, or someone you already follow, returns `false` and changes nothing. `unfollow` returns `true` only if an edge was removed.
- `followers(u)` = who follows `u`. `following(u)` = whom `u` follows. Unknown users have empty sets.
- `commonFollowing(a, b)` = users that **both** `a` and `b` follow.
- `suggestions(user, limit)`: candidates are users followed by someone `user` follows ("friends of friends"), **excluding** `user` and anyone `user` already follows. Rank by how many of `user`'s followees follow the candidate (more = better); ties by id ascending. Return at most `limit` ids.

## Example

`me → a`, `me → b`, `a → x`, `a → y`, `b → x`. Suggestions for `me`: `x` (2 of my followees follow x), then `y` (1).

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`follow()` / `unfollow()`** — update BOTH maps. 🎯 `followAndUnfollowReturnWhetherTheGraphChanged`, `unfollowRemovesBothDirections`
2. **`followers()` / `following()`**. 🎯 `followersAndFollowingAreMirrorImages`
3. **`commonFollowing()`**. 🎯 `commonFollowingIsTheIntersection`
4. **`suggestions()`**. 🎯 `suggestionsRankByNumberOfFolloweesWhoFollowTheCandidate`, `suggestionsExcludeSelfAndAlreadyFollowed`, `suggestionTiesAreBrokenByIdAndLimitApplies`, `noFolloweesMeansNoSuggestions`, `largerGraphScoresCorrectly`

## Think about (answer in NOTES.md)

- A user follows 5,000 accounts and each of those follows 5,000 more. How big is the 2-hop set? How would you limit the work?
- Where would you keep the graph if it does not fit on one machine, and which operations become expensive?

---

## Run the tests

```bash
./gradlew :l2-instagram-follow-graph:test
```

Your code goes in `src/main/kotlin/FollowGraph.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
