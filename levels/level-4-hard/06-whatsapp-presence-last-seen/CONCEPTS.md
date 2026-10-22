# Concepts — Online status, last seen and privacy

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Presence

**Presence** is "who is online right now". It changes constantly and is only interesting in the moment, so it's usually kept in fast memory (Redis) instead of a durable database.

## Heartbeats

Apps can crash or lose network without saying goodbye. Instead of relying on a "logout" event, clients send a **heartbeat** every few seconds; the server marks them offline if heartbeats stop for longer than a **timeout**. The last heartbeat time doubles as "last seen".

## Privacy rules as a decision tree

"What does this viewer see?" depends on several settings. Writing the rules as an explicit, ordered decision tree (self? viewer hidden? target's audience?) and putting it in **one** function avoids leaks where one code path forgets a check.

## Reciprocity

Some products enforce fairness: if you hide your information, you can't see others'. It's a product rule, not a technical one, but it must be applied consistently everywhere presence is shown.

## Push vs. pull for live status

Viewers can **poll** ("is bob online?") or the server can **push** changes to everyone looking at bob's chat. Pushing to all contacts on every change is enormous traffic; real systems push only to users currently viewing that chat (*subscription-based presence*).
