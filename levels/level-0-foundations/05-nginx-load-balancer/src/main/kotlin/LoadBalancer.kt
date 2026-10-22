enum class Strategy { ROUND_ROBIN, LEAST_CONNECTIONS }

class LoadBalancer(private val strategy: Strategy, private val fall: Int = 3, private val rise: Int = 2) {

    fun addServer(name: String) {
        TODO()
    }

    fun removeServer(name: String) {
        TODO()
    }

    fun healthCheck(name: String, passed: Boolean) {
        TODO()
    }

    fun isHealthy(name: String): Boolean {
        TODO()
    }

    fun acquire(): String? {
        TODO()
    }

    fun release(name: String) {
        TODO()
    }

    fun activeConnections(name: String): Int {
        TODO()
    }
}
