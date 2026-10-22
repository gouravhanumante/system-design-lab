# Concepts — Circuit breaker with fallbacks

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Cascading failures

When one dependency slows down, callers wait on it, their threads/connections fill up, and **they** become slow for *their* callers — the failure spreads up the chain until the whole system is down. Most big outages look like this.

## Circuit breaker

A **circuit breaker** wraps calls to a dependency and tracks failures. After too many failures it **opens**: calls fail immediately (with a fallback) instead of waiting, which protects the caller and gives the sick service room to recover. After a cool-down it goes **half-open** and lets a few trial calls through; if they succeed it **closes** again, otherwise it reopens.

## Fallbacks and graceful degradation

A **fallback** is a reduced but acceptable answer: cached data, a default ("Popular on Netflix" instead of personalised rows), or hiding the feature. Designing products to **degrade gracefully** is as important as preventing failures.

## Fail fast

Returning an error in 1 ms is often better than returning it after a 30 s timeout: resources are freed and users get feedback quickly. Breakers, timeouts and **bulkheads** (separate resource pools per dependency, so one can't exhaust everything) are the standard fail-fast tools. Libraries: Hystrix (retired), Resilience4j, Polly; service meshes (Istio, Envoy) do it at the network level.

## Thundering herd on recovery

When a service recovers and every client's breaker closes at the same moment, they all send traffic at once and may knock it over again. Half-open trial limits, **jittered** timers and gradual ramp-up avoid this.
