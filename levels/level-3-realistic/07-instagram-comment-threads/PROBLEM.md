# Nested comments with soft delete

| App | Level | Concept |
|-----|-------|---------|
| Instagram | 3 — Realistic | Tree of replies, depth-first rendering, pruning deleted branches |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Comments on a post form a **tree**: replies to replies. If someone deletes a comment that already has replies, the replies must stay (otherwise a whole conversation vanishes), so the deleted comment becomes a placeholder. A placeholder with nothing alive beneath it is just clutter and should disappear.

## Build this (small scale)

A comment store that renders a post's discussion as indented lines.

## API

```kotlin
class CommentThreads {
    fun add(postId: String, commentId: String, author: String, text: String, parentId: String? = null)
    fun delete(commentId: String)
    fun render(postId: String): List<String>
    fun count(postId: String): Int
}
```

## Rules

- `add`: a duplicate `commentId` → `IllegalArgumentException`. If `parentId` is given it must exist **and belong to the same post**, else `IllegalArgumentException`. Replies may be added to deleted comments too.
- `delete` marks the comment deleted (**soft delete**). Unknown `commentId` → `IllegalArgumentException`. Deleting twice is fine.
- `render(postId)` returns one line per visible comment, depth-first, children in the order they were added. A line is `"  " * depth + "<author>: <text>"` (two spaces per level, top-level comments have none).
  - A **deleted** comment is shown as `"  " * depth + "[deleted]"` **only if** at least one comment below it (at any depth) is **not** deleted. Otherwise it (and its all-deleted subtree) is omitted.
- `count(postId)` = number of comments of that post that are **not** deleted.
- Unknown post → `render` is empty and `count` is `0`.

## Example

`c1` (ann: "nice") has reply `c2` (bob: "thanks") which has reply `c3` (ann: "welcome"). Delete `c2` → render:

```
ann: nice
  [deleted]
    ann: welcome
```

Then delete `c3` too → only `ann: nice` remains.

## Think about (answer in NOTES.md)

- A viral post has 300,000 comments. `render` returns all of them — what should the API really do (pagination? "view 12 more replies"?), and how does that change how you store the tree?
- Instead of storing a `children` list you could store `parentId` only. What gets easier and what gets harder?

---

## Run the tests

```bash
./gradlew :l3-instagram-comment-threads:test
```

Your code goes in `src/main/kotlin/CommentThreads.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
