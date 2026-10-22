# Problem catalog

Every problem has the same folder layout (see the README). Levels run easy → hard across *all* apps; inside a level, apps are in the same order.


## Level 0 — Foundations

| # | App | Problem | Concept | Module |
|--:|-----|---------|---------|--------|
| 1 | GitHub | [A REST API for issues](levels/level-0-foundations/01-github-rest-api-basics/PROBLEM.md) | Resources, HTTP methods, status codes, validation | `l0-github-rest-api-basics` |
| 2 | Bitly | [URL shortener](levels/level-0-foundations/02-bitly-url-shortener/PROBLEM.md) | Base62 encoding, counters vs hashes, custom aliases, redirects | `l0-bitly-url-shortener` |
| 3 | Redis | [Key-value store with transactions](levels/level-0-foundations/03-redis-kv-transactions/PROBLEM.md) | Transactions, atomicity, rollback, nested layers of changes | `l0-redis-kv-transactions` |
| 4 | PostgreSQL | [Database indexes vs full table scans](levels/level-0-foundations/04-postgresql-database-index/PROBLEM.md) | Full scan, sorted (B-tree-like) index, hash index, binary search, index maintenance | `l0-postgresql-database-index` |
| 5 | Nginx | [Load balancer with health checks](levels/level-0-foundations/05-nginx-load-balancer/PROBLEM.md) | Round robin, least connections, health checks with rise/fall thresholds | `l0-nginx-load-balancer` |
| 6 | Amazon SQS | [Work queue with acknowledgements](levels/level-0-foundations/06-amazon-sqs-work-queue-ack/PROBLEM.md) | Message queues, visibility timeout, ack, redelivery, dead-letter queue | `l0-amazon-sqs-work-queue-ack` |
| 7 | Flipkart | [Read replicas and read-your-writes](levels/level-0-foundations/07-flipkart-read-replicas/PROBLEM.md) | Primary/replica, replication lag, eventual consistency, read-your-writes routing | `l0-flipkart-read-replicas` |
| 8 | Google Sign-In | [Stateless login tokens (HMAC-signed)](levels/level-0-foundations/08-google-sign-in-signed-tokens/PROBLEM.md) | Sessions vs tokens, HMAC signatures, expiry, revocation, stateless servers | `l0-google-sign-in-signed-tokens` |

## Level 1 — Warm-up

