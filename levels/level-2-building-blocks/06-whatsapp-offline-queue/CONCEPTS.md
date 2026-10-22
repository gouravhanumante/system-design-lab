# Concepts — Delivering messages to offline users

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Store-and-forward

If the recipient is offline, the server **stores** the message and **forwards** it when they reconnect. Email has worked this way since the 1970s. The server acts as a mailbox, so sender and receiver don't need to be online at the same time.

## Queue (FIFO)

Messages must arrive in the order they were sent, so each user's mailbox is a **first-in-first-out queue**. On reconnect, deliver from the front.

## Time-to-live (TTL)

Messages waiting too long are dropped (WhatsApp: about 30 days). A **TTL** bounds storage and avoids delivering stale content. Check expiry at delivery time, and/or remove expired items in a background job.

## Idempotency keys

A phone that didn't get an acknowledgement will **retry** the send. If the server treats the retry as a new message, the recipient sees it twice. The client attaches a unique **message id**; the server remembers ids it has seen and ignores repeats. The same pattern protects payments (*idempotency keys* in Stripe's API).

## Online/offline routing

The server needs to know whether a user is connected right now, and on which server their connection lives, to decide "deliver now" vs. "queue". At scale this is a **session registry** (often Redis) mapping user → connection server.
