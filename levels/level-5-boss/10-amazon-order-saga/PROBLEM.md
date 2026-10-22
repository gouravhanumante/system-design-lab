# Order saga with compensations

| App | Level | Concept |
|-----|-------|---------|
| Amazon | 5 — Boss | Saga pattern, retries, compensating actions in reverse, idempotent orchestration |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Placing an Amazon order touches several services: reserve inventory, charge the card, book a courier. There is no single database transaction across all of them. If the courier booking fails after the card was charged, you can't "roll back" — you must **undo** each completed step with a **compensating action** (refund the card, release the stock), in reverse order. This is the **saga** pattern.

## Build this (small scale)

An orchestrator that runs a list of steps, retries transient failures, and compensates on permanent failure — writing a log of everything it does.

## API

```kotlin
class Step(val name: String, val action: () -> Unit, val compensate: () -> Unit)

enum class SagaStatus { COMPLETED, COMPENSATED, FAILED }
data class SagaResult(val status: SagaStatus, val log: List<String>)

class SagaOrchestrator(maxRetries: Int = 2) {
    fun run(sagaId: String, steps: List<Step>): SagaResult
}
```

## Rules

A call "fails" when it throws an exception. Each action and each compensation is tried at most `1 + maxRetries` times.

- **Forward**: for each step in order:
  - before the first attempt log `do:<name>`; before retry number `k` (1, 2, …) log `retry:<name>#<k>`;
  - when an attempt succeeds log `ok:<name>` and move on;
  - when all attempts fail log `fail:<name>` and start compensating.
- **Compensation**: for every step that **completed** (logged `ok`), in **reverse** order:
  - before the first attempt log `undo:<name>`; before retry `k` log `retry-undo:<name>#<k>`; on success log `undone:<name>`;
  - if all attempts fail log `stuck:<name>` and **stop** — status `FAILED` (a human must look; the earlier steps are left alone).
- The step that failed is **not** compensated (it never completed).
- Status: all steps ok → `COMPLETED`; failed and fully compensated → `COMPENSATED`; compensation stuck → `FAILED`.
- **Idempotent**: running the same `sagaId` again returns the stored result and executes **nothing**.
- `maxRetries < 0` → `IllegalArgumentException`.

## Example

Steps reserve, charge, ship; `ship` always fails, `maxRetries = 1`:
`do:reserve, ok:reserve, do:charge, ok:charge, do:ship, retry:ship#1, fail:ship, undo:charge, undone:charge, undo:reserve, undone:reserve` → `COMPENSATED`.

## Think about (answer in NOTES.md)

- Compensations are not true rollbacks: the customer may have already received a "payment successful" email. How do you design steps so that compensating is acceptable?
- Compensations and actions get retried, so they must be **idempotent** (refunding twice is a disaster). How would the payment service guarantee that?
- **Orchestration** (a central coordinator, like this) vs **choreography** (each service reacts to events). Trade-offs?
- The orchestrator crashes halfway. What must it have saved to continue after restart?

---

## Run the tests

```bash
./gradlew :l5-amazon-order-saga:test
```

Your code goes in `src/main/kotlin/Saga.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
