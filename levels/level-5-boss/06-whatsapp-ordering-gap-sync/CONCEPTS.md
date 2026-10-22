# Concepts — Message ordering and gap recovery across devices

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Sequence numbers

Device clocks are wrong surprisingly often, so timestamps can't order messages reliably. Instead, the **server** gives each message in a chat an increasing **sequence number** (1, 2, 3, …). One authority assigning numbers creates one agreed order.

## Gap detection

If a device has messages 1, 2 and 5, it knows 3 and 4 exist but are missing. Sequence numbers make **gaps** visible — timestamps can't do that. The device shows only the **contiguous prefix** (1, 2) so the conversation never appears in the wrong order.

## Buffering out-of-order data

Messages that arrive early (5 before 3) are kept in a **buffer** until the gap is filled. TCP does exactly this for network packets.

## Catch-up sync with pagination

To fill gaps, the device asks the server "everything after #2", in **pages** of `batchSize`. If a page comes back full, there may be more; if it comes back short, you've reached the end. Paging keeps each response small and lets the client show progress.

## Idempotent sends

The sender's phone may resend a message after a timeout. The server recognises the message id and returns the original sequence number instead of creating a duplicate. Without this, retries would create "ghost" duplicate messages with new numbers.

## Single writer per chat

Handing out sequence numbers needs one place that knows the latest number. Large systems make one server the **owner** of each chat (chosen by hashing the chat id), so numbering stays fast and consistent.
