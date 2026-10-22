# Concepts — Folders, files and total size

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Tree data structure

A **tree** is nodes connected parent → children with one root and no loops. File systems, comment threads, org charts and the Android view hierarchy are all trees. A folder is an inner node; a file is a leaf.

## Path parsing

A path like `/photos/2026/cat.jpg` is a list of names separated by `/`. Splitting it and walking from the root one name at a time finds the node. Handle the edge cases explicitly: the root `/`, empty segments, a missing name halfway.

## Recursion

A folder's size = sum of its children's sizes, and a child folder's size is computed the same way. A function calling itself on smaller parts is **recursion**. Every recursive function needs a base case (a file returns its own size). Very deep trees can overflow the call stack; an explicit stack (iteration) avoids that.

## Sorted maps

A `TreeMap` keeps keys in sorted order, so listing a folder alphabetically costs nothing extra. A `HashMap` is faster for lookups but has no order. Pick the structure based on the operations you need most.
