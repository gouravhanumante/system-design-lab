# Hints — Shopping cart with money in paise

Open one at a time, and only after you have been stuck for a while.

<details><summary>Hint 1 — a nudge</summary>

Validate everything first, change state last. Then a rejected call can never leave the cart half-changed.

</details>

<details><summary>Hint 2 — which data structure?</summary>

`LinkedHashMap<String, Line>` keeps the order SKUs were first added; `Line(price, qty)`.

</details>

<details><summary>Hint 3 — the approach</summary>

Store the coupon as a nullable pair `(percent, cap)`. Integer division `subtotal * percent / 100` already rounds down for positive numbers.

</details>

<details><summary>Scale-up thought (for NOTES.md, section 4)</summary>

Carts are read and written constantly but rarely need strong consistency; Amazon's Dynamo paper used the shopping cart as its main example of an "always writable" store.

</details>
