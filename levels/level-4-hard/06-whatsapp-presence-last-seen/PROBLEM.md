# Online status, last seen and privacy

| App | Level | Concept |
|-----|-------|---------|
| WhatsApp | 4 — Hard | Heartbeat-based presence, privacy rules, reciprocity |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

"online", "last seen today at 14:32", or nothing at all — WhatsApp shows presence depending on **who is looking**. You can hide your last seen from everyone or show it only to your contacts, and (the subtle part) if you hide yours, you lose the ability to see anyone else's. Presence itself comes from **heartbeats**: while the app is open it pings the server; when pings stop, you are "offline since the last ping".

## Build this (small scale)

A presence service that answers "what does `viewer` see for `target`?". Time is passed in (`now`, seconds).

## API

```kotlin
enum class Visibility { EVERYONE, CONTACTS, NOBODY }

class Presence(onlineTimeoutSeconds: Long = 30) {
    fun heartbeat(user: String, now: Long)
    fun setVisibility(user: String, visibility: Visibility)
    fun addContact(owner: String, contact: String)
    fun view(viewer: String, target: String, now: Long): String
}
```

## Rules

- A user is **online** at `now` if their last heartbeat satisfies `now - lastHeartbeat < onlineTimeoutSeconds`.
- Default visibility is `EVERYONE`. `addContact(owner, contact)` puts `contact` in `owner`'s contact list (one direction only).
- `view` returns exactly one of:
  - `"online"` — target is online **and** visible to the viewer,
  - `"last seen <t>"` — target is offline, has sent at least one heartbeat, is visible; `<t>` is the time of the last heartbeat as a plain number (e.g. `"last seen 120"`),
  - `"unknown"` — target is visible but has **never** sent a heartbeat,
  - `"hidden"` — the viewer may not see the target's presence.
- A target is **visible** to the viewer when:
  - `viewer == target` (you always see yourself — and the reciprocity rule below does not apply to yourself), or
  - target's visibility is `EVERYONE`, or
  - target's visibility is `CONTACTS` **and** the viewer is in the **target's** contact list.
- **Reciprocity:** if the *viewer's own* visibility is `NOBODY`, the viewer sees `"hidden"` for everyone else, regardless of the target's settings.

## Example

`bob` sets `CONTACTS` and adds `alice` as a contact. `alice` sees bob's presence; `carol` sees `"hidden"`. If `alice` sets her own visibility to `NOBODY`, she now sees `"hidden"` for bob too.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`heartbeat()`** + **`view()`** without privacy. 🎯 `onlineWhileHeartbeatsAreFresh`, `becomesLastSeenExactlyAtTheTimeout`, `newerHeartbeatReplacesTheOldOne`, `neverSeenUserIsUnknown`
2. **`setVisibility()`**, **`addContact()`**, **`canSee()`** — privacy rules. 🎯 `nobodyHidesPresenceFromEveryoneElse`, `contactsOnlyUsesTheTargetsContactList`, `reciprocityHidingYoursHidesEveryonesFromYou`, `contactsViewersAreNotBlockedByReciprocity`, `hiddenBeatsUnknown`

## Think about (answer in NOTES.md)

- 500 million users sending a heartbeat every 20 seconds is 25 million writes per second. How would you reduce that load? (Fewer heartbeats? Only store "online" in memory? Only notify friends who are looking?)
- When a user comes online, who needs to be told, and how (push vs. the viewer polling)?
- Why does the reciprocity rule exist? What would a stalker do without it?

---

## Run the tests

```bash
./gradlew :l4-whatsapp-presence-last-seen:test
```

Your code goes in `src/main/kotlin/Presence.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
