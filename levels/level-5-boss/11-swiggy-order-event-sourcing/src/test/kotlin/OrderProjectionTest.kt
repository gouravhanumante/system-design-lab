import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OrderProjectionTest {
    private fun placed(o: String = "o1") = OrderEvent(o, 1, EventType.PLACED, "dosa,coffee")
    private fun accepted(o: String = "o1") = OrderEvent(o, 2, EventType.ACCEPTED)
    private fun picked(o: String = "o1", rider: String = "ravi") = OrderEvent(o, 3, EventType.PICKED_UP, rider)
    private fun delivered(o: String = "o1") = OrderEvent(o, 4, EventType.DELIVERED)

    @Test
    fun happyPathInOrder() {
        val p = OrderProjection()
        listOf(placed(), accepted(), picked(), delivered()).forEach { assertEquals(ApplyResult.APPLIED, p.apply(it)) }
        assertEquals(OrderView("o1", EventType.DELIVERED, listOf("dosa", "coffee"), "ravi", null, 4), p.view("o1"))
    }

    @Test
    fun duplicatesAreIgnored() {
        val p = OrderProjection()
        p.apply(placed())
        assertEquals(ApplyResult.DUPLICATE, p.apply(placed()))
        assertEquals(1, p.view("o1")!!.version)
    }

    @Test
    fun earlyEventsAreBufferedAndDrained() {
        val p = OrderProjection()
        assertEquals(ApplyResult.BUFFERED, p.apply(picked()))
        assertNull(p.view("o1"))
        assertEquals(ApplyResult.APPLIED, p.apply(placed()))
        assertEquals(ApplyResult.DUPLICATE, p.apply(placed()))
        assertEquals(1, p.bufferedCount("o1"))
        assertEquals(ApplyResult.APPLIED, p.apply(accepted()))
        assertEquals(0, p.bufferedCount("o1"))
        val v = p.view("o1")!!
        assertEquals(EventType.PICKED_UP, v.status)
        assertEquals("ravi", v.rider)
        assertEquals(3, v.version)
    }

    @Test
    fun invalidTransitionsAreRejectedWithoutConsumingTheVersion() {
        val p = OrderProjection()
        assertEquals(ApplyResult.REJECTED, p.apply(OrderEvent("o1", 1, EventType.ACCEPTED)))
        assertNull(p.view("o1"))
        p.apply(placed())
        assertEquals(ApplyResult.REJECTED, p.apply(OrderEvent("o1", 2, EventType.DELIVERED)))
        assertEquals(ApplyResult.APPLIED, p.apply(accepted()))           // version 2 is still free
    }

    @Test
    fun cancellationRules() {
        val p = OrderProjection()
        p.apply(placed()); p.apply(accepted())
        assertEquals(ApplyResult.APPLIED, p.apply(OrderEvent("o1", 3, EventType.CANCELLED, "restaurant busy")))
        assertEquals(EventType.CANCELLED, p.view("o1")!!.status)
        assertEquals("restaurant busy", p.view("o1")!!.cancelReason)
        assertEquals(ApplyResult.REJECTED, p.apply(OrderEvent("o1", 4, EventType.PICKED_UP, "ravi")))

        val q = OrderProjection()
        q.apply(placed("o2")); q.apply(accepted("o2")); q.apply(picked("o2"))
        assertEquals(ApplyResult.REJECTED, q.apply(OrderEvent("o2", 4, EventType.CANCELLED, "too late")))
    }

    @Test
    fun invalidBufferedEventStopsDraining() {
        val p = OrderProjection()
        p.apply(OrderEvent("o1", 3, EventType.DELIVERED))               // impossible right after ACCEPTED
        p.apply(OrderEvent("o1", 4, EventType.DELIVERED))
        p.apply(placed())
        assertEquals(ApplyResult.APPLIED, p.apply(accepted()))
        assertEquals(2, p.view("o1")!!.version)                          // v3 dropped, draining stopped
        assertEquals(1, p.bufferedCount("o1"))                           // v4 still waiting
        assertEquals(ApplyResult.APPLIED, p.apply(picked()))             // a correct v3 arrives...
        assertEquals(EventType.DELIVERED, p.view("o1")!!.status)         // ...and v4 drains
    }

    @Test
    fun ordersAreIndependent() {
        val p = OrderProjection()
        p.apply(placed("a")); p.apply(placed("b")); p.apply(accepted("b"))
        assertEquals(EventType.PLACED, p.view("a")!!.status)
        assertEquals(EventType.ACCEPTED, p.view("b")!!.status)
        assertNull(p.view("c"))
        assertEquals(0, p.bufferedCount("c"))
    }

    @Test
    fun rebuildFromAnyOrderGivesTheSameState() {
        val history = ArrayList<OrderEvent>()
        for (i in 0 until 30) {
            val o = "order-$i"
            history.add(placed(o)); history.add(accepted(o))
            when (i % 3) {
                0 -> { history.add(picked(o, "rider$i")); history.add(delivered(o)) }
                1 -> history.add(OrderEvent(o, 3, EventType.CANCELLED, "reason$i"))
                else -> {}
            }
        }
        val reference = rebuild(history)
        val rnd = Random(21)
        repeat(5) {
            val shuffled = (history + history.shuffled(rnd).take(20)).shuffled(rnd)   // with duplicates
            val p = rebuild(shuffled)
            for (i in 0 until 30) assertEquals(reference.view("order-$i"), p.view("order-$i"))
        }
        assertEquals("rider0", reference.view("order-0")!!.rider)
        assertEquals("reason1", reference.view("order-1")!!.cancelReason)
    }
}
