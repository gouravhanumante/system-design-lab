# Concepts — Webhook delivery with backoff and a dead-letter queue

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Webhooks

A **webhook** is an HTTP call *from* a service *to* your server when something happens ("payment succeeded"). It's a push alternative to polling. The sender can't assume your server is up, so it must retry.

## At-least-once delivery

Retrying until success means the receiver may get the same event **more than once** (e.g. it processed the event but its "200 OK" got lost). Receivers must de-duplicate by event id — idempotency again.

## Exponential backoff

After each failure, wait longer before retrying: 10 s, 20 s, 40 s, 80 s… A struggling receiver isn't hammered, and short outages are still recovered quickly. Adding random **jitter** spreads retries out so thousands of failed deliveries don't all retry at the same instant.

## Dead-letter queue (DLQ)

After a maximum number of attempts, the message is moved aside to a **dead-letter queue** instead of retrying forever. Humans can inspect it, fix the cause, and **redrive** it (send it back for delivery). DLQs exist in SQS, RabbitMQ, Kafka setups and every serious job system.

## Delay queues

Pending retries are ordered by "next attempt time". A queue that releases items only when they're due is a **delay queue**; it's usually a sorted structure or a timer wheel underneath.
