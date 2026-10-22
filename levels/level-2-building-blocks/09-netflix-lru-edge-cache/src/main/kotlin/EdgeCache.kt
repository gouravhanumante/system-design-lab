class EdgeCache(private val capacityBytes: Long) {

    var usedBytes: Long = 0
        private set
    var hits: Int = 0
        private set
    var misses: Int = 0
        private set
    var evictions: Int = 0
        private set

    fun get(key: String): ByteArray? {
        TODO()
    }

    fun put(key: String, value: ByteArray) {
        TODO()
    }
}
