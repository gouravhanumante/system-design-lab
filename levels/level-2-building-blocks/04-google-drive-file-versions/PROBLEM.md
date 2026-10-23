# Version history and restore

| App | Level | Concept |
|-----|-------|---------|
| Google Drive | 2 — Building blocks | Append-only history, retention limit, never reusing ids |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Google Docs keeps a version history: you can look at yesterday's draft and restore it. Storage is not infinite, so old versions are eventually dropped. Two details matter: version numbers must **never be reused**, and *restoring* an old version must not destroy the newer ones — it creates a **new** version.

## Build this (small scale)

The version history of a single file.

## API

```kotlin
class VersionedFile(maxVersions: Int = 5) {
    fun save(content: String): Int          // returns the version number that now holds this content
    fun current(): String?                  // null if nothing saved yet
    fun versions(): List<Int>               // retained version numbers, ascending
    fun read(version: Int): String
    fun restore(version: Int): Int          // returns the version number that now holds the restored content
}
```

## Rules

- Version numbers start at `1` and go up by one for every **new** version. They are never reused, even after old versions are dropped.
- `save(content)`: if `content` equals the current content, **no new version** is created — return the current version number. Otherwise create the next version.
- Only the newest `maxVersions` versions are kept. Older ones are dropped.
- `read(v)` of a version that does not exist (never created, or already dropped) → `IllegalArgumentException`.
- `restore(v)` copies the content of version `v` into a **new** version (same rule as `save`: if it equals the current content, nothing new is created). Unknown `v` → `IllegalArgumentException`.

## Example

`maxVersions = 3`. Save `a, b, c, d, e` → versions `[3, 4, 5]`. `restore(3)` creates version `6` with content `"c"` → versions `[4, 5, 6]`.

## Think about (answer in NOTES.md)

- Storing the full text of every version wastes space when each edit changes one word. How would you store only the *difference*? What does it cost to read an old version?
- Should a restore itself be restorable? (It is, here — why is that a good property?)

---

## Run the tests

```bash
./gradlew :l2-google-drive-file-versions:test
```

Your code goes in `src/main/kotlin/VersionedFile.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
