# Concepts — Match many requests to many captains

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Greedy algorithms

A **greedy** algorithm makes the best-looking choice at each step and never reconsiders. "Always take the globally closest remaining (rider, captain) pair" is greedy. Greedy algorithms are simple and fast, and often good enough — but not always optimal. Knowing when "good enough" is acceptable is a core system-design skill.

## Bipartite matching

When you match items from two groups (riders ↔ captains, jobs ↔ workers), you have a **bipartite matching** problem. Finding the assignment with the **minimum total distance** is the *assignment problem*, solved exactly by the **Hungarian algorithm** in O(n³) — too slow for thousands of people every few seconds, which is why dispatch systems use greedy or approximate methods.

## Batching decisions

Processing requests one at a time (first-come-first-served) is easy but makes locally good, globally bad choices. **Batching** — collecting requests for a short window (say 2 seconds) and deciding together — gives better overall results at the cost of a small delay. Many systems trade a little latency for a better global outcome: ride matching, ad auctions, database write batching.

## Sorting all pairs

If you list every possible pair and sort them by cost, walking the list once gives you the greedy matching: take a pair if both sides are still free. With n riders and m captains this is n×m pairs — fine for a few hundred, too many for a whole city, which is where a spatial index helps.
