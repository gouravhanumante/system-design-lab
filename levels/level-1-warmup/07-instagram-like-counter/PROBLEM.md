# Likes that can't be double-counted

| App | Level | Concept |
|-----|-------|---------|
| Instagram | 1 — Warm-up | Idempotent set membership, recency ordering |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Double-tapping a photo likes it. Double-tapping again (or the app retrying a request on a bad connection) must **not** add a second like. And "Liked by sara and 1,203 others" needs the most recent likers.

## Build this (small scale)

A like service where each user can like a post at most once.

## API

```kotlin
class LikeService {
    fun like(userId: String, postId: String): Boolean       // true if this call changed something
    fun unlike(userId: String, postId: String): Boolean     // true if this call changed something
    fun likeCount(postId: String): Int
    fun hasLiked(userId: String, postId: String): Boolean
    fun likedBy(postId: String): List<String>               // most recent like first
}
```

## Rules

- `like` of a post the user already likes returns `false` and changes nothing (the like keeps its original position).
- `unlike` of a post the user does not like returns `false`.
- `likedBy` lists current likers, **most recent like first**. A user who unlikes and likes again counts as the most recent.
- A post nobody liked has count `0` and an empty list.

## Example

`ann` likes, `bob` likes, `ann` unlikes, `ann` likes again → `likedBy = ["ann", "bob"]`, count `2`.

## Think about (answer in NOTES.md)

- Why return a `Boolean` from `like`? (What would a client or a counter-cache do with it?)
- A celebrity post gets 2 million likes in an hour. Do you keep a `Set` of 2 million user ids? What would you store instead, and what do you lose?

---

## Run the tests

```bash
./gradlew :l1-instagram-like-counter:test
```

Your code goes in `src/main/kotlin/LikeService.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
