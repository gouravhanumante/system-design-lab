# Concepts — Snowflake IDs

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Distributed id generation

Every record needs a unique id. A single auto-increment counter in one database is simple but becomes a bottleneck and a single point of failure. **Distributed id generation** lets each server create ids independently while guaranteeing uniqueness.

## Bit packing

A 64-bit `Long` can hold several smaller numbers side by side. Snowflake uses 41 bits for a timestamp, 10 for the worker id and 12 for a sequence. Combine with shifts and OR (`ts shl 22 or worker shl 12 or seq`); extract with shifts and masks (`(id shr 12) and 0x3FF`). Because the timestamp is in the highest bits, sorting ids ≈ sorting by creation time.

## Sequence within a millisecond

Two ids in the same millisecond on the same worker differ by the sequence number. If more than 4,096 ids are needed in one millisecond, the generator waits for the next millisecond. Overflow handling like this is easy to forget and shows up only under load.

## Clock problems

Server clocks are corrected by NTP and can jump **backwards**. If a generator kept going, it could produce an id it already produced. Options: refuse (throw), wait until the clock catches up, or use a "logical" clock that never goes back. Clocks are one of the most common sources of subtle distributed-systems bugs.

## UUIDs and ULIDs

**UUIDv4** is 128 random bits: no coordination, but not sortable and large for database indexes. **UUIDv7 / ULID** put a timestamp first, so they're time-sortable like Snowflake but need no worker id. Choosing an id format affects index performance, privacy (ids reveal creation time and volume) and URL length.
