enum class EventType { PLACED, ACCEPTED, PICKED_UP, DELIVERED, CANCELLED }

data class OrderEvent(val orderId: String, val version: Int, val type: EventType, val value: String = "")

data class OrderView(
    val orderId: String,
    val status: EventType,
    val items: List<String>,
    val rider: String?,
    val cancelReason: String?,
    val version: Int,
)

enum class ApplyResult { APPLIED, DUPLICATE, BUFFERED, REJECTED }

class OrderProjection {

    fun apply(e: OrderEvent): ApplyResult {
        TODO()
    }

    fun view(orderId: String): OrderView? {
        TODO()
    }

    fun bufferedCount(orderId: String): Int {
        TODO()
    }
}

fun rebuild(events: List<OrderEvent>): OrderProjection {
    TODO()
}
