# Timeline pages with a cursor

| App | Level | Concept |
|-----|-------|---------|
| Twitter | 1 — Warm-up | Cursor (keyset) pagination vs offset pagination |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

When you scroll a profile on X/Twitter the app loads 20 tweets, then the next 20, and so on. New tweets keep arriving and old ones get deleted *while you scroll*. If pages were "skip 20, take 20" (offset pagination), a single deletion would shift everything and you would skip or repeat tweets. Real APIs use a **cursor**: "give me 20 tweets older than tweet #9182".

## Build this (small scale)

A per-user timeline that is paged with a cursor.

## API

```kotlin
data class Tweet(val id: Long, val userId: String, val text: String)
data class Page(val tweets: List<Tweet>, val nextCursor: Long?)

class UserTimeline {
    fun post(userId: String, tweetId: Long, text: String)
    fun delete(tweetId: Long)
    fun page(userId: String, cursor: Long?, limit: Int): Page
}
```

## Rules

- Tweet ids are unique across all users (posting an existing id → `IllegalArgumentException`). Bigger id = newer.
- `page(userId, null, limit)` returns the user's newest `limit` tweets, newest first.
- `page(userId, cursor, limit)` returns the newest `limit` tweets with `id < cursor`.
- `nextCursor` is the id of the **last tweet in the page** if the user has at least one more (older) tweet after it; otherwise `null`. An empty page has `nextCursor = null`.
- `delete` removes a tweet (unknown id: nothing happens). Pagination must keep working: a cursor stays valid even if the tweet it points to was deleted.
- `limit <= 0` → empty page.

## Example

User tweets with ids `1..5`. `page(u, null, 2)` → `[5, 4]`, cursor `4`. `page(u, 4, 2)` → `[3, 2]`, cursor `2`. `page(u, 2, 2)` → `[1]`, cursor `null`.

## Think about (answer in NOTES.md)

- Show with a small example how **offset** pagination (`skip 2, take 2`) breaks when a tweet is deleted between two page loads.
- Why do real APIs make the cursor an **opaque string** (e.g. base64) instead of a raw id?
- How would a database answer "newest 20 with id < X" quickly? (Think: index on `(user_id, id)`.)

---

## Run the tests

```bash
./gradlew :l1-twitter-cursor-pagination:test
```

Your code goes in `src/main/kotlin/UserTimeline.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
