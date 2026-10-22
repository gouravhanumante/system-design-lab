# Concepts — Database indexes vs full table scans

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Full table scan

Without an index, a query must read **every row** and check the condition: O(n). Fine for 1,000 rows, terrible for 100 million.

## Indexes

An **index** is an extra data structure that maps column values to rows, so the database can jump straight to matching rows. Like a book's index: you don't read the whole book to find "Dijkstra".

## Sorted (B-tree) vs. hash indexes

- A **sorted index** (B-tree in real databases) supports equality **and** ranges (`age BETWEEN 25 AND 30`, `ORDER BY`), because neighbouring values are stored next to each other.
- A **hash index** supports only equality (`city = 'Pune'`), but very fast.

B-trees keep many keys per node so a lookup in a billion rows touches only 3–4 disk pages.

## Binary search

Finding a value in a sorted list by repeatedly halving the search range takes O(log n): about 30 steps for a billion items. Sorted indexes are built on this idea.

## The cost of indexes

Every insert, update and delete must also update every index, and indexes use disk and memory. Add indexes for the queries you actually run; remove ones nobody uses.

## Selectivity

An index helps when it narrows results a lot (**high selectivity**: email, user id). An index on a column with two values (gender, is_active) usually doesn't help — the database would read half the table anyway.
