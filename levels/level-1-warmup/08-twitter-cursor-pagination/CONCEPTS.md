# Concepts — Timeline pages with a cursor

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Offset pagination

`?page=3&size=20` means "skip 40 rows, return 20". Simple, and it lets users jump to page 50. Two problems: when rows are inserted or deleted between requests, every later item shifts, so users see **duplicates or gaps**; and the database still has to walk past all skipped rows, so deep pages get slow (`OFFSET 1000000` is expensive).

## Cursor (keyset) pagination

Instead of "skip N", the client sends **where it stopped**: "give me 20 items older than id 9182". The query becomes `WHERE id < 9182 ORDER BY id DESC LIMIT 20`, which uses an index directly no matter how deep you scroll. Insertions and deletions elsewhere don't shift the result. Infinite-scroll feeds (Twitter, Instagram, Slack) all use cursors. Downside: no "jump to page 50".

## The limit + 1 trick

To know whether there is a next page without a second query, fetch **one more** item than requested. If you got it, there's more (and you don't return the extra item). If not, this was the last page.

## Sorted maps and range views

A `TreeMap` keeps keys sorted and can return a **view** of a key range (`headMap`, `tailMap`, `subMap`) cheaply. That's the in-memory equivalent of an index range scan in a database.

## Opaque cursors

APIs usually encode the cursor (e.g. base64 of `id` + sort key) so clients don't depend on its internals. The server can then change the cursor format later without breaking apps.
