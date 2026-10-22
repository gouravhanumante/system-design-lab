# Hints — Message ordering and gap recovery across devices

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Make the client's life easy by always keeping *everything it received*, keyed by `seq`. "Displayed" is then just a view over that map.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Server: per chat a `MutableList<Message>` (index = seq − 1) and a `Map<messageId, Message>`. Client: a `TreeMap<Long, Message>` (or `HashMap`) of received messages.

</details>

<details><summary>Hint 3 — the approach</summary>

Write a private `contiguousUpTo(): Long` on the client that walks `1, 2, 3…` while the key exists. `displayed`, `hasGap` and `sync` all use it. In `sync`, loop: fetch → receive all → `if (batch.size < batchSize) break`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Devices remember the highest contiguous `seq` they have and send it when they reconnect, so the server only ships what is missing — no full history download.

</details>
