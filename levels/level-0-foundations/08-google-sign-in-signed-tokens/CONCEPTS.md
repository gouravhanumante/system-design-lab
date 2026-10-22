# Concepts — Stateless login tokens (HMAC-signed)

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Authentication vs. authorization

**Authentication** answers "who are you?" (login). **Authorization** answers "what may you do?" (permissions, like the Drive sharing problem). Tokens carry the result of authentication to every later request.

## Sessions vs. tokens

- **Server-side sessions**: the server stores `sessionId → user`; the cookie only holds a random id. Easy to revoke; every server needs access to the session store.
- **Signed tokens** (JWT-style): the token itself contains `user + expiry`, signed by the server. Any server with the key can verify it without a lookup — servers stay **stateless**. Harder to revoke early.

## HMAC signatures

An **HMAC** is a fingerprint of a message computed with a secret key. Without the key nobody can produce a valid HMAC for a modified message, so tampering is detected. Signing ≠ encrypting: the payload is still readable (base64 is just encoding), so never put secrets in it.

## Expiry and revocation

Tokens carry an expiry time. To log someone out early, servers check a small **revocation (deny) list**. Short-lived access tokens (minutes) plus long-lived **refresh tokens** keep that list small.

## Constant-time comparison

Comparing signatures byte by byte and stopping at the first difference leaks timing information an attacker can measure. `MessageDigest.isEqual` always takes the same time.

## OAuth and OpenID Connect

"Sign in with Google" uses **OAuth 2.0** (delegated authorization) and **OpenID Connect** (identity on top of it). Google signs an **ID token** (a JWT) that your backend verifies with Google's public keys.
