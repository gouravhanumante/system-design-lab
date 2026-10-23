# Resumable chunked upload

| App | Level | Concept |
|-----|-------|---------|
| YouTube | 3 — Realistic | Out-of-order chunks, idempotent retries, integrity checks |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

A 4 GB video upload over a flaky mobile connection will fail somewhere in the middle. YouTube (and S3, Drive…) therefore split files into **chunks**, upload them independently — possibly in parallel and out of order — and let the client **resume** by asking "which chunks are you still missing?". Each chunk carries a checksum so corruption is caught early.

## Build this (small scale)

The server-side bookkeeping for one upload session.

## API

```kotlin
enum class ReceiveResult { ACCEPTED, DUPLICATE, CORRUPT, OUT_OF_RANGE }

fun checksumOf(data: ByteArray): Long          // provided for you in the starter file

class ChunkedUpload(totalChunks: Int) {
    fun receive(index: Int, data: ByteArray, checksum: Long): ReceiveResult
    fun missingChunks(): List<Int>
    fun isComplete(): Boolean
    fun assemble(): ByteArray
}
```

## Rules

- Chunk indexes are `0 until totalChunks`. Anything else → `OUT_OF_RANGE` (nothing stored).
- If `checksum != checksumOf(data)` → `CORRUPT` (nothing stored; the client will retry).
- If that chunk was already stored → `DUPLICATE` (the **first** stored copy wins; the new data is ignored).
- Otherwise store it and return `ACCEPTED`.
- `missingChunks()` lists the indexes not yet stored, ascending. `isComplete()` is true when none are missing.
- `assemble()` returns all chunks concatenated **in index order**. If the upload is incomplete it throws `IllegalStateException`.
- Chunks may be of different sizes (the last one is usually smaller).

## Example

3 chunks. Receive index 2, then 0 → `missingChunks() = [1]`. Receive 1 → `isComplete()` and `assemble()` returns chunk0 + chunk1 + chunk2.

## Think about (answer in NOTES.md)

- Why is making `receive` **idempotent** (duplicates are harmless) so important on a bad network?
- Where would the chunk bytes actually live in a real system? What would the session object store instead?
- What if the same upload session is touched by two servers at once?

---

## Run the tests

```bash
./gradlew :l3-youtube-chunked-resumable-upload:test
```

Your code goes in `src/main/kotlin/ChunkedUpload.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
