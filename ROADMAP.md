# Roadmap and problem registry

This file is the **single source of truth** for what exists and what is planned. Before adding a problem, read [CONTRIBUTING.md](CONTRIBUTING.md) §0: a new problem must teach a *new core concept* (or a clearly new twist) compared to every row below.

Status: ✅ built and tested · 📝 planned (title + core idea fixed, not written yet)

## Built (68 problems)


### Level 0 — Foundations

| # | App | Problem | Core concept | Status |
|--:|-----|---------|--------------|:------:|
| 1 | GitHub | [A REST API for issues](levels/level-0-foundations/01-github-rest-api-basics/PROBLEM.md) | Resources, HTTP methods, status codes, validation | ✅ |
| 2 | Bitly | [URL shortener](levels/level-0-foundations/02-bitly-url-shortener/PROBLEM.md) | Base62 encoding, counters vs hashes, custom aliases, redirects | ✅ |
| 3 | Redis | [Key-value store with transactions](levels/level-0-foundations/03-redis-kv-transactions/PROBLEM.md) | Transactions, atomicity, rollback, nested layers of changes | ✅ |
| 4 | PostgreSQL | [Database indexes vs full table scans](levels/level-0-foundations/04-postgresql-database-index/PROBLEM.md) | Full scan, sorted (B-tree-like) index, hash index, binary search, index maintenance | ✅ |
| 5 | Nginx | [Load balancer with health checks](levels/level-0-foundations/05-nginx-load-balancer/PROBLEM.md) | Round robin, least connections, health checks with rise/fall thresholds | ✅ |
| 6 | Amazon SQS | [Work queue with acknowledgements](levels/level-0-foundations/06-amazon-sqs-work-queue-ack/PROBLEM.md) | Message queues, visibility timeout, ack, redelivery, dead-letter queue | ✅ |
| 7 | Flipkart | [Read replicas and read-your-writes](levels/level-0-foundations/07-flipkart-read-replicas/PROBLEM.md) | Primary/replica, replication lag, eventual consistency, read-your-writes routing | ✅ |
| 8 | Google Sign-In | [Stateless login tokens (HMAC-signed)](levels/level-0-foundations/08-google-sign-in-signed-tokens/PROBLEM.md) | Sessions vs tokens, HMAC signatures, expiry, revocation, stateless servers | ✅ |

### Level 1 — Warm-up

