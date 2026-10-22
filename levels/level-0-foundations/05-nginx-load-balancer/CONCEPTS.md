# Concepts — Load balancer with health checks

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Horizontal scaling

Instead of buying one bigger machine (**vertical** scaling), run many identical servers (**horizontal** scaling) and spread requests across them. A **load balancer** sits in front and decides which server gets each request.

## Balancing strategies

- **Round robin**: take turns. Simple and fair when requests are similar.
- **Least connections**: pick the least busy server. Better when request durations vary a lot.
- **Weighted**: bigger servers get more traffic.
- **Hash-based / consistent hashing**: the same user or key always goes to the same server (useful for caches).

## Health checks

The balancer regularly checks each server (an HTTP `/health` endpoint) and stops sending traffic to failing ones. **Fall/rise thresholds** (down after 3 failures in a row, up after 2 passes) prevent flapping on a single bad check — hysteresis again.

## Stateless servers

Balancing is easy when any server can handle any request — i.e. servers keep no user state in memory (sessions live in a shared store or in signed tokens). **Sticky sessions** work around stateful servers but make scaling and failover harder.

## Layer 4 vs. layer 7

L4 balancers route TCP connections without looking inside; L7 balancers understand HTTP (paths, headers, cookies) and can route `/api` and `/images` differently. Nginx, HAProxy and Envoy can do both.
