# Folders, files and total size

| App | Level | Concept |
|-----|-------|---------|
| Google Drive | 1 — Warm-up | Tree of nodes, path parsing, recursion |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Everything in Google Drive lives in a tree: folders contain folders and files, and "how much space is this folder using?" is a question you ask constantly.

## Build this (small scale)

An in-memory file tree addressed by absolute paths like `/photos/2026/cat.jpg`.

## API

```kotlin
class FileTree {
    fun mkdir(path: String)
    fun createFile(path: String, sizeBytes: Long)
    fun list(path: String): List<String>
    fun exists(path: String): Boolean
    fun totalSize(path: String): Long
}
```

## Rules

- Paths are absolute: they start with `/`, use `/` between names, and have no trailing slash (except the root `"/"` itself). The root always exists and is a folder.
- `mkdir` / `createFile`: the parent must exist **and be a folder**, and the name must not already exist — otherwise throw `IllegalArgumentException`.
- `list(folder)` returns the names of its children sorted alphabetically; **folder names get a trailing `/`**. Listing a file or a missing path throws `IllegalArgumentException`.
- `totalSize(path)`: a file's size, or for a folder the sum of everything inside it (recursively). Missing path → `IllegalArgumentException`.

## Example

`mkdir("/a")`, `createFile("/a/x.txt", 10)`, `mkdir("/a/b")`, `createFile("/a/b/y.txt", 5)` → `list("/a") = ["b/", "x.txt"]`, `totalSize("/a") = 15`.

## Think about (answer in NOTES.md)

- `totalSize` walks the whole subtree every time. How would you make it O(1) for a huge folder — and what does that cost on every write?
- Drive lets one file live in several folders ("shortcuts"). Why does a pure tree stop working?

---

## Run the tests

```bash
./gradlew :l1-google-drive-folder-tree:test
```

Your code goes in `src/main/kotlin/FileTree.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
