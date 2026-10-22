# Hints — Folders, files and total size

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

A folder is a thing with named children; a file is a thing with a size. Both can be one small class.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `Node` with `isFolder`, `size`, and `children: TreeMap<String, Node>` (the sorted map gives you alphabetical order for free).

</details>

<details><summary>Hint 3 — the approach</summary>

Write one private helper that splits a path into names and walks from the root. `mkdir`/`createFile` walk to the *parent*, then add. `totalSize` is a recursive sum.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Real Drive does not store paths. Every item has an id and a `parentId`, so renaming a folder changes one row instead of millions of paths.

</details>
