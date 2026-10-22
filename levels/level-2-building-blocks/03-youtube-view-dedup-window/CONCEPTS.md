# Concepts — Count a view only once per viewer

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## De-duplication

**De-duplication** means recognising that two events are "the same thing" and counting them once. You need a **key** that defines sameness (here: user + video) and a rule for how long the memory lasts (a time window). It's used for view counts, ad clicks, payment requests and notifications.

## Composite keys

When sameness depends on two values, the key must contain both. Gluing strings like `"$user|$video"` breaks if ids can contain `|` — `("a|b", "c")` and `("a", "b|c")` both become `"a|b|c"`. Use a structured key like `Pair(user, video)` or a data class, which compares both parts separately.

## Sliding windows per key

Each (user, video) pair has its own window: "has this pair been counted in the last 30 minutes?". Only storing the **last counted time** is enough. Notice the subtle rule: failed attempts must not move the window, otherwise someone spamming refresh would never be counted again.

## Expiring old state

Remembering every pair forever costs unbounded memory. Once an entry is older than the window it can never affect a decision again, so it can be deleted — typically by giving it a **TTL** in a store like Redis.
