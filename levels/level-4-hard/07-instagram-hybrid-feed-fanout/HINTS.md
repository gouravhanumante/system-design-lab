# Hints — Home feed: push, pull, or both?

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

You are building *two* ways to find a feed and one function that combines them. Write the combination last.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`posts: Map<author, List<Post>>`, `followers: Map<author, Map<follower, followTime>>`, `following: Map<user, Set<author>>`, `pushed: Map<user, MutableList<postId>>`, `celebrities: Set<String>`, and a `postInfo: Map<postId, Post>` for timestamps.

</details>

<details><summary>Hint 3 — the approach</summary>

`feed`: collect `pushed[user]` ids; for every followed celebrity add the author's posts newer than the follow time; put everything in a **set** to remove duplicates; sort by `(-timestamp, id)`; take `limit`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Push writes go through a queue and are done asynchronously by workers — a post becomes visible "eventually". How soon must a follower see it?

</details>
