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

    fun handle(req: Request): Response {
        TODO("Route by path, then by method")
    }
}
