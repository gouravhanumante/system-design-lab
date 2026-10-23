data class Request(
    val method: String,
    val path: String,
    val body: Map<String, String> = emptyMap(),
    val query: Map<String, String> = emptyMap(),
)

data class Response(
    val status: Int,
    val body: Map<String, String> = emptyMap(),
    val items: List<Map<String, String>> = emptyList(),
)

class IssuesApi {

    // ───────────── State (your "database") ─────────────

    /** All issues, by id. Each issue is a map like {"id": "1", "title": "...", "state": "open"}. A TreeMap keeps them sorted by id. */
    private val issues = java.util.TreeMap<Int, MutableMap<String, String>>()

    /** The id the next created issue gets: 1, 2, 3, … (never reused, even after a delete). */
    private var nextId = 1

    // ───────────── Step 1: routing ─────────────

    /**
     * Decide which endpoint this is and call the function for it:
     *
     * | path            | method | call                    |
     * |-----------------|--------|-------------------------|
     * | `/issues`       | POST   | createIssue(req)        |
     * | `/issues`       | GET    | listIssues(req)         |
     * | `/issues/{id}`  | GET    | getIssue(id)            |
     * | `/issues/{id}`  | PATCH  | updateIssue(id, req)    |
     * | `/issues/{id}`  | DELETE | deleteIssue(id)         |
     *
     * - A known path with any other method → `error(405, ...)`.
     * - Any other path → `error(404, ...)`.
     * - `{id}` that isn't a positive whole number (`abc`, `0`, `-1`) → `error(404, ...)`.
     *
     * Tip: `req.path.split("/")` turns `"/issues/3"` into `["", "issues", "3"]`, and `"3".toIntOrNull()` gives `3` (or null).
     */
    fun handle(req: Request): Response {
        TODO("Step 1: route the request")
    }

    // ───────────── Steps 2–6: one function per endpoint ─────────────

    /**
     * Step 2 — `POST /issues`.
     * - `req.body["title"]` missing or blank → `error(400, ...)`.
     * - Otherwise store a new issue `{id, title, state = "open"}` under `nextId`, increase `nextId`,
     *   and return `Response(201, body = the new issue)`.
     */
    private fun createIssue(req: Request): Response {
        TODO("Step 2")
    }

    /**
     * Step 3 — `GET /issues/{id}`.
     * Unknown id → `error(404, ...)`; otherwise `Response(200, body = the issue)`.
     */
    private fun getIssue(id: Int): Response {
        TODO("Step 3")
    }

    /**
     * Step 4 — `GET /issues`.
     * - `req.query["state"]` may be absent (return all), `"open"` or `"closed"` (return only those). Anything else → `error(400, ...)`.
     * - Return `Response(200, items = the issues, sorted by id)`.
     */
    private fun listIssues(req: Request): Response {
        TODO("Step 4")
    }

    /**
     * Step 5 — `DELETE /issues/{id}`.
     * Unknown id → `error(404, ...)`; otherwise remove it and return `Response(204)` (empty body).
     */
    private fun deleteIssue(id: Int): Response {
        TODO("Step 5")
    }

    /**
     * Step 6 — `PATCH /issues/{id}` with optional `title` and/or `state` in `req.body`.
     * - Unknown id → `error(404, ...)`.
     * - `title` present but blank, or `state` present but not `"open"`/`"closed"` → `error(400, ...)` and change NOTHING.
     * - Otherwise change only the fields that were sent and return `Response(200, body = the updated issue)`.
     */
    private fun updateIssue(id: Int, req: Request): Response {
        TODO("Step 6")
    }

    // ───────────── Helper (done for you) ─────────────

    /** Every error response looks like `{"error": "<message>"}`. */
    private fun error(status: Int, message: String) = Response(status, body = mapOf("error" to message))
}
