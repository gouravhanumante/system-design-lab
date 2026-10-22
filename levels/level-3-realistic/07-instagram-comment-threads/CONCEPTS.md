# Concepts — Nested comments with soft delete

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Tree data structure

Nested comments form a **tree**: each comment has one parent (or none, at the top level) and any number of replies. Reddit, Hacker News and email threads are trees too.

## Depth-first traversal

To display a thread you visit a comment, then all of its replies (and their replies) before moving to the next sibling. That's **depth-first** order, naturally written as recursion. Depth also gives the indentation level.

## Soft delete

A **soft delete** marks something as deleted instead of removing it. The data stays, so replies keep their parent, history and audit trails are preserved, and undo is possible. The cost: every read must remember to filter deleted items.

## Pruning

A deleted comment with no living replies carries no information, so it is **pruned** (not shown). Deciding that needs information from below (does any descendant survive?), which is a bottom-up calculation — a typical recursive pattern.

## Adjacency list vs. nested sets

Databases usually store trees as rows with `parent_id` (**adjacency list**). Loading a whole tree then needs recursive queries (`WITH RECURSIVE` in SQL). Alternatives like *materialized paths* (`/1/4/9/`) or *closure tables* make subtree queries faster at the cost of more complex writes.
