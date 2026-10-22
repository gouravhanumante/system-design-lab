data class ChargeRequest(val customer: String, val amountPaise: Long, val currency: String)
data class ChargeResponse(val chargeId: String, val amountPaise: Long, val replayed: Boolean)

/** Provided: thrown when a key is reused for a different request. */
class IdempotencyConflict(message: String) : RuntimeException(message)

class PaymentApi(
    private val processor: (ChargeRequest) -> String,
    private val ttlSeconds: Long = 86_400,
) {

    var processorCalls: Int = 0
        private set

    fun createCharge(idempotencyKey: String, request: ChargeRequest, now: Long): ChargeResponse {
        TODO()
    }
}
