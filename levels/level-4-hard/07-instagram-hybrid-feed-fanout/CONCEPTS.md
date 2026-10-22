# Concepts — Home feed: push, pull, or both?

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Fan-out on write (push)

When someone posts, write the post id into **every follower's** precomputed feed. Reads are then a simple lookup — fast. Writes cost one operation per follower, which is fine for normal users but enormous for celebrities.

## Fan-out on read (pull)

Store each post once. When a user opens their feed, **gather** recent posts from everyone they follow and merge them. Writes are cheap; reads are expensive, especially for people following thousands of accounts.

## The celebrity problem (hybrid fan-out)

Twitter and Instagram combine both: **push** for ordinary accounts, **pull** for accounts with huge follower counts, merging both at read time. The threshold is a tuning knob. It's a classic example of handling **skewed** data: a few keys behave very differently from the rest.

## Merging sorted streams and de-duplication

A feed merges several sources sorted by time. A post may come from both sources (pushed earlier, pulled now), so merging must **de-duplicate** by id. Using a map keyed by id, then sorting, is the simple version; a k-way merge with a heap is the efficient one.

## Write amplification

One logical action (posting) causing many physical writes is called **write amplification**. Every fan-out design is a trade between write amplification and read cost.
