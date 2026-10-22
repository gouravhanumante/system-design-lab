import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PaymentApiTest {
    private var next = 0
    private var failNext = false
    private val processor: (ChargeRequest) -> String = {
        if (failNext) { failNext = false; throw IllegalStateException("bank timeout") }
        "ch_${++next}"
    }
    private val req = ChargeRequest("cus_1", 49_900, "INR")

    @Test
    fun firstCallChargesAndRetryReplays() {
        val api = PaymentApi(processor)
        assertEquals(ChargeResponse("ch_1", 49_900, false), api.createCharge("k1", req, 0))
        assertEquals(ChargeResponse("ch_1", 49_900, true), api.createCharge("k1", req, 5))
        assertEquals(1, api.processorCalls)
    }

    @Test
    fun differentKeysAreDifferentCharges() {
        val api = PaymentApi(processor)
        assertEquals("ch_1", api.createCharge("k1", req, 0).chargeId)
        assertEquals("ch_2", api.createCharge("k2", req, 0).chargeId)
        assertEquals(2, api.processorCalls)
    }

    @Test
    fun sameKeyDifferentRequestIsAConflict() {
        val api = PaymentApi(processor)
        api.createCharge("k1", req, 0)
        assertFailsWith<IdempotencyConflict> { api.createCharge("k1", req.copy(amountPaise = 99_900), 1) }
        assertFailsWith<IdempotencyConflict> { api.createCharge("k1", req.copy(customer = "cus_2"), 1) }
        assertEquals(1, api.processorCalls)
    }

    @Test
    fun keysExpire() {
        val api = PaymentApi(processor, ttlSeconds = 100)
        api.createCharge("k1", req, 0)
        assertEquals(true, api.createCharge("k1", req, 99).replayed)
        val fresh = api.createCharge("k1", req.copy(amountPaise = 1), 100)   // expired: a new charge, no conflict
        assertEquals(ChargeResponse("ch_2", 1, false), fresh)
        assertEquals(true, api.createCharge("k1", req.copy(amountPaise = 1), 150).replayed)
    }

    @Test
    fun processorFailureStoresNothing() {
        val api = PaymentApi(processor)
        failNext = true
        assertFailsWith<IllegalStateException> { api.createCharge("k1", req, 0) }
        assertEquals(ChargeResponse("ch_1", 49_900, false), api.createCharge("k1", req, 1))
        assertEquals(2, api.processorCalls)
    }

    @Test
    fun blankKeysAreRejected() {
        val api = PaymentApi(processor)
        assertFailsWith<IllegalArgumentException> { api.createCharge("", req, 0) }
        assertFailsWith<IllegalArgumentException> { api.createCharge("   ", req, 0) }
        assertEquals(0, api.processorCalls)
    }
}
