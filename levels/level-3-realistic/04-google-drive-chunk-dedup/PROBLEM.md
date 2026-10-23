# Deduplicated chunk storage

| App | Level | Concept |
|-----|-------|---------|
| Google Drive | 3 — Realistic | Content-addressed chunks, reference counting, garbage collection |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Millions of people upload the same PDF, the same installer, the same meme. Storing every copy would waste enormous space. Drive-like systems cut files into **chunks**, identify each chunk by its *content*, and store each distinct chunk **once**. A chunk is only deleted when no file refers to it any more.

## Build this (small scale)

A store that splits files into fixed-size chunks, keeps one copy per distinct chunk, and tracks how many files (and how many places inside files) use each chunk.

## API

```kotlin
class DedupStore(chunkSize: Int) {
    fun put(fileName: String, data: ByteArray)
    fun get(fileName: String): ByteArray
    fun delete(fileName: String)
    fun storedBytes(): Long      // bytes of distinct chunks actually kept
    fun logicalBytes(): Long     // sum of the sizes of all files as the user sees them
}
```

## Rules

- A file is cut into chunks of `chunkSize` bytes; the last chunk may be shorter. Two chunks are "the same" if their bytes are equal (a short final chunk is only equal to an identical short chunk).
- Each distinct chunk is stored once. If it appears several times — in one file or in many — it is still stored once.
- `put` of an existing `fileName` **replaces** the file (the old file's chunks are released first).
- `get` returns exactly the bytes that were stored. Unknown name → `IllegalArgumentException`.
- `delete` removes the file and frees every chunk no remaining file uses. Unknown name → nothing happens.
- An empty file is allowed: it has no chunks.

## Example

`chunkSize = 4`. `put("f1", "AAAABBBB")`, `put("f2", "AAAACCCC")` → distinct chunks `AAAA`, `BBBB`, `CCCC` → `storedBytes = 12`, `logicalBytes = 16`. `delete("f1")` → `BBBB` is freed, `AAAA` stays → `storedBytes = 8`.

## Think about (answer in NOTES.md)

- Fixed-size chunking breaks if someone inserts **one byte at the start** of a file: every chunk shifts. How does *content-defined chunking* (rolling hash) fix this?
- Two different chunks could, very rarely, have the same hash. What happens if you identify chunks by hash only? Is that acceptable?
- Dedup across *users* has a privacy side-channel ("is this file already stored?"). What is it?

---

## Run the tests

```bash
./gradlew :l3-google-drive-chunk-dedup:test
```

Your code goes in `src/main/kotlin/DedupStore.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
