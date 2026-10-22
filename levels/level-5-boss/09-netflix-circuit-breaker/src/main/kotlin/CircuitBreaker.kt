enum class State { CLOSED, OPEN, HALF_OPEN }

class CircuitBreaker(
    private val failureThreshold: Int,
    private val openMillis: Long,
    private val halfOpenSuccesses: Int,
    private val clock: () -> Long,
) {

    var rejected: Int = 0
        private set

    fun <T> call(action: () -> T, fallback: () -> T): T {
        TODO()
    }

    fun state(): State {
        TODO()
    }
}
