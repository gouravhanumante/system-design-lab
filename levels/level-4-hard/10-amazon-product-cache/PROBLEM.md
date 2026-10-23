# Product page cache (cache-aside, stale-while-revalidate)

| App | Level | Concept |
|-----|-------|---------|
| Amazon | 4 — Hard | Cache-aside, TTL, invalidation on write, negative caching, background refresh |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Product pages are read millions of times and changed rarely. Hitting the database for every view is wasteful, so pages read through a **cache**. The classic problems: stale prices after an update, repeated database hits for products that don't exist (bots love those), and a slow page for the unlucky user who arrives just as an entry expires. Three standard answers: **invalidate on write**, **negative caching**, and **stale-while-revalidate** (serve the slightly old copy, refresh it in the background).

## Build this (small scale)

A cache in front of a provided `ProductDb`. Time is passed in seconds.

## API

```kotlin
data class Product(val id: String, val name: String, val pricePaise: Long)

interface ProductDb {                                  // provided
    fun load(id: String): Product?
    fun save(p: Product)
}

class ProductCache(db: ProductDb, ttlSeconds: Long = 60, staleSeconds: Long = 30, negativeTtlSeconds: Long = 10) {
    fun get(id: String, now: Long): Product?
    fun update(p: Product, now: Long)
    fun runRefreshes(now: Long): Int
    val hits: Int
    val misses: Int
}
```

## Rules

Each cache entry remembers its value (possibly `null` = "does not exist") and `loadedAt`. Let `age = now − loadedAt`.

- `get(id, now)`:
  - **Fresh hit**: entry with a product and `age < ttlSeconds`, or a `null` entry with `age < negativeTtlSeconds` → return it, `hits + 1`, no database call.
  - **Stale hit**: product entry with `ttlSeconds <= age < ttlSeconds + staleSeconds` → return the old product, `hits + 1`, and put `id` in the **refresh queue** (once — no duplicates). No database call now.
  - Otherwise (**miss**): `misses + 1`, call `db.load(id)`, store the result (including `null`) with `loadedAt = now`, return it.
- `update(p, now)`: call `db.save(p)` **first**, then **remove** the cache entry for `p.id` and remove `p.id` from the refresh queue. (The next `get` loads the new version.)
- `runRefreshes(now)`: for every id in the queue, in the order queued: `db.load(id)` and store with `loadedAt = now`. Empty the queue. Return how many were refreshed.

## Example

TTL 60, stale 30. Loaded at 0. `get` at 59 → fresh. At 70 → stale (old value returned, refresh queued). `runRefreshes(71)` reloads it. At 100 → fresh again (age 29). Without a refresh, at 90 it would be a miss.

## Think about (answer in NOTES.md)

- Why delete the cache entry on update instead of writing the new value into it? (Think of two updates racing.)
- Why must `db.save` happen **before** the cache delete, not after?
- What is a **cache stampede**, and how does stale-while-revalidate reduce it?

---

## Run the tests

```bash
./gradlew :l4-amazon-product-cache:test
```

Your code goes in `src/main/kotlin/ProductCache.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