| # | App | Problem | Concept | Module |
|--:|-----|---------|---------|--------|
| 1 | Uber | [Find the nearest drivers](levels/level-1-warmup/01-uber-nearest-driver/PROBLEM.md) | Brute-force scan, sorting, tie-breaking | `l1-uber-nearest-driver` |
| 2 | Rapido | [Ride-start OTP](levels/level-1-warmup/02-rapido-ride-otp/PROBLEM.md) | Per-ride secret, one-time use, injected randomness | `l1-rapido-ride-otp` |
| 3 | YouTube | [Video view counter and top videos](levels/level-1-warmup/03-youtube-view-counter/PROBLEM.md) | Hash map counting, ranking with tie-breaks | `l1-youtube-view-counter` |
| 4 | Google Drive | [Folders, files and total size](levels/level-1-warmup/04-google-drive-folder-tree/PROBLEM.md) | Tree of nodes, path parsing, recursion | `l1-google-drive-folder-tree` |
| 5 | Kafka | [The append-only log](levels/level-1-warmup/05-kafka-append-log/PROBLEM.md) | Append-only log, offsets, reading from a position | `l1-kafka-append-log` |
| 6 | WhatsApp | [Message ticks (sent, delivered, read)](levels/level-1-warmup/06-whatsapp-message-ticks/PROBLEM.md) | Monotonic state, ordered enum, ignoring stale updates | `l1-whatsapp-message-ticks` |
| 7 | Instagram | [Likes that can't be double-counted](levels/level-1-warmup/07-instagram-like-counter/PROBLEM.md) | Idempotent set membership, recency ordering | `l1-instagram-like-counter` |
| 8 | Twitter | [Timeline pages with a cursor](levels/level-1-warmup/08-twitter-cursor-pagination/PROBLEM.md) | Cursor (keyset) pagination vs offset pagination | `l1-twitter-cursor-pagination` |
| 9 | Netflix | [Continue watching](levels/level-1-warmup/09-netflix-continue-watching/PROBLEM.md) | Per-profile state, recency ordering, completion threshold | `l1-netflix-continue-watching` |
| 10 | Amazon | [Shopping cart with money in paise](levels/level-1-warmup/10-amazon-shopping-cart/PROBLEM.md) | Integer money, validation rules, coupon caps | `l1-amazon-shopping-cart` |
| 11 | Swiggy | [Is the restaurant open?](levels/level-1-warmup/11-swiggy-opening-hours/PROBLEM.md) | Time intervals, wrap-around past midnight and end of week, modular arithmetic | `l1-swiggy-opening-hours` |
| 12 | Stripe | [Double-entry ledger](levels/level-1-warmup/12-stripe-double-entry-ledger/PROBLEM.md) | Double-entry bookkeeping, invariants, idempotent transactions | `l1-stripe-double-entry-ledger` |

## Level 2 — Building blocks

| # | App | Problem | Concept | Module |
|--:|-----|---------|---------|--------|
| 1 | Uber | [Surge pricing for a zone](levels/level-2-building-blocks/01-uber-surge-pricing/PROBLEM.md) | Sliding time window, queue eviction | `l2-uber-surge-pricing` |
| 2 | Rapido | [OTP with expiry and attempt limit](levels/level-2-building-blocks/02-rapido-otp-expiry-attempts/PROBLEM.md) | Time-to-live, attempt counting, lockout, result codes | `l2-rapido-otp-expiry-attempts` |
| 3 | YouTube | [Count a view only once per viewer](levels/level-2-building-blocks/03-youtube-view-dedup-window/PROBLEM.md) | Per-key timestamps, windowed de-duplication | `l2-youtube-view-dedup-window` |
| 4 | Google Drive | [Version history and restore](levels/level-2-building-blocks/04-google-drive-file-versions/PROBLEM.md) | Append-only history, retention limit, never reusing ids | `l2-google-drive-file-versions` |
| 5 | Kafka | [Partitions and key-based routing](levels/level-2-building-blocks/05-kafka-partitions/PROBLEM.md) | Hash partitioning, per-partition ordering, round-robin | `l2-kafka-partitions` |
| 6 | WhatsApp | [Delivering messages to offline users](levels/level-2-building-blocks/06-whatsapp-offline-queue/PROBLEM.md) | Per-user queue, FIFO delivery on reconnect, TTL, idempotent sends | `l2-whatsapp-offline-queue` |
| 7 | Instagram | [Follow graph and "suggested for you"](levels/level-2-building-blocks/07-instagram-follow-graph/PROBLEM.md) | Directed graph as adjacency sets, 2-hop traversal, ranking by overlap | `l2-instagram-follow-graph` |
| 8 | Twitter | [Snowflake IDs](levels/level-2-building-blocks/08-twitter-snowflake-ids/PROBLEM.md) | Unique, time-sortable ids without a central counter; bit packing | `l2-twitter-snowflake-ids` |
| 9 | Netflix | [LRU cache on an edge server](levels/level-2-building-blocks/09-netflix-lru-edge-cache/PROBLEM.md) | Least-recently-used eviction with a byte budget, O(1) operations | `l2-netflix-lru-edge-cache` |
| 10 | Amazon | [Inventory holds during checkout](levels/level-2-building-blocks/10-amazon-inventory-holds/PROBLEM.md) | Reservations with expiry, preventing overselling, idempotent confirm | `l2-amazon-inventory-holds` |
| 11 | Swiggy | [Delivery slots with capacity and a waitlist](levels/level-2-building-blocks/11-swiggy-slot-booking-waitlist/PROBLEM.md) | Capacity limits, FIFO waitlist promotion, idempotent booking | `l2-swiggy-slot-booking-waitlist` |
| 12 | Stripe | [Idempotency keys for a payment API](levels/level-2-building-blocks/12-stripe-idempotency-keys/PROBLEM.md) | Idempotent POST requests, stored responses, key conflicts, key expiry | `l2-stripe-idempotency-keys` |

## Level 3 — Realistic

| # | App | Problem | Concept | Module |
|--:|-----|---------|---------|--------|
| 1 | Uber | [Grid index for nearby drivers](levels/level-3-realistic/01-uber-geo-grid-index/PROBLEM.md) | Spatial index, bucketing, avoiding full scans | `l3-uber-geo-grid-index` |
| 2 | Rapido | [Match many requests to many captains](levels/level-3-realistic/02-rapido-batch-matching/PROBLEM.md) | Global greedy matching vs first-come-first-served | `l3-rapido-batch-matching` |
| 3 | YouTube | [Resumable chunked upload](levels/level-3-realistic/03-youtube-chunked-resumable-upload/PROBLEM.md) | Out-of-order chunks, idempotent retries, integrity checks | `l3-youtube-chunked-resumable-upload` |
| 4 | Google Drive | [Deduplicated chunk storage](levels/level-3-realistic/04-google-drive-chunk-dedup/PROBLEM.md) | Content-addressed chunks, reference counting, garbage collection | `l3-google-drive-chunk-dedup` |
| 5 | Kafka | [Consumer groups, commits and replay](levels/level-3-realistic/05-kafka-consumer-offsets/PROBLEM.md) | Committed offsets, at-least-once delivery, lag | `l3-kafka-consumer-offsets` |
| 6 | WhatsApp | [Group chat fan-out and "read by all"](levels/level-3-realistic/06-whatsapp-group-fanout/PROBLEM.md) | Snapshot of recipients at send time, per-recipient state, fan-out | `l3-whatsapp-group-fanout` |
| 7 | Instagram | [Nested comments with soft delete](levels/level-3-realistic/07-instagram-comment-threads/PROBLEM.md) | Tree of replies, depth-first rendering, pruning deleted branches | `l3-instagram-comment-threads` |
| 8 | Twitter | [Trending hashtags with a Count-Min Sketch](levels/level-3-realistic/08-twitter-count-min-trending/PROBLEM.md) | Probabilistic counting in fixed memory, heavy hitters | `l3-twitter-count-min-trending` |
| 9 | Netflix | [Consistent hashing for cache servers](levels/level-3-realistic/09-netflix-consistent-hashing/PROBLEM.md) | Hash ring, virtual nodes, minimal key movement | `l3-netflix-consistent-hashing` |
| 10 | Amazon | [Search autocomplete with a trie](levels/level-3-realistic/10-amazon-autocomplete-trie/PROBLEM.md) | Trie (prefix tree), precomputed top-k per node, read-optimised indexes | `l3-amazon-autocomplete-trie` |
| 11 | Swiggy | [Fastest route and ETA (Dijkstra)](levels/level-3-realistic/11-swiggy-dijkstra-eta/PROBLEM.md) | Weighted graph, Dijkstra's shortest path, priority queue, road closures | `l3-swiggy-dijkstra-eta` |
| 12 | Stripe | [Webhook delivery with backoff and a dead-letter queue](levels/level-3-realistic/12-stripe-webhook-retries/PROBLEM.md) | At-least-once delivery, exponential backoff, dead-letter queue, redrive | `l3-stripe-webhook-retries` |

## Level 4 — Hard

| # | App | Problem | Concept | Module |
|--:|-----|---------|---------|--------|
| 1 | Uber | [Dispatch with offer timeouts](levels/level-4-hard/01-uber-dispatch-timeouts/PROBLEM.md) | Time-driven state machine, retry to next candidate | `l4-uber-dispatch-timeouts` |
| 2 | Rapido | [Live demand heatmap (hot zones)](levels/level-4-hard/02-rapido-hot-zones-window/PROBLEM.md) | Ring buffer of time buckets, running totals, top-k | `l4-rapido-hot-zones-window` |
| 3 | YouTube | [View counts at scale — write batching](levels/level-4-hard/03-youtube-batched-view-counts/PROBLEM.md) | Write coalescing, buffering in front of a database | `l4-youtube-batched-view-counts` |
| 4 | Google Drive | [Sharing, groups and inherited permissions](levels/level-4-hard/04-google-drive-sharing-permissions/PROBLEM.md) | Permission inheritance up a tree, groups, max-wins resolution | `l4-google-drive-sharing-permissions` |
| 5 | Kafka | [Group coordinator and rebalancing](levels/level-4-hard/05-kafka-consumer-group-rebalance/PROBLEM.md) | Partition assignment, membership, heartbeats, generations | `l4-kafka-consumer-group-rebalance` |
| 6 | WhatsApp | [Online status, last seen and privacy](levels/level-4-hard/06-whatsapp-presence-last-seen/PROBLEM.md) | Heartbeat-based presence, privacy rules, reciprocity | `l4-whatsapp-presence-last-seen` |
| 7 | Instagram | [Home feed: push, pull, or both?](levels/level-4-hard/07-instagram-hybrid-feed-fanout/PROBLEM.md) | Fan-out on write vs read, the celebrity problem, merge and de-duplicate | `l4-instagram-hybrid-feed-fanout` |
| 8 | Twitter | [API rate limiter (token buckets, per user and global)](levels/level-4-hard/08-twitter-api-rate-limiter/PROBLEM.md) | Token bucket algorithm, layered limits, Retry-After | `l4-twitter-api-rate-limiter` |
| 9 | Netflix | [Adaptive bitrate: picking video quality](levels/level-4-hard/09-netflix-adaptive-bitrate/PROBLEM.md) | Throughput estimation (harmonic mean), buffer-based safety rules, hysteresis | `l4-netflix-adaptive-bitrate` |
| 10 | Amazon | [Product page cache (cache-aside, stale-while-revalidate)](levels/level-4-hard/10-amazon-product-cache/PROBLEM.md) | Cache-aside, TTL, invalidation on write, negative caching, background refresh | `l4-amazon-product-cache` |
| 11 | Swiggy | [Delivery zones (point in polygon)](levels/level-4-hard/11-swiggy-geofence-serviceability/PROBLEM.md) | Geofencing, ray casting, boundary cases, bounding-box prefilter | `l4-swiggy-geofence-serviceability` |
| 12 | Stripe | [Reconciling the ledger with the bank statement](levels/level-4-hard/12-stripe-reconciliation/PROBLEM.md) | Multi-pass record matching, tolerance windows, exception reports | `l4-stripe-reconciliation` |

## Level 5 — Boss

| # | App | Problem | Concept | Module |
|--:|-----|---------|---------|--------|
| 1 | Uber | [Trip lifecycle, fare and cancellation fees](levels/level-5-boss/01-uber-trip-lifecycle-fare/PROBLEM.md) | State machine, invalid-transition guards, pricing rules | `l5-uber-trip-lifecycle-fare` |
| 2 | Rapido | ["Share trip" route deviation alerts](levels/level-5-boss/02-rapido-route-deviation-alerts/PROBLEM.md) | Point-to-segment geometry, streak counting, alert de-duplication | `l5-rapido-route-deviation-alerts` |
| 3 | YouTube | [Video processing pipeline (job DAG scheduler)](levels/level-5-boss/03-youtube-transcode-job-dag/PROBLEM.md) | Dependency graph, worker pool simulation, retries, failure propagation | `l5-youtube-transcode-job-dag` |
| 4 | Google Drive | [Offline sync and conflict copies](levels/level-5-boss/04-google-drive-sync-conflicts/PROBLEM.md) | Optimistic concurrency with revisions, conflict detection, convergence | `l5-google-drive-sync-conflicts` |
| 5 | Kafka | [Replication, ISR and high watermark](levels/level-5-boss/05-kafka-replication-isr/PROBLEM.md) | Leader/follower replication, in-sync replicas, no loss of committed data | `l5-kafka-replication-isr` |
| 6 | WhatsApp | [Message ordering and gap recovery across devices](levels/level-5-boss/06-whatsapp-ordering-gap-sync/PROBLEM.md) | Server-assigned sequence numbers, buffering, gap detection, batched catch-up | `l5-whatsapp-ordering-gap-sync` |
| 7 | Instagram | [Stories: 24-hour expiry, audiences and highlights](levels/level-5-boss/07-instagram-stories-expiry/PROBLEM.md) | TTL, audience rules evaluated at read time, unique viewers, exceptions to expiry | `l5-instagram-stories-expiry` |
| 8 | Twitter | [Tweet search with an inverted index](levels/level-5-boss/08-twitter-search-inverted-index/PROBLEM.md) | Inverted index, positional postings, phrase and exclusion queries, deletes | `l5-twitter-search-inverted-index` |
| 9 | Netflix | [Circuit breaker with fallbacks](levels/level-5-boss/09-netflix-circuit-breaker/PROBLEM.md) | Fail fast, CLOSED/OPEN/HALF_OPEN states, recovery probing | `l5-netflix-circuit-breaker` |
| 10 | Amazon | [Order saga with compensations](levels/level-5-boss/10-amazon-order-saga/PROBLEM.md) | Saga pattern, retries, compensating actions in reverse, idempotent orchestration | `l5-amazon-order-saga` |
| 11 | Swiggy | [Order tracking with event sourcing](levels/level-5-boss/11-swiggy-order-event-sourcing/PROBLEM.md) | Event sourcing, versioned events, projections, out-of-order buffering, replay | `l5-swiggy-order-event-sourcing` |
| 12 | Stripe | [Two-phase commit across banks (UPI-style)](levels/level-5-boss/12-stripe-two-phase-commit/PROBLEM.md) | Atomic commitment, prepare/commit votes, durable decision log, crash recovery | `l5-stripe-two-phase-commit` |
