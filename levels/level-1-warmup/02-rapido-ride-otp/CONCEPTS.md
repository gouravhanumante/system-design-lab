# Concepts — Ride-start OTP

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## One-time passwords (OTP)

An OTP is a short secret that is valid **once**. Because it can't be reused, overhearing it after use gives an attacker nothing. Ride apps generate a fresh 4-digit OTP per ride; the rider tells the driver, who types it in to start the trip. It proves both people are the ones the system matched.

A **static PIN** (same code every ride) is simpler and works offline, but once someone learns it, it works for them on every future ride until you change it.

## Hash map lookup

A hash map stores `key → value` pairs and finds a value by key in O(1) on average. Here `rideId → code`. Most backend state you'll build in this repo is "a few hash maps"; real systems replace them with Redis or a database table keyed the same way.

## Injecting randomness

`Random()` inside a class makes tests unpredictable. Passing a **generator function** in from outside (`generator: () -> String`) lets production use a secure random source while tests supply fixed codes. Same idea as injecting the clock. For real secrets use a cryptographically secure generator (`java.security.SecureRandom`), not `kotlin.random.Random`.
