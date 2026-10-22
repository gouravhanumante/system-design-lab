# URL shortener

| App | Level | Concept |
|-----|-------|---------|
| Bitly | 0 — Foundations | Base62 encoding, counters vs hashes, custom aliases, redirects |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

`bit.ly/3xYz9` → a 300-character product link. The URL shortener is the most famous beginner system-design question because it touches ids, encoding, storage, caching and analytics while staying small. The core trick: give each URL a number from a counter and write that number in **base 62** (`0-9a-zA-Z`), so even billions of links have codes of 6–7 characters.

## Build this (small scale)

A shortener with base62 codes, custom aliases and click counts.

## API

```kotlin
fun encodeBase62(n: Long): String
fun decodeBase62(s: String): Long

class UrlShortener {
    fun shorten(longUrl: String): String
    fun customAlias(alias: String, longUrl: String): Boolean
    fun resolve(code: String): String?
    fun clicks(code: String): Int
}
```

## Rules

- Alphabet: `0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ` (digit value = position). `encodeBase62(0) = "0"`, `encodeBase62(61) = "Z"`, `encodeBase62(62) = "10"`. Negative numbers → `IllegalArgumentException`. `decodeBase62` reverses it; an invalid character or empty string → `IllegalArgumentException`.
- A long URL must start with `http://` or `https://`, else `IllegalArgumentException`.
- `shorten`: if this exact long URL was shortened before (by `shorten`, not by an alias), return the same code. Otherwise take the next counter value (`1, 2, 3, …`), encode it, and use it — **skipping** any value whose code is already taken by a custom alias.
- `customAlias`: alias must match `[a-zA-Z0-9-]{3,30}`, else `IllegalArgumentException`. Returns `false` if the code is already in use (by anything); otherwise maps it and returns `true`.
- `resolve(code)` returns the long URL and adds one click; unknown → `null` (no click).
- `clicks` of an unknown code is `0`.

## Example

`shorten("https://amazon.in/dp/B0C…")` → `"1"`, next new URL → `"2"`. `customAlias("sale", …)` → `true`. After resolving `"1"` twice, `clicks("1") = 2`.

## Think about (answer in NOTES.md)

- Counter-based codes are guessable (`/1`, `/2`, `/3`). Why might that matter, and how would you hide the order (shuffle bits, random codes)?
- Alternative: take the first 7 characters of a hash of the URL. What happens on a **collision**?
- With 100 app servers, where does "the next counter value" come from? (Look back at Snowflake, or think "each server reserves a block of 1,000 numbers".)
- Redirect with `301` (permanent) or `302` (temporary)? Which one lets you count clicks?

---

## Run the tests

```bash
./gradlew :l0-bitly-url-shortener:test
```

Your code goes in `src/main/kotlin/UrlShortener.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
