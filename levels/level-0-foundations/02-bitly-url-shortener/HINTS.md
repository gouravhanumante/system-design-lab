# Hints — URL shortener

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Base 62 works exactly like converting a number to base 10 by hand: repeatedly take `n % 62` (the last digit) and `n / 62`.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`codeToUrl: HashMap<String, String>`, `urlToCode: HashMap<String, String>` (only for `shorten`), `clicks: HashMap<String, Int>`, and a `Long` counter.

</details>

<details><summary>Hint 3 — the approach</summary>

In `shorten`: `do { code = encodeBase62(counter++) } while (code in codeToUrl)`. Start the counter at 1.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Reads (redirects) outnumber writes 100:1, so put a cache (or the CDN) in front of `resolve` and count clicks asynchronously (YouTube L4's batching).

</details>
