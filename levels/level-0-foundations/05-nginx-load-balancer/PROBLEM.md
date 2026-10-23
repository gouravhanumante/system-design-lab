# Load balancer with health checks

| App | Level | Concept |
|-----|-------|---------|
| Nginx | 0 — Foundations | Round robin, least connections, health checks with rise/fall thresholds |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

One server can't handle all your traffic, so you run several behind a **load balancer** (Nginx, HAProxy, AWS ELB). For every request it picks a server: **round robin** takes turns; **least connections** picks whoever is least busy. It also runs **health checks** — and to avoid flapping on one bad check, a server is marked down only after several failures in a row (`fall`) and back up only after several passes in a row (`rise`).

## Build this (small scale)

A load balancer with two strategies and health checks.

## API

```kotlin
enum class Strategy { ROUND_ROBIN, LEAST_CONNECTIONS }

class LoadBalancer(strategy: Strategy, fall: Int = 3, rise: Int = 2) {
    fun addServer(name: String)
    fun removeServer(name: String)
    fun healthCheck(name: String, passed: Boolean)
    fun isHealthy(name: String): Boolean
    fun acquire(): String?
    fun release(name: String)
    fun activeConnections(name: String): Int
}
```

## Rules

- Servers start **healthy** with 0 active connections. Adding an existing name → `IllegalArgumentException`. Removing an unknown name does nothing.
- `healthCheck`: a failed check adds to the server's consecutive-failure streak (and resets its pass streak); a passed check does the opposite. A **healthy** server becomes unhealthy when its failure streak reaches `fall`. An **unhealthy** server becomes healthy when its pass streak reaches `rise`. Unknown server → `IllegalArgumentException`.
- `acquire` picks a **healthy** server, adds 1 to its active connections and returns its name; no healthy server → `null`.
  - `ROUND_ROBIN`: servers in the order they were added; continue from the position **after** the last chosen one, wrapping around, skipping unhealthy servers.
  - `LEAST_CONNECTIONS`: the healthy server with the fewest active connections; ties → the one added first.
- `release(name)` subtracts 1 (never below 0; unknown: nothing happens).
- `isHealthy` / `activeConnections` of an unknown server → `IllegalArgumentException`.

## Example

Round robin over `a, b, c`: `a, b, c, a, …`. Mark `b` down (3 failed checks) → `a, c, a, c`. Two passes later `b` is back in rotation.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`addServer` / `removeServer` / `activeConnections`** with the `Server` class and the `servers` map.
2. **`acquire()` for ROUND_ROBIN** (all servers healthy for now) + **`release()`**. 🎯 `roundRobinTakesTurns`, `releaseNeverGoesBelowZero`, `removedServersAreNeverChosen`
3. **`healthCheck()` + `isHealthy()`** — the fall/rise counters. 🎯 `serverGoesDownOnlyAfterFallFailuresInARow`
4. **Skip unhealthy servers in `acquire()`**. 🎯 `unhealthyServersAreSkippedAndComeBackAfterRisePasses`, `noHealthyServers`
5. **LEAST_CONNECTIONS** branch in `acquire()`. 🎯 `leastConnectionsPicksTheLeastBusy`
6. **Errors**: duplicate add, unknown server. 🎯 `invalidCalls`

## Think about (answer in NOTES.md)

- When is least-connections clearly better than round robin? (Think: some requests take 10 ms, some 10 s.)
- **Sticky sessions** send a user to the same server every time. Why would you want that — and why do people prefer **stateless** servers instead?
- The load balancer is now a single point of failure. How do real setups avoid that?

---

## Run the tests

```bash
./gradlew :l0-nginx-load-balancer:test
```

Your code goes in `src/main/kotlin/LoadBalancer.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
