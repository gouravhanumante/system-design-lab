# Hints — Offline sync and conflict copies

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

The whole design hangs on one question the server asks: *"was your edit made on top of what I currently have?"* The revision number answers it.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Server: `TreeMap<String, Versioned>`. Client: `TreeMap<String, Local(content, base, dirty)>` — sorted maps make the "smallest dirty name" rule easy.

</details>

<details><summary>Hint 3 — the approach</summary>

Implement `SyncServer.push` first and test it alone. In `sync()` use a `while (true)` that finds the first dirty entry, pushes, and handles the two outcomes; then a simple loop over `server.snapshot()` for the pull phase.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

With many devices per user, a single revision number is not enough to know *how* two versions are related. Version vectors tell you whether one version descends from another or they are truly concurrent.

</details>
