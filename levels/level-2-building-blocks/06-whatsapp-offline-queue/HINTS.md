# Hints — Delivering messages to offline users

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

There are two different paths: "recipient is online → deliver now" and "recipient is offline → remember for later". Both need the same duplicate check.

</details>

<details><summary>Hint 2 — which data structure?</summary>

Per user: an `ArrayDeque` of queued `(messageId, text, sentAt)`, a `delivered` list, and a set of seen message ids. Plus a set of online users.

</details>

<details><summary>Hint 3 — the approach</summary>

Check/insert the message id in the user's seen-set first. Then branch: online → append to delivered; offline → add to the queue. On `connect`, pop from the front of the queue, skip expired ones.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

WhatsApp removes a message from the server as soon as it is delivered. What does that buy you (storage, privacy), and what does it cost (multi-device history)?

</details>
