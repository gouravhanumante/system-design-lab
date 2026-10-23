enum class Strategy { ROUND_ROBIN, LEAST_CONNECTIONS }

class LoadBalancer(private val strategy: Strategy, private val fall: Int = 3, private val rise: Int = 2) {

    // ───────────── State ─────────────

    /** Everything the balancer knows about one backend server. */
    private class Server {
        /** New servers start healthy. */
        var healthy = true
        /** Failed health checks in a row (reset to 0 by a pass). */
        var fails = 0
        /** Passed health checks in a row (reset to 0 by a fail). */
        var passes = 0
        /** Requests currently being handled by this server. */
        var active = 0
    }

    /** name → server, in the order they were added (LinkedHashMap keeps that order for round robin). */
    private val servers = LinkedHashMap<String, Server>()

    /** Round robin: position in the server list where the next search starts. */
    private var cursor = 0

    /** Helper (done for you): the server with this name, or `IllegalArgumentException` if unknown. */
    private fun serverOrThrow(name: String): Server = servers[name] ?: throw IllegalArgumentException("unknown server $name")

    // ───────────── Managing servers ─────────────

    /** Step 1 — add a healthy server with 0 connections. Name already added → `IllegalArgumentException`. */
    fun addServer(name: String) {
        TODO("Step 1")
    }

    /** Step 1 — remove it (unknown name: do nothing). It must never be chosen again. */
    fun removeServer(name: String) {
        TODO("Step 1")
    }

    /**
     * Step 3 — record one health check result (unknown server → `IllegalArgumentException`).
     * - Pass: `passes++`, `fails = 0`. Fail: `fails++`, `passes = 0`.
     * - A healthy server becomes unhealthy after `fall` fails in a row.
     * - An unhealthy server becomes healthy again after `rise` passes in a row.
     */
    fun healthCheck(name: String, passed: Boolean) {
        TODO("Step 3")
    }

    /** Step 3 — is this server healthy? Unknown → `IllegalArgumentException`. */
    fun isHealthy(name: String): Boolean {
        TODO("Step 3")
    }

    // ───────────── Picking a server ─────────────

    /**
     * Steps 2, 4, 5 — choose a server for a new request, add 1 to its `active`, and return its name.
     * Return `null` if there are no healthy servers.
     * - ROUND_ROBIN: starting at `cursor`, go through the servers in order (wrapping around) and take the first
     *   healthy one; then move `cursor` to just after it.
     * - LEAST_CONNECTIONS: the healthy server with the smallest `active`; on a tie, the one added first.
     */
    fun acquire(): String? {
        TODO("Step 2")
    }

    /** Step 2 — a request finished: `active - 1`, but never below 0. Unknown name: do nothing. */
    fun release(name: String) {
        TODO("Step 2")
    }

    /** Step 1 — current `active` for this server. Unknown → `IllegalArgumentException`. */
    fun activeConnections(name: String): Int {
        TODO("Step 1")
    }
}
