# Concepts — URL shortener

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Base62 encoding

Writing a number using 62 symbols (0–9, a–z, A–Z) makes it short and URL-safe: 62⁷ ≈ 3.5 trillion codes fit in 7 characters. Converting works like base 10: repeatedly take `n % 62` for the next digit and divide by 62.

## Counters vs. hashes vs. random ids

- **Counter** + base62: short, no collisions, but needs a unique counter across servers and codes are guessable.
- **Hash** of the URL (first 7 chars): the same URL always gives the same code, but different URLs can **collide**; you must detect and handle it.
- **Random** codes: unguessable, but you must check for collisions on insert.

## Read-heavy systems

A shortener is written once and read (redirected) many times. Design for reads: cache hot codes in memory or at the CDN; make writes simple.

## Redirects: 301 vs 302

`301 Moved Permanently` lets browsers cache the redirect, so later clicks never reach you — fast, but you can't count them. `302 Found` (temporary) sends every click through your server, enabling analytics.

## Namespaces and reserved words

Custom aliases share the code space with generated codes. The system must prevent collisions both ways (and usually reserves words like "admin", "api", "login").
