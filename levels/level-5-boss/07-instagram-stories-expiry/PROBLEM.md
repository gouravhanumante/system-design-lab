# Stories: 24-hour expiry, audiences and highlights

| App | Level | Concept |
|-----|-------|---------|
| Instagram | 5 — Boss | TTL, audience rules evaluated at read time, unique viewers, exceptions to expiry |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Stories vanish after 24 hours — unless the owner adds them to a **highlight**. Each story is shown to **everyone** or only to **close friends**, and the owner sees the list of people who viewed it (each person once, in order). Access rules are evaluated **at the moment someone looks**: remove a close friend and they instantly lose access, but their earlier view stays in the viewer list.

## Build this (small scale)

A stories service. Time is passed in (`now`, seconds).

## API

```kotlin
enum class Audience { EVERYONE, CLOSE_FRIENDS }

class Stories(ttlSeconds: Long = 86_400) {
    fun addCloseFriend(owner: String, friend: String)
    fun removeCloseFriend(owner: String, friend: String)
    fun post(owner: String, storyId: String, audience: Audience, now: Long)
    fun canView(viewer: String, storyId: String, now: Long): Boolean
    fun view(viewer: String, storyId: String, now: Long): Boolean
    fun viewers(storyId: String): List<String>
    fun visibleStories(owner: String, viewer: String, now: Long): List<String>
    fun highlight(owner: String, storyId: String, now: Long): Boolean
}
```

## Rules

- A story posted at `t` is **expired** when `now - t >= ttlSeconds`, unless it has been **highlighted**. Posting an existing `storyId` again is ignored.
- `canView(viewer, storyId, now)` is `true` when the story exists, is not expired (or is highlighted), **and**
  - `viewer` is the owner, or
  - audience is `EVERYONE`, or
  - audience is `CLOSE_FRIENDS` and `viewer` is **currently** in the owner's close-friends list.
- `view` returns `canView(...)`. When it is `true`, the viewer is not the owner, and has not viewed this story before, the viewer is appended to the story's viewer list. The owner viewing their own story is never recorded.
- `viewers(storyId)` lists unique viewers in the order of their first view (unknown story → empty). It is not affected by expiry or by later changes to close friends.
- `visibleStories(owner, viewer, now)` returns the ids of `owner`'s stories that `viewer` can view right now, oldest first (ties by id).
- `highlight(owner, storyId, now)` returns `true` and makes the story permanent if the story exists, belongs to `owner`, and is currently **not expired** (an expired story cannot be rescued). Otherwise `false`. Highlighting twice is fine (`true` while viewable).

## Example

TTL 100. `ann` posts `s1` at 0 for close friends and `bob` is one. At 50 bob can view. `ann` removes bob → bob cannot view, but `viewers("s1")` still contains bob. At 100 even ann cannot view `s1` — unless she highlighted it before.

## Think about (answer in NOTES.md)

- Expiry: do you delete expired stories with a background job, check the clock on every read, or both? What are the failure modes of each?
- "Who viewed my story" for a user with 10 million followers: what do you store, and what can you approximate?
- Close-friends changes are applied at read time here. What would it take to apply them *retroactively* to a cached feed of stories?

---

## Run the tests

```bash
./gradlew :l5-instagram-stories-expiry:test
```

Your code goes in `src/main/kotlin/Stories.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