| # | App | Problem | Core concept | Status |
|--:|-----|---------|--------------|:------:|
| 1 | Uber | [Find the nearest drivers](levels/level-1-warmup/01-uber-nearest-driver/PROBLEM.md) | Brute-force scan, sorting, tie-breaking | ✅ |
| 2 | Rapido | [Ride-start OTP](levels/level-1-warmup/02-rapido-ride-otp/PROBLEM.md) | Per-ride secret, one-time use, injected randomness | ✅ |
| 3 | YouTube | [Video view counter and top videos](levels/level-1-warmup/03-youtube-view-counter/PROBLEM.md) | Hash map counting, ranking with tie-breaks | ✅ |
| 4 | Google Drive | [Folders, files and total size](levels/level-1-warmup/04-google-drive-folder-tree/PROBLEM.md) | Tree of nodes, path parsing, recursion | ✅ |
| 5 | Kafka | [The append-only log](levels/level-1-warmup/05-kafka-append-log/PROBLEM.md) | Append-only log, offsets, reading from a position | ✅ |
| 6 | WhatsApp | [Message ticks (sent, delivered, read)](levels/level-1-warmup/06-whatsapp-message-ticks/PROBLEM.md) | Monotonic state, ordered enum, ignoring stale updates | ✅ |
| 7 | Instagram | [Likes that can't be double-counted](levels/level-1-warmup/07-instagram-like-counter/PROBLEM.md) | Idempotent set membership, recency ordering | ✅ |
| 8 | Twitter | [Timeline pages with a cursor](levels/level-1-warmup/08-twitter-cursor-pagination/PROBLEM.md) | Cursor (keyset) pagination vs offset pagination | ✅ |
| 9 | Netflix | [Continue watching](levels/level-1-warmup/09-netflix-continue-watching/PROBLEM.md) | Per-profile state, recency ordering, completion threshold | ✅ |
| 10 | Amazon | [Shopping cart with money in paise](levels/level-1-warmup/10-amazon-shopping-cart/PROBLEM.md) | Integer money, validation rules, coupon caps | ✅ |
| 11 | Swiggy | [Is the restaurant open?](levels/level-1-warmup/11-swiggy-opening-hours/PROBLEM.md) | Time intervals, wrap-around past midnight and end of week, modular arithmetic | ✅ |
| 12 | Stripe | [Double-entry ledger](levels/level-1-warmup/12-stripe-double-entry-ledger/PROBLEM.md) | Double-entry bookkeeping, invariants, idempotent transactions | ✅ |

### Level 2 — Building blocks

| # | App | Problem | Core concept | Status |
|--:|-----|---------|--------------|:------:|
| 1 | Uber | [Surge pricing for a zone](levels/level-2-building-blocks/01-uber-surge-pricing/PROBLEM.md) | Sliding time window, queue eviction | ✅ |
| 2 | Rapido | [OTP with expiry and attempt limit](levels/level-2-building-blocks/02-rapido-otp-expiry-attempts/PROBLEM.md) | Time-to-live, attempt counting, lockout, result codes | ✅ |
| 3 | YouTube | [Count a view only once per viewer](levels/level-2-building-blocks/03-youtube-view-dedup-window/PROBLEM.md) | Per-key timestamps, windowed de-duplication | ✅ |
| 4 | Google Drive | [Version history and restore](levels/level-2-building-blocks/04-google-drive-file-versions/PROBLEM.md) | Append-only history, retention limit, never reusing ids | ✅ |
| 5 | Kafka | [Partitions and key-based routing](levels/level-2-building-blocks/05-kafka-partitions/PROBLEM.md) | Hash partitioning, per-partition ordering, round-robin | ✅ |
| 6 | WhatsApp | [Delivering messages to offline users](levels/level-2-building-blocks/06-whatsapp-offline-queue/PROBLEM.md) | Per-user queue, FIFO delivery on reconnect, TTL, idempotent sends | ✅ |
| 7 | Instagram | [Follow graph and "suggested for you"](levels/level-2-building-blocks/07-instagram-follow-graph/PROBLEM.md) | Directed graph as adjacency sets, 2-hop traversal, ranking by overlap | ✅ |
| 8 | Twitter | [Snowflake IDs](levels/level-2-building-blocks/08-twitter-snowflake-ids/PROBLEM.md) | Unique, time-sortable ids without a central counter; bit packing | ✅ |
| 9 | Netflix | [LRU cache on an edge server](levels/level-2-building-blocks/09-netflix-lru-edge-cache/PROBLEM.md) | Least-recently-used eviction with a byte budget, O(1) operations | ✅ |
| 10 | Amazon | [Inventory holds during checkout](levels/level-2-building-blocks/10-amazon-inventory-holds/PROBLEM.md) | Reservations with expiry, preventing overselling, idempotent confirm | ✅ |
| 11 | Swiggy | [Delivery slots with capacity and a waitlist](levels/level-2-building-blocks/11-swiggy-slot-booking-waitlist/PROBLEM.md) | Capacity limits, FIFO waitlist promotion, idempotent booking | ✅ |
| 12 | Stripe | [Idempotency keys for a payment API](levels/level-2-building-blocks/12-stripe-idempotency-keys/PROBLEM.md) | Idempotent POST requests, stored responses, key conflicts, key expiry | ✅ |

### Level 3 — Realistic

| # | App | Problem | Core concept | Status |
|--:|-----|---------|--------------|:------:|
| 1 | Uber | [Grid index for nearby drivers](levels/level-3-realistic/01-uber-geo-grid-index/PROBLEM.md) | Spatial index, bucketing, avoiding full scans | ✅ |
| 2 | Rapido | [Match many requests to many captains](levels/level-3-realistic/02-rapido-batch-matching/PROBLEM.md) | Global greedy matching vs first-come-first-served | ✅ |
| 3 | YouTube | [Resumable chunked upload](levels/level-3-realistic/03-youtube-chunked-resumable-upload/PROBLEM.md) | Out-of-order chunks, idempotent retries, integrity checks | ✅ |
| 4 | Google Drive | [Deduplicated chunk storage](levels/level-3-realistic/04-google-drive-chunk-dedup/PROBLEM.md) | Content-addressed chunks, reference counting, garbage collection | ✅ |
| 5 | Kafka | [Consumer groups, commits and replay](levels/level-3-realistic/05-kafka-consumer-offsets/PROBLEM.md) | Committed offsets, at-least-once delivery, lag | ✅ |
| 6 | WhatsApp | [Group chat fan-out and "read by all"](levels/level-3-realistic/06-whatsapp-group-fanout/PROBLEM.md) | Snapshot of recipients at send time, per-recipient state, fan-out | ✅ |
| 7 | Instagram | [Nested comments with soft delete](levels/level-3-realistic/07-instagram-comment-threads/PROBLEM.md) | Tree of replies, depth-first rendering, pruning deleted branches | ✅ |
| 8 | Twitter | [Trending hashtags with a Count-Min Sketch](levels/level-3-realistic/08-twitter-count-min-trending/PROBLEM.md) | Probabilistic counting in fixed memory, heavy hitters | ✅ |
| 9 | Netflix | [Consistent hashing for cache servers](levels/level-3-realistic/09-netflix-consistent-hashing/PROBLEM.md) | Hash ring, virtual nodes, minimal key movement | ✅ |
| 10 | Amazon | [Search autocomplete with a trie](levels/level-3-realistic/10-amazon-autocomplete-trie/PROBLEM.md) | Trie (prefix tree), precomputed top-k per node, read-optimised indexes | ✅ |
| 11 | Swiggy | [Fastest route and ETA (Dijkstra)](levels/level-3-realistic/11-swiggy-dijkstra-eta/PROBLEM.md) | Weighted graph, Dijkstra's shortest path, priority queue, road closures | ✅ |
| 12 | Stripe | [Webhook delivery with backoff and a dead-letter queue](levels/level-3-realistic/12-stripe-webhook-retries/PROBLEM.md) | At-least-once delivery, exponential backoff, dead-letter queue, redrive | ✅ |

### Level 4 — Hard

| # | App | Problem | Core concept | Status |
|--:|-----|---------|--------------|:------:|
| 1 | Uber | [Dispatch with offer timeouts](levels/level-4-hard/01-uber-dispatch-timeouts/PROBLEM.md) | Time-driven state machine, retry to next candidate | ✅ |
| 2 | Rapido | [Live demand heatmap (hot zones)](levels/level-4-hard/02-rapido-hot-zones-window/PROBLEM.md) | Ring buffer of time buckets, running totals, top-k | ✅ |
| 3 | YouTube | [View counts at scale — write batching](levels/level-4-hard/03-youtube-batched-view-counts/PROBLEM.md) | Write coalescing, buffering in front of a database | ✅ |
| 4 | Google Drive | [Sharing, groups and inherited permissions](levels/level-4-hard/04-google-drive-sharing-permissions/PROBLEM.md) | Permission inheritance up a tree, groups, max-wins resolution | ✅ |
| 5 | Kafka | [Group coordinator and rebalancing](levels/level-4-hard/05-kafka-consumer-group-rebalance/PROBLEM.md) | Partition assignment, membership, heartbeats, generations | ✅ |
| 6 | WhatsApp | [Online status, last seen and privacy](levels/level-4-hard/06-whatsapp-presence-last-seen/PROBLEM.md) | Heartbeat-based presence, privacy rules, reciprocity | ✅ |
| 7 | Instagram | [Home feed: push, pull, or both?](levels/level-4-hard/07-instagram-hybrid-feed-fanout/PROBLEM.md) | Fan-out on write vs read, the celebrity problem, merge and de-duplicate | ✅ |
| 8 | Twitter | [API rate limiter (token buckets, per user and global)](levels/level-4-hard/08-twitter-api-rate-limiter/PROBLEM.md) | Token bucket algorithm, layered limits, Retry-After | ✅ |
| 9 | Netflix | [Adaptive bitrate: picking video quality](levels/level-4-hard/09-netflix-adaptive-bitrate/PROBLEM.md) | Throughput estimation (harmonic mean), buffer-based safety rules, hysteresis | ✅ |
| 10 | Amazon | [Product page cache (cache-aside, stale-while-revalidate)](levels/level-4-hard/10-amazon-product-cache/PROBLEM.md) | Cache-aside, TTL, invalidation on write, negative caching, background refresh | ✅ |
| 11 | Swiggy | [Delivery zones (point in polygon)](levels/level-4-hard/11-swiggy-geofence-serviceability/PROBLEM.md) | Geofencing, ray casting, boundary cases, bounding-box prefilter | ✅ |
| 12 | Stripe | [Reconciling the ledger with the bank statement](levels/level-4-hard/12-stripe-reconciliation/PROBLEM.md) | Multi-pass record matching, tolerance windows, exception reports | ✅ |

### Level 5 — Boss

| # | App | Problem | Core concept | Status |
|--:|-----|---------|--------------|:------:|
| 1 | Uber | [Trip lifecycle, fare and cancellation fees](levels/level-5-boss/01-uber-trip-lifecycle-fare/PROBLEM.md) | State machine, invalid-transition guards, pricing rules | ✅ |
| 2 | Rapido | ["Share trip" route deviation alerts](levels/level-5-boss/02-rapido-route-deviation-alerts/PROBLEM.md) | Point-to-segment geometry, streak counting, alert de-duplication | ✅ |
| 3 | YouTube | [Video processing pipeline (job DAG scheduler)](levels/level-5-boss/03-youtube-transcode-job-dag/PROBLEM.md) | Dependency graph, worker pool simulation, retries, failure propagation | ✅ |
| 4 | Google Drive | [Offline sync and conflict copies](levels/level-5-boss/04-google-drive-sync-conflicts/PROBLEM.md) | Optimistic concurrency with revisions, conflict detection, convergence | ✅ |
| 5 | Kafka | [Replication, ISR and high watermark](levels/level-5-boss/05-kafka-replication-isr/PROBLEM.md) | Leader/follower replication, in-sync replicas, no loss of committed data | ✅ |
| 6 | WhatsApp | [Message ordering and gap recovery across devices](levels/level-5-boss/06-whatsapp-ordering-gap-sync/PROBLEM.md) | Server-assigned sequence numbers, buffering, gap detection, batched catch-up | ✅ |
| 7 | Instagram | [Stories: 24-hour expiry, audiences and highlights](levels/level-5-boss/07-instagram-stories-expiry/PROBLEM.md) | TTL, audience rules evaluated at read time, unique viewers, exceptions to expiry | ✅ |
| 8 | Twitter | [Tweet search with an inverted index](levels/level-5-boss/08-twitter-search-inverted-index/PROBLEM.md) | Inverted index, positional postings, phrase and exclusion queries, deletes | ✅ |
| 9 | Netflix | [Circuit breaker with fallbacks](levels/level-5-boss/09-netflix-circuit-breaker/PROBLEM.md) | Fail fast, CLOSED/OPEN/HALF_OPEN states, recovery probing | ✅ |
| 10 | Amazon | [Order saga with compensations](levels/level-5-boss/10-amazon-order-saga/PROBLEM.md) | Saga pattern, retries, compensating actions in reverse, idempotent orchestration | ✅ |
| 11 | Swiggy | [Order tracking with event sourcing](levels/level-5-boss/11-swiggy-order-event-sourcing/PROBLEM.md) | Event sourcing, versioned events, projections, out-of-order buffering, replay | ✅ |
| 12 | Stripe | [Two-phase commit across banks (UPI-style)](levels/level-5-boss/12-stripe-two-phase-commit/PROBLEM.md) | Atomic commitment, prepare/commit votes, durable decision log, crash recovery | ✅ |


## Planned (39 apps × 5 = 195 problems)

Each row is one app; columns are Levels 1–5. Titles describe the core idea so nobody writes a second problem about the same thing. Batches will be built in the order listed (AI first, by request). When an app is built, its row moves to *Built* above.


### AI

| App | L1 | L2 | L3 | L4 | L5 |
|-----|----|----|----|----|----|
| **ChatGPT** | Token-per-minute rate limits (weighted token bucket) | Conversation context window trimming (keep system + newest within budget) | Streaming responses: reassemble SSE chunks with ids and resume | Prompt prefix cache (hash of token prefix, LRU, hit accounting) | Continuous batching scheduler for GPU inference (slots, KV memory budget) |
| **Perplexity** | Split documents into overlapping chunks | BM25 keyword ranking | Vector search: cosine top-k with brute force vs IVF buckets | Hybrid retrieval: reciprocal rank fusion of keyword + vector results | RAG answer citations: map generated sentences back to source chunks |
| **MCP server** | JSON-RPC request/response routing and error codes | Tool registry with JSON-schema-lite argument validation | Capability negotiation and versioning between client and server | Long-running tool calls: progress notifications and cancellation | Permission gating: allow-lists, human approval queue, audit log |
| **GitHub Copilot** | Debounce keystrokes before requesting completions | Completion cache keyed by file prefix with invalidation on edit | Ranking multiple suggestions (acceptance-rate feedback) | Context gathering: pick most relevant open-file snippets within a budget | A/B experiment assignment by stable hash bucketing |

### Social

| App | L1 | L2 | L3 | L4 | L5 |
|-----|----|----|----|----|----|
| **Reddit** | Upvotes/downvotes with one vote per user (vote changes) | "Hot" ranking with time decay | "Best" comment sort with Wilson score lower bound | Front page: k-way merge of many subreddit feeds | Vote manipulation detection: velocity + account-age rules |
| **LinkedIn** | Degrees of connection with BFS (1st/2nd/3rd) | Profile view counts with weekly buckets | Job recommendations by skill-set similarity (Jaccard/cosine) | Feed ranking with diversity rules (no more than N from one author) | Invitation spam limits with weekly quotas and reputation |
| **Discord** | Role permissions as bitmasks with channel overwrites | Lazy member list: windowed pagination over a sorted list | Bulk message delete with 14-day rule and audit | Voice channel state: join/leave/mute with user limits | Gateway sharding: shard = (guild_id >> 22) % shards and resharding |
| **Slack** | Unread counts with per-channel read cursors | Mentions and notification preferences (keywords, DND, muting) | Typing indicators: ephemeral state with TTL | Permission-aware message search (private channels) | WebSocket gateway: connection registry and fan-out to many devices |
| **Tinder** | Mutual-like match detection | Daily swipe quotas with timezone reset | Candidate filtering by distance + preferences with a geo grid | Elo-style desirability score updates | Fair exposure: show every profile at least k times per day |
| **Pinterest** | Boards and pins (many-to-many) | Masonry layout: greedy shortest-column packing | Near-duplicate images with perceptual hash + Hamming distance | Related pins from shared boards (co-occurrence) | Home feed: candidate generation + re-ranking pipeline |

### Media

| App | L1 | L2 | L3 | L4 | L5 |
|-----|----|----|----|----|----|
| **Spotify** | Fair shuffle (Fisher–Yates) without back-to-back artists | Royalty play counts: a play counts after 30 s, once per session | Collaborative playlist ordering with fractional indexes | Offline downloads under a storage quota (priority eviction) | "Fans also like": item–item collaborative filtering |
| **Hotstar** | Concurrent-stream limit per account (device leases) | Live score updates: coalesce many updates into one push | Ad insertion points on a live timeline | Unique viewers with HyperLogLog (approximate distinct count) | Pre-scaling for a known spike (capacity planning simulation) |
| **Shazam** | Spectrogram peaks to landmark hashes (provided peaks) | Hash index lookup with time-offset voting | Matching with noise: threshold on aligned votes | Index sharding by hash prefix | Query-by-humming: tolerance windows |

### Productivity

| App | L1 | L2 | L3 | L4 | L5 |
|-----|----|----|----|----|----|
| **Google Docs** | Insert/delete operations on text with positions | Comments anchored to ranges that move with edits | Operational transform for two concurrent inserts | Presence cursors with colour assignment and TTL | Version snapshots and line diff (LCS) |
| **Notion** | Block tree with move operation and cycle prevention | Ordering blocks with fractional indexes | Page permissions inherited through the block tree | Backlinks index (who links to this page) | Offline edits merged with a last-writer-wins CRDT map |
| **Google Calendar** | Free/busy: merge intervals and find common slots | Recurring events expansion (daily/weekly rules, exceptions) | Reminder scheduler with a timer wheel | Time zones and DST-safe event times | RSVP state and attendee notifications with dedupe |
| **Gmail** | Threading by subject and reply headers | Label search with set operations | Naive Bayes spam score | Send-later scheduler with cancellation | Undo send: delayed delivery with a cancellation window |
| **Zoom** | Meeting state: host, co-host, join/leave rules | Waiting room admission queue | Jitter buffer: reorder packets with a playout deadline | Simulcast layer selection per subscriber bandwidth | Breakout rooms: balanced assignment with constraints |

### Dev

| App | L1 | L2 | L3 | L4 | L5 |
|-----|----|----|----|----|----|
| **GitHub** | Line diff with LCS | Three-way merge and conflict detection | CODEOWNERS glob matching (last match wins) | CI job queue with concurrency groups and cancellation | Git object store: content-addressed blobs, trees and commits (Merkle) |
| **Dropbox** | Rolling-hash delta sync (rsync) | Storage quota with pending-upload reservations | Shared folder membership changes and access propagation | Merkle tree to find changed folders fast | Selective sync and placeholders |
| **Cloudflare** | DNS resolution with TTL caching and CNAME chains | Anycast-style nearest-PoP selection | WAF rule matching (ordered rules, first match) | DDoS mitigation: per-IP sliding windows + challenge | Cache purge propagation across PoPs with versioned keys |
| **Datadog** | Metrics ingestion: time-series downsampling (min/max/avg rollups) | Percentiles with a fixed-bucket histogram | Alert rules with "for N minutes" and recovery hysteresis | Log search over time-partitioned shards | Distributed tracing: assemble spans into a tree and find the critical path |

### Commerce

| App | L1 | L2 | L3 | L4 | L5 |
|-----|----|----|----|----|----|
| **Airbnb** | Availability calendar: book date ranges without overlap | Price for a stay: nightly rates, weekly discounts, fees | Double-booking prevention with optimistic version checks | Search: quadtree adaptive spatial index | Host payouts: delayed release after check-in with holds |
| **BookMyShow** | Best available contiguous seats | Seat locks with lease extension | Virtual waiting room tokens for big releases | Dynamic price tiers by remaining seats | Cancellation and refund policy engine |
| **Zomato** | Restaurant ratings with Bayesian average | Menu availability by time slots and stock | Delivery fee calculation by distance bands and surge | Rider batching: two orders on one trip if detour < X | Review fraud detection with similarity of texts |
| **Meesho** | Product catalog variants (size/colour matrix) | Reseller margins and payouts | Image CDN URL signing with expiry | Cart abandonment reminders scheduler | Catalog search ranking with business rules |

### Fintech

| App | L1 | L2 | L3 | L4 | L5 |
|-----|----|----|----|----|----|
| **Zerodha** | Order book with price-time priority matching | OHLC candles from a tick stream | Stop-loss and GTT triggers | Margin and risk checks before order placement | Market data fan-out: snapshot + incremental deltas |
| **PhonePe** | UPI handle (VPA) resolution and collect requests | Daily transaction limits on a rolling window | QR payload parsing with CRC checksum | Fraud velocity rules (n payments to new payees in m minutes) | Settlement batching and file generation |
| **Splitwise** | Group expenses and balances | Unequal splits with rounding remainder rules | Debt simplification (minimum cash flow) | Multi-currency with exchange-rate snapshots | Settle-up history with undo (compensating entries) |
| **Coinbase** | Wallet balances with pending vs available | Withdrawal limits and cooling periods | Hot/cold wallet rebalancing thresholds | Blockchain confirmations tracking with re-org handling | Proof of reserves with a Merkle sum tree |

### Maps

| App | L1 | L2 | L3 | L4 | L5 |
|-----|----|----|----|----|----|
| **Google Maps** | Geohash encode/decode and neighbours | Map tile coordinates and zoom pyramid | A* search with a distance heuristic | Time-dependent travel times (traffic by hour) | Contraction hierarchies lite: precomputed shortcuts |
| **Strava** | GPS track simplification (Douglas–Peucker) | Distance and pace from GPS points (haversine) | Segment matching: did this ride pass the segment start and end? | Segment leaderboards with ties and date filters | Privacy zones: trim track points near home |

### Gaming

| App | L1 | L2 | L3 | L4 | L5 |
|-----|----|----|----|----|----|
| **Steam** | Friends online status with presence fan-out | Leaderboards with ranks, ties and pagination | Matchmaking by Elo rating with widening windows | Atomic item trades between inventories | Achievement unlock rules engine |
| **Duolingo** | Streaks across time zones with freezes | Spaced repetition scheduling (SM-2) | Weekly leagues: promotion and demotion | XP events idempotent by event id | Experiment assignment with holdout groups |

### Infra

| App | L1 | L2 | L3 | L4 | L5 |
|-----|----|----|----|----|----|
| **Google Search** | Web crawler frontier with per-host politeness | robots.txt rule matching | PageRank by power iteration | Spelling correction with edit distance | Index sharding: document- vs term-partitioned |
| **Wikipedia** | Revision history and diffs | Daily top-1000 pages: batch aggregation of view logs | Link graph: orphans and dead ends | CDN purge on edit with surrogate keys | Read-only mode during database failover |
| **AWS S3** | Object keys, prefixes and delimiter listing | Strong read-after-write vs eventual listing consistency | Versioned buckets and delete markers | Lifecycle rules (transition and expiry) | Erasure coding: rebuild data from k of n parts |
| **Kubernetes** | Pod scheduling with resource requests (bin packing) | Liveness/readiness probes | Rolling update with maxSurge/maxUnavailable | Horizontal autoscaler with stabilization window | Leader election with leases |
| **Truecaller** | Phone number normalisation (E.164) | Spam score from crowd reports with decay | Privacy-preserving contact sync (hashed numbers) | Caller lookup with regional shards and fallback reads | Number reassignment: expire old identities |


## Coverage checklist — what a backend / system-design learner must know

✅ = taught by a built problem (click to open). 📖 = covered in [FUNDAMENTALS.md](FUNDAMENTALS.md) (reading). 📝 = planned above.

| Topic | Where |
|-------|-------|
| HTTP, REST, status codes, idempotent methods | ✅ L0 GitHub REST API |
| Unique ids (auto-increment, Snowflake, UUID) | ✅ L0 Bitly, L2 Twitter Snowflake |
| Hash maps, sets, queues, heaps, trees, tries, graphs | ✅ throughout L1–L3 (Uber, Kafka, Drive, Amazon trie, Instagram graph) |
| Big-O and back-of-envelope estimation | ✅ L1 Uber · 📖 Fundamentals |
| Database indexes (B-tree vs hash), full scans, selectivity | ✅ L0 PostgreSQL index |
| Transactions, ACID, rollback | ✅ L0 Redis KV transactions |
| SQL vs NoSQL, data modelling | 📖 Fundamentals |
| Caching: LRU, TTL, cache-aside, invalidation, negative caching, stampede | ✅ L2 Netflix LRU, L4 Amazon product cache |
| CDN | ✅ L2 Netflix (concepts) · 📖 Fundamentals |
| Load balancing, health checks, stateless servers | ✅ L0 Nginx |
| Horizontal scaling, sharding/partitioning | ✅ L2 Kafka partitions, L3 Netflix consistent hashing |
| Consistent hashing | ✅ L3 Netflix |
| Replication, read replicas, replication lag, read-your-writes | ✅ L0 Flipkart replicas, L5 Kafka ISR |
| Consistency models, CAP/PACELC | ✅ L0 Flipkart, L5 Kafka · 📖 Fundamentals |
| Message queues vs logs, at-least-once, DLQ | ✅ L0 SQS, L1–L5 Kafka, L3 Stripe webhooks |
| Idempotency keys, retries, exponential backoff, jitter | ✅ L2 Stripe, L3 Stripe, L3 YouTube upload |
| Rate limiting (token bucket, windows) | ✅ L4 Twitter rate limiter |
| Sliding windows and time buckets | ✅ L2 Uber surge, L4 Rapido hot zones |
| Pagination (offset vs cursor) | ✅ L1 Twitter |
| Search: inverted index, tokenization, ranking | ✅ L5 Twitter search · 📝 Perplexity BM25 |
| Autocomplete / typeahead | ✅ L3 Amazon trie |
| Geo: distance, grid/geohash index, point-in-polygon, routing | ✅ L1/L3 Uber, L4 Swiggy, L3 Swiggy Dijkstra · 📝 Google Maps |
| Feeds: fan-out on write vs read, celebrity problem | ✅ L4 Instagram |
| Chat: delivery states, offline queues, group fan-out, ordering, presence | ✅ WhatsApp L1–L5 |
| Notifications, webhooks | ✅ L3 Stripe webhooks · 📝 Slack mentions |
| File storage: chunking, resumable uploads, dedup, versioning, sync | ✅ YouTube L3, Drive L1–L5 |
| Authentication: sessions vs tokens, HMAC, revocation | ✅ L0 Google Sign-In |
| Authorization: ACLs, groups, inheritance | ✅ L4 Google Drive sharing |
| Probabilistic structures (Count-Min, Bloom, HyperLogLog) | ✅ L3 Twitter Count-Min · 📝 Hotstar HyperLogLog |
| Resilience: timeouts, circuit breakers, fallbacks, bulkheads | ✅ L5 Netflix circuit breaker |
| Distributed transactions: 2PC, sagas | ✅ L5 Stripe 2PC, L5 Amazon saga |
| Event sourcing, CQRS | ✅ L5 Swiggy |
| Leader election, consensus (Raft/Paxos), fencing | ✅ L4 Kafka generations (fencing) · 📝 Kubernetes leases · 📖 Fundamentals |
| Money: integer amounts, double-entry ledgers, reconciliation | ✅ L1 Amazon cart, L1/L4 Stripe |
| Scheduling: DAGs, worker pools, retries | ✅ L5 YouTube |
| Observability: metrics, percentiles, alerting, tracing | 📝 Datadog · 📖 Fundamentals |
| DNS | 📖 Fundamentals · 📝 Cloudflare |
| AI systems: RAG, vector search, token limits, inference batching, MCP | 📝 ChatGPT, Perplexity, MCP server, Copilot |

