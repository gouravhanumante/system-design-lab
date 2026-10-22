# Hints — Product page cache (cache-aside, stale-while-revalidate)

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Write a tiny helper that classifies an entry as FRESH, STALE or DEAD for a given `now`. `get` becomes a `when` over that.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`HashMap<String, Entry(value: Product?, loadedAt: Long)>` and a `LinkedHashSet<String>` for the refresh queue (keeps order, no duplicates).

</details>

<details><summary>Hint 3 — the approach</summary>

Negative entries (`value == null`) have no stale period: after `negativeTtlSeconds` they are DEAD.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

With many app servers each having its own cache, an update must invalidate **all** of them — often via a message on a pub/sub channel, or by putting the cache in a shared Redis/Memcached tier.

</details>
