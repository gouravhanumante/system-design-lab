# Concepts — Offline sync and conflict copies

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Optimistic concurrency control

Instead of locking a file while someone edits it (pessimistic), let everyone edit and **check at save time**: "was your edit based on the latest version?". Each version has a **revision number**; a save carries the revision it started from (`baseRevision`). If someone else saved in between, the numbers don't match and the save is rejected. HTTP has this built in (`ETag` + `If-Match`).

## Conflict detection vs. conflict resolution

**Detection** answers "did two people change the same thing concurrently?". **Resolution** decides what to do about it:
- *Last write wins* — simple, silently loses data.
- *Conflict copy* — keep both, let a human decide (Dropbox, Drive for binary files).
- *Merge* — combine automatically (Git for text, CRDTs/OT for live docs).

## Offline-first sync

An **offline-first** client works on local data and syncs later. It must remember, per item: what it last saw from the server (base revision) and whether it has unsynced changes (**dirty** flag). Sync = push local changes, then pull remote changes.

## Convergence

After all devices sync (possibly more than once), they should all show the **same** data. That property is called **convergence** and is the main correctness goal of any sync protocol.

## Version vectors

A single revision number can say "you are behind" but not "you and I edited at the same time from the same base". With many devices, **version vectors** (one counter per device) can tell "A happened before B" from "A and B are concurrent". Amazon Dynamo and Riak use them.
