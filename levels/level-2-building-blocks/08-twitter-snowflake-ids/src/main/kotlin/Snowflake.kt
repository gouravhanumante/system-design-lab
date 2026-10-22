data class SnowflakeParts(val timestamp: Long, val workerId: Int, val sequence: Int)

class SnowflakeGenerator(
    private val workerId: Int,
    private val epochMillis: Long = 0,
    private val clock: () -> Long,
) {

    fun nextId(): Long {
        TODO()
    }
}

fun decode(id: Long): SnowflakeParts {
    TODO()
}
