# Concepts — API rate limiter (token buckets, per user and global)

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Rate limiting

A **rate limiter** caps how many requests a client may make per unit of time. It protects servers from overload, stops abuse and scraping, and enforces paid tiers. Refused requests get HTTP **429 Too Many Requests**, usually with a `Retry-After` header.

## Token bucket

A bucket holds up to `capacity` tokens and refills at `rate` tokens per second. Each request takes one token; no token → refused. It allows **bursts** (up to capacity) while enforcing an **average** rate. The bucket doesn't need a timer: store `tokens` and `lastRefill`, and when a request arrives, add `elapsed × rate` (capped at capacity).

## Other algorithms

- **Fixed window**: count requests per calendar minute. Simple, but allows 2× the limit around the window boundary (end of one minute + start of the next).
- **Sliding window log**: store each request's timestamp; precise but memory-heavy.
- **Sliding window counter**: blend the current and previous window counts; a good approximation.
- **Leaky bucket**: requests drain at a fixed rate — smooths traffic rather than allowing bursts.

## Layered limits and atomicity

Real systems apply several limits at once (per user, per IP, per endpoint, global). A request must pass **all** of them, and if one refuses, the others must not be charged. In a distributed setup (Redis), "check all and spend all" must happen as one **atomic** operation, or concurrent requests slip through.

## Floating-point time math

Refill involves fractions (`0.5 tokens`). Small floating-point errors can make a comparison like `tokens >= 1.0` fail when it should pass. Be careful with rounding (here: round the retry time **up** so clients never retry too early).
