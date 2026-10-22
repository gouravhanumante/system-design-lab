# Concepts — Group chat fan-out and "read by all"

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Fan-out

**Fan-out** is turning one event into many deliveries: one group message → one delivery per member. It multiplies work: a 1,000-member group sending 10 messages per second creates 10,000 deliveries per second.

## Snapshot at send time

Who receives a message is decided **when it's sent**. If you computed recipients later ("whoever is in the group now"), new members would see old messages they weren't meant to see, and people who left would lose messages they were entitled to. Capturing a snapshot of relevant state at the moment of the event is a common correctness rule.

## Per-recipient state

Read receipts are tracked **per recipient per message**. That's a lot of small records; real systems compress it, e.g. by storing per user "read everything up to message #N" instead of one flag per message.

## Set operations

Many questions here are set math: recipients = members − sender; still-relevant recipients = recipients ∩ current members; "read by all" = still-relevant ⊆ readers. Thinking in sets makes the code short and the rules clear.

## End-to-end encryption and fan-out

With end-to-end encryption, the server can't read messages, so a group message must be encrypted for each recipient's device (or with a shared group key that is rotated when members leave — the *Signal Sender Keys* approach). Fan-out then happens partly on the sender's device.
