data class SnowflakeParts(val timestamp: Long, val workerId: Int, val sequence: Int)

/*
 * A Snowflake id is one 64-bit Long:
 *
 *   | 1 bit: 0 | 41 bits: timestamp (ms since epoch) | 10 bits: workerId (0..1023) | 12 bits: sequence (0..4095) |
 *
 *   id = (timestamp shl 22) or (workerId shl 12) or sequence
 */

class SnowflakeGenerator(
    private val workerId: Int,
    private val epochMillis: Long = 0,
    private val clock: () -> Long,
) {

    /** Timestamp (ms since [epochMillis]) used for the previous id; -1 before the first. */
    private var lastTimestamp = -1L

    /** Counter for ids created in the same millisecond. */
    private var sequence = 0

    /** Step 1 — [workerId] outside 0..1023 → `IllegalArgumentException`. */
    init {
        // TODO Step 1
    }

    /**
     * Steps 2–3 — the next id.
     * - timestamp = `clock() - epochMillis`. Negative → `IllegalStateException`.
     * - Step 3: timestamp smaller than `lastTimestamp` (clock went backwards) → `IllegalStateException` (could create duplicates).
     * - Same millisecond as last time → `sequence + 1`; new millisecond → `sequence = 0`.
     * - Step 3: if the sequence would go past 4095, keep calling `clock()` until the millisecond changes, then use sequence 0.
     * - Remember `lastTimestamp`, then pack the three parts with `shl` / `or` (see the layout above).
     */
    fun nextId(): Long {
        TODO("Step 2")
    }
}

/**
 * Step 1 — split an id back into its parts:
 * timestamp = `id shr 22`; workerId = `(id shr 12) and 0x3FF` (10 bits); sequence = `id and 0xFFF` (12 bits).
 */
fun decode(id: Long): SnowflakeParts {
    TODO("Step 1")
}
