/** Provided: FNV-1a 32-bit hash, as an unsigned position on the ring (0 .. 2^32 - 1). */
fun ringPosition(s: String): Long {
    var h = 0x811C9DC5.toInt()
    for (b in s.toByteArray()) {
        h = h xor (b.toInt() and 0xFF)
        h *= 0x01000193
    }
    h = h xor (h ushr 16)
    h *= 0x45d9f3b
    h = h xor (h ushr 16)
    return h.toLong() and 0xFFFFFFFFL
}

class HashRing(private val virtualNodes: Int) {

    fun addNode(node: String) {
        TODO()
    }

    fun removeNode(node: String) {
        TODO()
    }

    fun nodeFor(key: String): String? {
        TODO()
    }

    fun nodes(): Set<String> {
        TODO()
    }
}
