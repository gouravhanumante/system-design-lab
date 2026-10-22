data class Versioned(val content: String, val revision: Int)

sealed class PushResult {
    data class Accepted(val revision: Int) : PushResult()
    data class Conflict(val serverCopy: Versioned) : PushResult()
}

class SyncServer {

    fun push(name: String, baseRevision: Int, content: String): PushResult {
        TODO()
    }

    fun fetch(name: String): Versioned? {
        TODO()
    }

    fun snapshot(): Map<String, Versioned> {
        TODO()
    }
}

class SyncClient(private val device: String, private val server: SyncServer) {

    fun edit(file: String, content: String) {
        TODO()
    }

    fun read(file: String): String? {
        TODO()
    }

    fun files(): Set<String> {
        TODO()
    }

    fun sync() {
        TODO()
    }
}
