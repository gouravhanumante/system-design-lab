# Concepts — Work queue with acknowledgements

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Message queues

A **queue** lets one part of a system hand work to another **asynchronously**: the producer adds a message and moves on; workers process messages at their own pace. This smooths traffic spikes and keeps slow work (emails, image processing) off the request path.

## Acknowledgements and visibility timeout

A received message isn't deleted — it's **hidden** for a while (the **visibility timeout**). The worker deletes it (**ack**) after finishing. If the worker crashes, the timeout passes and the message reappears for another worker. Nothing is lost.

## At-least-once delivery

Because messages reappear after timeouts, a slow-but-alive worker and a second worker can both process the same message. Queues like SQS guarantee **at-least-once**, so processing must be idempotent.

## Poison messages and DLQs

A message that crashes every worker would be retried forever. After `maxReceives` attempts it's moved to a **dead-letter queue** for investigation.

## Queues vs. logs

A **queue** (SQS, RabbitMQ) deletes messages once processed; each message goes to one worker. A **log** (Kafka) keeps messages; many consumer groups read independently and can replay. Use a queue for "do this job once", a log for "many systems react to this stream of events".
