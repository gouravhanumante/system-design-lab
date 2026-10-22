# Concepts — Shopping cart with money in paise

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Money as integers

Floating-point numbers can't represent most decimal fractions exactly (`0.1 + 0.2 = 0.30000000000000004`). Financial code stores money as **integers in the smallest unit** (paise, cents) or uses decimal types (`BigDecimal`). Rounding happens only at well-defined points with a well-defined rule.

## Validate first, then change

If a method checks inputs halfway through modifying state, a failure leaves the object half-changed. Doing **all** validation before **any** modification keeps operations all-or-nothing — a tiny version of transactional thinking.

## Business invariants

An **invariant** is a rule that must always be true: "quantity ≤ 10", "total ≥ 0", "a SKU has one price in the cart". Enforcing invariants inside the class (instead of trusting callers) prevents whole categories of bugs.

## Caps and percentage discounts

"20% off, maximum ₹200" is `min(subtotal × 20 / 100, 200)`. The order of operations and the rounding direction (down, in the customer's favour or the shop's?) must be explicit, because they change the final amount.

## Price snapshots

A cart line stores the price at the time of adding. Whether checkout uses that price or the current one is a product decision; either way it must be deliberate and visible to the user.
