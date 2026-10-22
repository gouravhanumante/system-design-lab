# Concepts — Is the restaurant open?

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Time intervals

An interval `[start, end)` includes the start and excludes the end, so back-to-back intervals (12:00–15:00, 15:00–18:00) never overlap and never leave a gap. Using **half-open** intervals consistently avoids a whole family of off-by-one bugs.

## A single time axis

Comparing `(day, hour, minute)` tuples is error-prone. Converting everything to **one number** — minutes since Monday 00:00 — turns every question into simple integer comparisons. Databases do the same with timestamps (seconds since 1970).

## Wrap-around and modular arithmetic

Intervals can cross midnight and even the end of the week. Two tricks: let the end go **past** the week length (Sunday 23:00 → Monday 01:00 becomes 10,020 → 10,140), and check a moment both as `m` and `m + 10,080`. For "how long until", `floorMod(target − now, weekLength)` gives the forward distance, always non-negative.

## Time zones

Opening hours are **local** time. Servers usually run in UTC. Store the zone id ("Asia/Kolkata") with the restaurant and convert "now" into that zone before checking. Fixed offsets (+05:30) break in countries with daylight saving.
