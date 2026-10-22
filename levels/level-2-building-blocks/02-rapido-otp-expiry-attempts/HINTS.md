# Hints — OTP with expiry and attempt limit

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

The order of the checks is part of the specification. Write the six rules as six `if` lines in that exact order.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A small private class per ride: `code`, `expiresAt`, `attempts`, `used`.

</details>

<details><summary>Hint 3 — the approach</summary>

`issue` creates a brand-new record (this resets everything). `verify` reads the record and walks down the rules. Only the "wrong code" branch increments `attempts`.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Store records in Redis with a TTL equal to the OTP lifetime — expiry then happens automatically and nothing needs cleaning up.

</details>
