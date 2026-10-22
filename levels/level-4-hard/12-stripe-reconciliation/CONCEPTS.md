# Concepts — Reconciling the ledger with the bank statement

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Reconciliation

**Reconciliation** compares two independent records of the same reality — your ledger and the bank's statement — and explains every difference. It's how finance teams discover failed settlements, fees, fraud and bugs. Every payment company runs it daily.

## Exact matching first, fuzzy matching second

Match on strong identifiers (reference ids) first; only then use weaker signals (same amount within a few days). Doing it in **passes** from strongest to weakest keeps the number of wrong matches low.

## Tolerance windows

Banks settle late, so dates rarely match exactly. A **tolerance** (± 2 days) allows for that. Tolerances are trade-offs: wider windows match more but risk pairing the wrong transactions.

## Exception reports

What can't be matched is the important output: missing in bank, missing in ledger, amount mismatches. A good report lets a human resolve each case quickly. Automation handles the 99%; humans handle the exceptions.

## Deterministic tie-breaking

When several candidates fit equally well, a fixed rule (smallest day difference, then earliest day, then list order) makes runs repeatable — essential when auditors ask "why was this matched?".

## Indexing for matching

Comparing every ledger row with every bank row is O(n × m). Grouping bank lines by reference and by amount (hash maps) makes each lookup nearly O(1) — the same "index first" idea as everywhere else.
