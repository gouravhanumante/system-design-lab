# Hints — Continue watching

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Each (profile, title) pair has a tiny record: position, duration, last watched time.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`HashMap<String, HashMap<String, Entry>>` — profile → title → entry.

</details>

<details><summary>Hint 3 — the approach</summary>

Keep finished titles in the map if you like, but filter them out in `row` and return 0 in `resumePosition`. Sort the rest by `(-lastWatched, titleId)`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Last-write-wins by *client timestamp* fixes the TV/phone race only if clocks agree. Many systems use the server receive time instead.

</details>
