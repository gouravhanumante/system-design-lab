import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WebhookDispatcherTest {
    private val healthy = HashSet<String>()
    private val sent = ArrayList<String>()
    private val send: (String, String) -> Boolean = { ep, ev ->
        sent.add("$ev->$ep")
        if (ep == "boom") throw RuntimeException("connection refused")
        ep in healthy
    }

    @Test
    fun healthyEndpointGetsItOnFirstTry() {
        healthy.add("shop")
        val d = WebhookDispatcher(send)
        d.enqueue("evt1", "shop", 0)
        assertEquals(listOf("evt1->shop #1 ok"), d.tick(0))
        assertEquals(listOf("evt1->shop"), d.delivered())
        assertEquals(0, d.pending())
    }

    @Test
    fun exponentialBackoffThenDeadLetter() {
        val d = WebhookDispatcher(send, maxAttempts = 3, baseDelaySeconds = 10)
        d.enqueue("evt1", "down", 0)
        assertEquals(listOf("evt1->down #1 fail"), d.tick(0))
        assertEquals(emptyList(), d.tick(9))
        assertEquals(listOf("evt1->down #2 fail"), d.tick(10))
        assertEquals(emptyList(), d.tick(29))
        assertEquals(listOf("evt1->down #3 fail"), d.tick(30))
        assertEquals(listOf("evt1->down"), d.deadLetters())
        assertEquals(0, d.pending())
        assertEquals(emptyList(), d.tick(10_000))
    }

    @Test
    fun delayIsCountedFromTheActualAttemptTime() {
        val d = WebhookDispatcher(send, maxAttempts = 5, baseDelaySeconds = 10)
        d.enqueue("e", "down", 0)
        d.tick(0)                                  // due 10
        d.tick(25)                                 // late tick: attempt 2 at 25 -> due 25 + 20 = 45
        assertEquals(emptyList(), d.tick(44))
        assertEquals(listOf("e->down #3 fail"), d.tick(45))
    }

    @Test
    fun exceptionsCountAsFailures() {
        val d = WebhookDispatcher(send, maxAttempts = 1)
        d.enqueue("e", "boom", 0)
        assertEquals(listOf("e->boom #1 fail"), d.tick(0))
        assertEquals(listOf("e->boom"), d.deadLetters())
    }

    @Test
    fun recoveringEndpointGetsTheRetry() {
        val d = WebhookDispatcher(send, baseDelaySeconds = 5)
        d.enqueue("e", "flaky", 0)
        d.tick(0)
        healthy.add("flaky")
        assertEquals(listOf("e->flaky #2 ok"), d.tick(5))
        assertEquals(listOf("e->flaky"), d.delivered())
    }

    @Test
    fun dueDeliveriesAreProcessedInOrderAndOnlyOncePerTick() {
        healthy.add("a")
        val d = WebhookDispatcher(send, baseDelaySeconds = 1)
        d.enqueue("e2", "a", 5)
        d.enqueue("e1", "b", 5)
        d.enqueue("e1", "a", 5)
        d.enqueue("e0", "b", 7)
        assertEquals(listOf("e1->a #1 ok", "e1->b #1 fail", "e2->a #1 ok", "e0->b #1 fail"), d.tick(100))
        assertEquals(2, d.pending())
    }

    @Test
    fun duplicatesAreIgnored() {
        healthy.add("a")
        val d = WebhookDispatcher(send)
        d.enqueue("e", "a", 0); d.enqueue("e", "a", 0)
        d.tick(0)
        d.enqueue("e", "a", 1)                      // already delivered
        assertEquals(emptyList(), d.tick(1))
        assertEquals(1, sent.size)
    }

    @Test
    fun redriveFromTheDeadLetterQueue() {
        val d = WebhookDispatcher(send, maxAttempts = 2, baseDelaySeconds = 1)
        d.enqueue("e", "x", 0)
        d.tick(0); d.tick(1)
        assertEquals(listOf("e->x"), d.deadLetters())
        assertFalse(d.redrive("e", "nope", 2))
        healthy.add("x")
        assertTrue(d.redrive("e", "x", 50))
        assertEquals(emptyList(), d.deadLetters())
        assertEquals(listOf("e->x #1 ok"), d.tick(50))
        assertFalse(d.redrive("e", "x", 60))       // delivered now, nothing to redrive
    }
}
