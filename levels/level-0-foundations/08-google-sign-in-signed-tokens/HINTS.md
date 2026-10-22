# Hints — Stateless login tokens (HMAC-signed)

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Verification is just "rebuild the signature from the payload and compare". If anyone changed even one character of the payload, the signatures won't match.

</details>

<details><summary>Hint 2 — which data structure?</summary>

A counter for token ids and a `HashSet<Long>` of revoked ids. Nothing else is stored per token — that's the point.

</details>

<details><summary>Hint 3 — the approach</summary>

Write one private `decode(token): Triple<user, expiresAt, id>?` that checks format and signature and returns `null` on any problem (wrap parsing in `try`/`toLongOrNull`). `verify` and `revoke` both use it.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

With **asymmetric** signatures (RS256/ES256) only the auth server holds the private key; every other service verifies with the public key — so a compromised API server can't mint tokens.

</details>
