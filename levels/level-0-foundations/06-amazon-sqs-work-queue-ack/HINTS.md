# Hints — Work queue with acknowledgements

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

A message is never "taken out" when received — only hidden. Picture a timestamp on each message saying "hidden until".

</details>

<details><summary>Hint 2 — which data structure?</summary>

A `LinkedHashMap<String, Entry(body, receiveCount, invisibleUntil, receipt)>` keeps send order; a list for dead-lettered ids.

</details>

<details><summary>Hint 3 — the approach</summary>

Use `invisibleUntil = Long.MIN_VALUE` for never-received messages so "visible" is always `now >= invisibleUntil`. Dead-lettering happens lazily inside `receive`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Scanning in send order is O(n) per receive. Real queues keep visible and in-flight messages in separate structures (and a timer wheel for timeouts).

</details>
