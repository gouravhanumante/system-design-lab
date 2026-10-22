# Concepts — The append-only log

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Append-only log

A **log** here means an ordered list you can only add to at the end. Nothing is updated or deleted in place. It sounds limited, but it's the backbone of Kafka, database replication (the *write-ahead log*), Git history and event sourcing. Writes are fast because disks are very good at sequential appends.

## Offsets

Each record's position in the log is its **offset**: 0, 1, 2, … A reader doesn't ask "give me new messages"; it asks "give me messages from offset 17". The **reader** remembers where it is, so the log doesn't need to track readers. That's why thousands of readers can share one log cheaply.

## Pull-based consumption

Kafka consumers **pull** data at their own pace instead of the server pushing it. A slow consumer simply falls behind; it doesn't slow the producer or other consumers. Reading "past the end" is normal — it just means "nothing new yet".

## Segments and retention

A real log can't grow forever. It's split into **segment files** (e.g. 1 GB each). Old segments are deleted after a **retention** period (say 7 days), but offsets are never renumbered — offset 17 means the same record forever, it just might not exist anymore.
