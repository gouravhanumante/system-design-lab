# Concepts — Stories: 24-hour expiry, audiences and highlights

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Time-to-live (TTL)

Stories disappear after 24 hours: a **TTL**. Two ways to enforce it: check the age on every read (always correct, but expired data still takes space), and delete expired data in the background (saves space, but there's a delay). Real systems do both.

## Authorization at read time

Who may see a story is computed **when someone looks**, from the current settings. Removing a close friend takes effect immediately. The alternative — copying permissions into each story at post time — is faster to read but goes stale. This is the same design choice as permission inheritance in Google Drive.

## Unique viewers

"Seen by" lists each viewer once, in order of their first view. An insertion-ordered set gives this for free. For very large audiences, systems store only a sample or a count (approximate with HyperLogLog).

## Exceptions to expiry (pinning)

Highlights are an **exception** to the TTL: pinned content is kept. Make the exception explicit (a flag checked in one place) rather than special-casing it in many places. Note the rule that you can only pin something that hasn't expired yet — rescuing expired data would make the TTL unreliable.

## Audit history vs. current state

The viewer list is **history** ("bob saw it at 10:00") and doesn't change when permissions change later. Access rights are **current state**. Keeping history and current state separate avoids confusing bugs.
