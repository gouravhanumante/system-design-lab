# Concepts — Deduplicated chunk storage

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Content-addressed storage

In **content-addressed** storage, data is identified by **what it contains** (usually a hash of the bytes), not by a name. Identical content → identical id → stored once. Git, Docker image layers and IPFS work like this.

## Hash functions

A hash function turns any amount of data into a short fixed-size fingerprint (SHA-256 gives 32 bytes). Good hashes make it astronomically unlikely that two different inputs share a fingerprint (a **collision**), so in practice "same hash = same content". In this problem you can use the bytes themselves as the key to keep things simple.

## Reference counting

When many files share a chunk, you can only delete the chunk when **nobody** uses it anymore. A **reference count** tracks how many users a chunk has: +1 when a file starts using it, −1 when a file stops, delete at 0. Bugs (forgetting a decrement, decrementing twice) leak storage or delete data still in use — the two classic garbage-collection failures.

## Chunking strategies

**Fixed-size** chunks are simple, but inserting one byte at the start shifts every chunk boundary, so nothing deduplicates. **Content-defined chunking** picks boundaries where a rolling hash of the data matches a pattern, so boundaries move with the content and most chunks stay identical. Backup tools (restic, borg) and Dropbox use this.
