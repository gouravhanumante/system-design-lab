# Concepts — Likes that can't be double-counted

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Sets and idempotency

A **set** contains each element at most once. Adding something already present does nothing. That makes "like" naturally **idempotent**: tapping twice, or the app retrying the request, can't create two likes.

## Insertion-ordered collections

`LinkedHashSet` / `LinkedHashMap` remember the order in which elements were added. Removing and re-adding an element moves it to the end. That's an easy way to get "most recent first" without storing timestamps.

## Return values that report change

Returning `true` only when something actually changed lets callers react correctly: update a cached count, send "X liked your photo" only once, or skip work. Many APIs (Java's `Set.add`, Redis `SADD`) return exactly this.

## Exact vs. approximate counting

For a post with millions of likes, storing every liker is expensive but needed for "did I like this?". The visible **count**, though, can come from a separate counter that may lag slightly. For "unique visitors" style counts where exact membership isn't needed, **HyperLogLog** estimates the count using a few kilobytes.
