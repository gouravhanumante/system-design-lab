# Home feed: push, pull, or both?

| App | Level | Concept |
|-----|-------|---------|
| Instagram | 4 — Hard | Fan-out on write vs read, the celebrity problem, merge and de-duplicate |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

When you post, your followers' feeds must show it. Two strategies:

- **Push (fan-out on write):** copy the post id into every follower's precomputed feed. Reading a feed is instant, but a post by someone with 100 million followers means 100 million writes.
- **Pull (fan-out on read):** store the post once; when a user opens their feed, fetch recent posts from everyone they follow. Cheap to write, expensive to read.

Real systems use **both**: push for ordinary accounts, pull for celebrities, and merge at read time.

## Build this (small scale)

A feed service that pushes posts of ordinary authors and *pulls* posts of celebrity authors, counting how many push-writes it did.

## API

```kotlin
class FeedService(celebrityThreshold: Int = 3) {
    fun follow(follower: String, followee: String, now: Long)
    fun post(author: String, postId: String, timestamp: Long)
    fun feed(user: String, limit: Int): List<String>
    val pushWrites: Int
}
```

## Rules

- A user becomes a **celebrity** the moment their follower count reaches `celebrityThreshold`. It is **sticky** (never reverts, even if followers leave — there is no unfollow here anyway).
- `follow` records the time `now`. Following yourself or following someone twice does nothing.
- `post(author, postId, timestamp)`:
  - always stores the post in the author's list;
  - if the author is **not** a celebrity: **push** the post id into the precomputed feed of **each current follower**, and increase `pushWrites` by one per follower;
  - if the author **is** a celebrity: do **nothing else** (no pushes).
- `feed(user, limit)` returns post ids, **newest first** (larger timestamp first; ties by post id ascending), at most `limit`, containing:
  - everything pushed to that user, and
  - for every **celebrity** the user follows: that celebrity's posts with `timestamp >= the time the user followed`.
  The two sources are merged and **de-duplicated** by post id.
- A user never sees posts made before they followed the author.
- Calls are made in chronological order.

## Example

Threshold 3. `eve` has 2 followers and posts `e1` → 2 push-writes. A third user follows → `eve` is now a celebrity. `eve` posts `e2` → no push-writes; followers still see `e2` when they open their feed (pulled).

## Think about (answer in NOTES.md)

- What is the break-even: at what follower count and read rate does pull beat push? Who reads their feed far more often than others post?
- Inactive users: pushing into the feed of someone who has not opened the app in a year is wasted work. What would you do?
- What should happen to the already-pushed posts if an account becomes a celebrity later?

---

## Run the tests

```bash
./gradlew :l4-instagram-hybrid-feed-fanout:test
```

Your code goes in `src/main/kotlin/FeedService.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
