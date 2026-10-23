# Stateless login tokens (HMAC-signed)

| App | Level | Concept |
|-----|-------|---------|
| Google Sign-In | 0 — Foundations | Sessions vs tokens, HMAC signatures, expiry, revocation, stateless servers |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

After you log in, every request must prove who you are. Option 1: a **session id** — the server stores `sessionId → user` and looks it up on every request (needs shared storage across servers). Option 2: a **signed token** (like a JWT): the server puts `user + expiry` *into* the token and adds a **signature** made with a secret key. Any server with the secret can verify the token **without looking anything up** — servers stay **stateless**. The catch: you can't easily "log out" a token before it expires, so systems keep a small **revocation list**.

## Build this (small scale)

A token service that issues, verifies and revokes HMAC-SHA256-signed tokens. Crypto helpers are provided — the lesson is the design, not the crypto API.

## API

```kotlin
fun hmacSha256(secret: ByteArray, message: String): ByteArray       // provided
fun b64(bytes: ByteArray): String                                    // provided, URL-safe, no padding
fun unb64(s: String): ByteArray?                                     // provided, null if invalid

class TokenService(secret: ByteArray, ttlSeconds: Long = 3600) {
    fun issue(userId: String, now: Long): String
    fun verify(token: String, now: Long): String?
    fun revoke(token: String): Boolean
}
```

## Rules

- `issue`: `userId` must be non-blank and must not contain `|`, else `IllegalArgumentException`. Token ids are `1, 2, 3, …`.
  - `payload = "<userId>|<expiresAt>|<tokenId>"` with `expiresAt = now + ttlSeconds`.
  - `token = b64(payload as UTF-8 bytes) + "." + b64(hmacSha256(secret, payload))`.
- `verify(token, now)` returns the `userId` only if **all** hold, otherwise `null` (never throw):
  - exactly two parts separated by `.`, both valid base64;
  - the signature equals `hmacSha256(secret, payload)` — compare with `java.security.MessageDigest.isEqual` (constant time);
  - the payload has exactly three `|`-separated fields and numeric expiry/id;
  - `now < expiresAt`;
  - the token id has not been revoked.
- `revoke(token)`: if the token's signature is valid (expired is fine), add its token id to the revoked set and return `true`; otherwise `false`.

## Example

`issue("alice", 0)` → `"YWxpY2V8MzYwMHwx.<signature>"`. `verify` at 10 → `"alice"`; at 3600 → `null` (expired). Changing `alice` to `admin` inside the payload breaks the signature → `null`.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`issue()`** — build the payload, sign it, join with a dot. 🎯 `tokenFormatIsExact`, `tokenIdsIncrease`, `invalidUserIds`
2. **`decode()`** — split, base64-decode, check the signature, parse the 3 fields; `null` on any problem.
3. **`verify()`** — decode + expiry + revoked check. 🎯 `verifyReturnsTheUserUntilExpiry`, `anyServerWithTheSecretCanVerifyButNotOthers`, `tamperingBreaksTheSignature`, `garbageNeverThrows`
4. **`revoke()`**. 🎯 `revocation`

## Think about (answer in NOTES.md)

- The payload is only base64 — anyone can **read** it. What must never be put in a token?
- Why compare signatures in **constant time**? (Look up "timing attack".)
- Revocation needs a shared list again. How do short-lived access tokens + long-lived **refresh tokens** keep that list small?
- Rotating the secret: how do you switch keys without logging everybody out? (Hint: a key id in the token.)

---

## Run the tests

```bash
./gradlew :l0-google-sign-in-signed-tokens:test
```

Your code goes in `src/main/kotlin/TokenService.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
