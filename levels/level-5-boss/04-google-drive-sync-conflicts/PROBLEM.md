# Offline sync and conflict copies

| App | Level | Concept |
|-----|-------|---------|
| Google Drive | 5 — Boss | Optimistic concurrency with revisions, conflict detection, convergence |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

You edit a document on your laptop on a plane while your phone (offline too) edits the same file. When both come back online, whose version wins? Dropbox and Drive don't silently overwrite: the server accepts a change only if it was based on the **latest** revision. Otherwise the change is saved as a **conflict copy** ("report (conflict laptop).txt") and nothing is lost.

## Build this (small scale)

A server that accepts or rejects pushes based on revision numbers, and clients that edit offline, then `sync()`.

## API

```kotlin
data class Versioned(val content: String, val revision: Int)

sealed class PushResult {
    data class Accepted(val revision: Int) : PushResult()
    data class Conflict(val serverCopy: Versioned) : PushResult()
}

class SyncServer {
    fun push(name: String, baseRevision: Int, content: String): PushResult
    fun fetch(name: String): Versioned?
    fun snapshot(): Map<String, Versioned>          // every file on the server
}

class SyncClient(device: String, server: SyncServer) {
    fun edit(file: String, content: String)         // local change, works offline
    fun read(file: String): String?
    fun files(): Set<String>
    fun sync()
}
```

## Rules — server `push(name, baseRevision, content)`

- File **unknown** to the server: if `baseRevision == 0` create it as revision `1` → `Accepted(1)`. If `baseRevision != 0` throw `IllegalArgumentException`.
- File exists and `content` equals the server's content → `Accepted(currentRevision)` (nothing changes, no new revision).
- File exists, content differs, and `baseRevision == currentRevision` → store it as `currentRevision + 1` → `Accepted(newRevision)`.
- Otherwise → `Conflict(serverCopy)` and nothing changes.

## Rules — client

- The client remembers, per file: its content, the `baseRevision` it last synced from the server (`0` for a file the server never saw) and whether it is **dirty** (changed locally since last sync).
- `edit`: new file → dirty with base `0`. Existing file: if `content` equals the current local content do nothing; else set it and mark dirty.
- `sync()` has two phases:
  1. **Push**: repeatedly take the dirty file with the alphabetically smallest name and push it with its base revision.
     - `Accepted(r)` → base = `r`, no longer dirty.
     - `Conflict(copy)` → keep the local version as a **new dirty file** (base `0`) named `"<name> (conflict <device>)"` (if that name is already used locally, try `"<name> (conflict <device>) 2"`, `… 3`, …), and replace the original file by the server's copy (`copy.content`, base `copy.revision`, not dirty). Keep pushing until no file is dirty.
  2. **Pull**: for every file on the server (`snapshot`): if the client has no such file, add it (clean). If the client's file is **not dirty** and its base is older than the server revision, update it. (After the push phase nothing is dirty.)
- `files()` is the set of file names the client has; `read` returns the content or `null`.

## Example

Laptop and phone both have `a.txt` at revision 1. Laptop edits and syncs → revision 2. The phone (still offline) edits and syncs → its push has base 1 ≠ 2 → `Conflict`. Phone now has `a.txt` = laptop's text and `a.txt (conflict phone)` = its own edit, which gets pushed as a brand-new file. Nothing was lost.

## Think about (answer in NOTES.md)

- Why a conflict **copy** instead of "last write wins"? When would last-write-wins be acceptable (and for what kind of data)?
- Text documents can be **merged** automatically (Google Docs uses OT / CRDTs). What is the difference between detecting conflicts and merging?
- What if a client crashes between receiving `Accepted` and saving the new revision locally? How would you make the protocol safe against that?

---

## Run the tests

```bash
./gradlew :l5-google-drive-sync-conflicts:test
```

Your code goes in `src/main/kotlin/Sync.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
