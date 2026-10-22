class ReplicatedDb(private val replicaCount: Int = 2, private val lagSeconds: Long = 5) {

    var lastReadFrom: String = ""
        private set
    var staleReads: Int = 0
        private set

    fun write(user: String, key: String, value: String, now: Long) {
        TODO()
    }

    fun read(user: String, key: String, now: Long): String? {
        TODO()
    }
}
