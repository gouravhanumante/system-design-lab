# Concepts — Version history and restore

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Append-only history

Instead of overwriting, every save **adds** a new version. Old versions stay readable. This is the idea behind Git, database audit logs and event sourcing: history is never edited, only extended. Restoring an old version is *also* an append (a new version with old content), so even a mistaken restore can be undone.

## Retention policies

Storage isn't free, so systems keep only the newest N versions, or versions younger than X days. A **retention policy** decides what to drop. Dropping must never break identity: version numbers are never reused, otherwise "version 3" could mean two different things.

## Monotonic ids

A counter that only ever goes up gives each item a unique, ordered id. Keep it separate from the list of stored items: when old items are deleted, the counter keeps going. Databases do this with auto-increment / sequence columns.

## Snapshots vs. diffs

Storing each version's full content is simple but wastes space. Storing only the **difference** from the previous version is compact, but reading an old version means replaying many diffs. Real systems mix both: a full snapshot every so often, diffs in between (Git packfiles, video keyframes).
