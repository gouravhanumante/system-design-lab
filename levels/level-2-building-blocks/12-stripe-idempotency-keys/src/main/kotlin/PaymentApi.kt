data class ChargeRequest(val customer: String, val amountPaise: Long, val currency: String)
data class ChargeResponse(val chargeId: String, val amountPaise: Long, val replayed: Boolean)

/** Provided: thrown when a key is reused for a different request. */
class IdempotencyConflict(message: String) : RuntimeException(message)

class PaymentApi(
    private val processor: (ChargeRequest) -> String,
    private val ttlSeconds: Long = 86_400,
) {

    /** What we remember for one idempotency key: the original request, its response, and when. */
    private class StoredResult(val request: ChargeRequest, val response: ChargeResponse, val storedAt: Long)

    /** idempotency key → stored result. */
    private val byKey = HashMap<String, StoredResult>()

    /** How many times the real payment processor was called (the tests check you don't charge twice). */
    var processorCalls: Int = 0
        private set

    /**
     * Steps 1–3 — charge the customer exactly once per key, even if the app retries.
     * - Blank key → `IllegalArgumentException`.
     * - Key stored and not expired (`now - storedAt < ttlSeconds`):
     *     same request → return the stored response with `replayed = true` (do NOT call the processor);
     *     different request → throw [IdempotencyConflict].
     * - Otherwise (new or expired key): `processorCalls++`, chargeId = `processor(request)`
     *   (if it throws, let it throw and store nothing), store and return `ChargeResponse(chargeId, amount, replayed = false)`.
     */
    fun createCharge(idempotencyKey: String, request: ChargeRequest, now: Long): ChargeResponse {
        TODO("Step 1")
    }
}
