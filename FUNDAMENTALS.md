# Fundamentals (read these — they don't need code)

The problems teach mechanisms by building them. Some essentials are better learned by reading and doing quick maths. Read this once before Level 0, and come back to it before interviews.

---

## 1. The 4-step interview framework

Almost every system-design interview (45–60 min) follows this shape. Practise saying it out loud.

1. **Clarify requirements (5 min).** *Functional*: what must it do? (post a tweet, see a feed). *Non-functional*: scale, latency, availability, consistency, cost. Ask: how many users? read-heavy or write-heavy? what can be slightly stale?
2. **Estimate (5 min).** Requests per second, storage per year, bandwidth. (Section 2.)
3. **High-level design (15–20 min).** Boxes and arrows: clients → load balancer → services → caches → databases → queues. Define the main APIs and data model.
4. **Deep dive (15–20 min).** Pick the hardest parts and go deep: the bottleneck, the data that grows fastest, the failure that hurts most. Discuss trade-offs; there is never one right answer.

The `NOTES.md` template in every problem trains steps 3–4 on a small scale.

## 2. Back-of-the-envelope estimation

**Handy numbers**

| | |
|---|---|
| Seconds in a day | 86,400 ≈ **10⁵** |
| 1 million requests/day | ≈ **12 per second** |
| 1 billion requests/day | ≈ **12,000 per second** |
| Peak traffic | usually **2–5×** the average |
| 1 KB × 1 million | 1 GB |
| 1 KB × 1 billion | 1 TB |

**Worked example — URL shortener** (Level 0 Bitly)
- 100 M new links/day → 100,000,000 / 86,400 ≈ **1,160 writes/s** (peak ~ 3–5k/s).
- Reads 100× writes → ≈ **116,000 redirects/s**: so caching matters far more than write speed.
- Storage: ~500 bytes per link × 100 M/day × 365 × 5 years ≈ **91 TB** → needs sharding, not one machine.
- 7 base62 characters → 62⁷ ≈ 3.5 trillion codes → enough for ~95 years at this rate.

The goal isn't exact numbers. It's knowing **which part of the system is under pressure** (here: reads, then storage).

## 3. Latency numbers every programmer should know

Orders of magnitude (exact values vary by hardware — learn the **ratios**):

| Operation | Time |
|-----------|------|
| CPU L1 cache reference | ~1 ns |
| Main memory reference | ~100 ns |
| Read 1 MB sequentially from memory | ~10 µs or less |
| SSD random read | ~20–100 µs |
| Round trip inside one data centre | ~0.5 ms |
| Read 1 MB sequentially from SSD | ~0.1–1 ms |
| Spinning-disk seek | ~5–10 ms |
| Round trip India ↔ US | ~150–250 ms |

Takeaways: memory is ~1,000× faster than a network hop; a cross-continent round trip costs about as much as millions of memory reads. That's why caches exist, why CDNs put data near users, and why "chatty" APIs (many small calls) are slow.

## 4. How a request reaches your server

1. **DNS**: the browser/app asks a resolver "what's the IP of api.example.com?". Answers are cached according to their **TTL**. DNS can also do simple load balancing (several IPs) and geo-routing (nearest region).
2. **TCP + TLS** handshake (HTTPS). Reusing connections (keep-alive, HTTP/2, HTTP/3) avoids paying this on every request.
3. **CDN / edge** (optional): static files and cacheable responses are answered by a nearby edge server.
4. **Load balancer** → one of many **stateless** app servers (Level 0 Nginx).
5. App server talks to **caches**, **databases**, **queues**, other services.

## 5. SQL vs NoSQL

| | Relational (PostgreSQL, MySQL) | NoSQL (DynamoDB, Cassandra, MongoDB, Redis) |
|---|---|---|
| Data model | Tables with fixed schemas, joins | Key-value, document, wide-column, graph |
| Transactions | Strong, multi-row ACID | Often single-key/single-document; some offer more |
| Scaling | Vertical first; sharding is manual work | Built to shard horizontally |
| Queries | Flexible (any column, ad-hoc SQL) | Designed around known access patterns |
| Pick when | Relationships, money, complex queries, "I'm not sure" | Huge scale, simple access patterns, flexible schemas |

Rule of thumb: **start with PostgreSQL** unless you have a concrete reason not to. Many large systems use both.

## 6. Consistency, availability and CAP

- **Strong consistency**: every read sees the latest write. **Eventual consistency**: replicas converge if writes stop (Level 0 Flipkart).
- **CAP theorem**: during a network **P**artition, a distributed system must choose between **C**onsistency (refuse some requests) and **A**vailability (answer, possibly with stale data). Partitions *will* happen, so the real choice is C vs A when they do.
- **PACELC** adds: *Else* (no partition), choose between **L**atency and **C**onsistency. Synchronous replication is consistent but slower.
- Examples: a bank balance wants C; a like counter or "people you may know" can be A.

## 7. Scaling playbook (in the order you usually need it)

1. **Measure** — find the actual bottleneck (metrics, profiling).
2. **Cache** hot reads (Level 2 Netflix, Level 4 Amazon).
3. **Add read replicas** for read-heavy databases (Level 0 Flipkart).
4. **Scale app servers horizontally** behind a load balancer; keep them stateless (Level 0 Nginx, Level 0 tokens).
5. **Move slow work to queues** and workers (Level 0 SQS, Kafka levels).
6. **Shard** data that outgrows one machine (Kafka partitions, consistent hashing).
7. **Denormalise / precompute** for heavy read paths (Instagram feed fan-out, Amazon trie).
8. **Go multi-region** for latency and disaster recovery — and face consistency trade-offs.

## 8. Reliability vocabulary

- **SLA / SLO / SLI**: the promise to customers / the internal target / the measured number (e.g. "99.9% of requests under 300 ms").
- **Availability "nines"**: 99.9% ≈ 8.8 hours of downtime a year; 99.99% ≈ 53 minutes.
- **Redundancy**: no single point of failure — two of everything that matters.
- **Graceful degradation**: lose features, not the whole product (Level 5 Netflix circuit breaker).
- **Observability**: *metrics* (numbers over time), *logs* (events), *traces* (one request across services). Alert on symptoms users feel, not on every CPU spike.

## 9. Consensus and coordination (just the idea)

Some decisions must be agreed by several machines: who is the leader, what's the committed order of writes. **Raft** and **Paxos** are algorithms that let a group agree even if a minority of machines fail; they're used inside etcd, ZooKeeper, Kafka (KRaft), CockroachDB and Spanner. You rarely implement them — but you should know *when* you're relying on them (leader election, distributed locks, configuration). Level 4 Kafka (generations/fencing) and Level 5 Kafka (ISR) show the problems they solve.

## 10. Where to go deeper

- *Designing Data-Intensive Applications* — Martin Kleppmann. The best book on the "why" behind all of this.
- *System Design Interview* Vol. 1 & 2 — Alex Xu. Many of the classic problems, interview-shaped.
- **The System Design Primer** (github.com/donnemartin/system-design-primer) — free, broad overview.
- Engineering blogs: Uber, Netflix TechBlog, Discord, Stripe, Cloudflare, Meta, Swiggy Bytes. Pick one post per week and connect it to a problem in this repo.
