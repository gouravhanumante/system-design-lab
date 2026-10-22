# Concepts — Product page cache (cache-aside, stale-while-revalidate)

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Cache-aside (lazy loading)

The application checks the cache first; on a **miss** it loads from the database and stores the result in the cache. The cache only contains data that was actually requested. This is the most common caching pattern (Redis/Memcached in front of a database).

## TTL and staleness

A **TTL** bounds how old cached data can be. Short TTLs give fresher data and more database load; long TTLs the opposite. Every cache is a trade-off between freshness and load.

## Invalidation on write

When data changes, the cached copy must go. Writing the database **first** and then **deleting** the cache entry (instead of updating it) is the safer default: if two updates race, the worst case is an extra miss, not a stale value stuck in the cache. "There are only two hard things in computer science: cache invalidation and naming things."

## Negative caching

Requests for things that don't exist (bad links, bots probing ids) would always miss and hit the database. Caching "not found" for a short time (**negative caching**) protects the database. Keep the negative TTL short so newly created items appear quickly.

## Stale-while-revalidate

Serve the slightly old value **immediately** and refresh it in the background. Users never wait for the database on a hot key, and the refresh happens once, not once per waiting user. HTTP has this as a `Cache-Control` directive too.

## Cache stampede

When a popular entry expires, thousands of requests miss at the same moment and all hit the database — a **stampede** (or *dog-pile*). Remedies: stale-while-revalidate, request coalescing (only one request reloads; others wait for it), and jittered TTLs.
