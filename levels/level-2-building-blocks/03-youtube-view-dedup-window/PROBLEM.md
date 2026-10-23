# Count a view only once per viewer

| App | Level | Concept |
|-----|-------|---------|
| YouTube | 2 — Building blocks | Per-key timestamps, windowed de-duplication |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

If hitting refresh 50 times gave a video 50 views, creators (and bots) would cheat in seconds. YouTube counts a view only if it looks like a *new* watch: the same viewer replaying within a short window does not count again.

## Build this (small scale)

Count a view for `(user, video)` only if that user has not had a **counted** view of that video within the last `windowSeconds`. Time is passed in (`now`, seconds).

## API

```kotlin
class ViewTracker(windowSeconds: Long = 1800) {
    fun watch(userId: String, videoId: String, now: Long): Boolean   // true if it was counted
    fun views(videoId: String): Long
}
```

## Rules

- The first watch of a video by a user is always counted.
- A later watch by the same user of the same video is counted only if `now - lastCountedTime >= windowSeconds`. When it is counted, the window restarts from `now`.
- Watches that are **not** counted do **not** extend or restart the window (spamming refresh must not keep you "blocked" forever).
- Different users and different videos never affect each other.
- `now` never goes backwards.

## Example

Window 1800. User `u` watches `v` at `0` ✔ counted, at `100` ✘, at `1799` ✘, at `1800` ✔ counted (window restarts at 1800), at `2000` ✘.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`views()`** and the `counts` map.
2. **`watch()`** — look up when this user's view of this video was last counted. 🎯 all tests (`firstWatchCounts`, `repeatWithinWindowDoesNotCount`, `countsAgainExactlyAtTheWindowEdgeAndWindowRestarts`, `uncountedWatchesDoNotExtendTheWindow`, `usersAndVideosAreIndependent`, `separatorCharactersInIdsDoNotCollide`)

## Think about (answer in NOTES.md)

- Memory: you now store one timestamp per `(user, video)` pair. With a billion users that is too much. What can you throw away, and when?
- What other signals would a real system use besides time (watch duration, IP, device)?

---

## Run the tests

```bash
./gradlew :l2-youtube-view-dedup-window:test
```

Your code goes in `src/main/kotlin/ViewTracker.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
